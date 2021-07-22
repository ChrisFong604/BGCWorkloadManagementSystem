package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
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

@Controller
@SpringBootApplication
@EnableScheduling
public class CreateManagerComponent {
	  
	 @Value("${spring.datasource.url}")

	  @Autowired
	  DataSource dataSource;
	
	  public String createManagerClass(Map<String, Object> model, boolean flag, boolean edit) {
	    UserLogin user = new UserLogin();
	    model.put("user", user);
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
	      return "error";
	    }
	  }
	  
	  public String addManagerToDatabaseClass(Map<String, Object> model, UserLogin user) throws Exception {
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
	  
}
