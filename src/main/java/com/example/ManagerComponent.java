package com.example;

//Todo - cleanup dependencies

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.ranges.Range;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.time.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ManagerComponent {
	
	@Autowired
	private DataSource dataSource;
	
	//@Value("${spring.datasource.url}")
	//private String dbUrl; 
	  
  public String createManagerComponent(Map<String, Object> model, boolean flag, boolean edit) {
    UserLogin user = new UserLogin();
    model.put("user", user);
    System.out.println("-- 2 --");
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      System.out.println("-- 3 --");
      String sql = "SELECT * FROM login";
      ResultSet rs = stmt.executeQuery(sql);

      ArrayList<UserLogin> output = new ArrayList<UserLogin>();
      while (rs.next()) {
        if (rs.getInt("id") == 1) {
          continue;
        }
        UserLogin manager = new UserLogin();
        manager.setID(rs.getInt("id"));
        manager.setUsername(rs.getString("username"));
        manager.setPassword(rs.getString("password"));
        manager.setAccess(rs.getString("access"));
        output.add(manager);
      }
      model.put("managers", output);

      if (flag && edit) {
        return "manager";
      } else {
        return "userNotFound";
      }
    } catch (Exception e) {
      model.put("message", e.getMessage());
      System.out.println("cannot connect");
      return "error";
    }
  }
  
  public String addManagerToDatabaseComponent(Map<String, Object> model, UserLogin user) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS login (id serial, username varchar(20), password varchar(20), access varchar(20))");
      String sql = "INSERT INTO login (username, password, access) VALUES ('" + user.getUsername() + "','"
          + user.getPassword() + "','" + user.getAccess() + "')";
      System.out.println(user.getAccess());
      stmt.executeUpdate(sql);

      return "redirect:/manager/create";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }
  
  public String deleteManagerComponent(Map<String, Object> model, @RequestParam String m_id) {
	    try (Connection connection = dataSource.getConnection()) {
	      String sql = "DELETE FROM login WHERE id =?";
	      PreparedStatement ps = connection.prepareStatement(sql);
	      ps.setInt(1, Integer.parseInt(m_id));
	      ps.executeUpdate();

	      return "redirect:/manager/create";
	    } catch (Exception e) {
	      model.put("message", e.getMessage());
	      return "error";
	    }
	  }
	  
}
