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
public class ProjectsComponent {
	
	@Autowired
	private DataSource dataSource;
	
	String returnProjectHomepageComponent(Map<String, Object> model, boolean flag, boolean edit) {
	    try {
	      if (flag && edit) {
	        return "projects/allProjects";
	      } else if (flag && !edit) {
	        return "readOnly/allProjects_r";
	      } else {
	        return "userNotFound";
	      }
	    } catch (Exception e) {
	      model.put("message", e.getMessage());
	      return "error";
	    }
	 }
	
	public String returnProjectCreateComponent(Map<String, Object> model, boolean flag, boolean edit) throws Exception {
	    Project project = new Project();
	    model.put("project", project);
	    if (flag && edit) {
	      return "projects/createProject";
	    } else {
	      return "userNotFound";
	    }
	}
	
	public String handleProjectSubmitComponent(Map<String, Object> model,Project project) throws Exception {
	    try (Connection connection = dataSource.getConnection()) {
	      Statement stmt = connection.createStatement();

	            stmt.executeUpdate(
	          "CREATE TABLE IF NOT EXISTS projects (id serial, name varchar(40), start date, end date, workload float)");

	      // Creates a universally unique ID for each employee (Only exists in Database)

	      String sql = "INSERT INTO projects ( name, start, end, workload ) VALUES ('" + project.getName() + "','" 
	      + project.getStart() + "','" + project.getEnd() + "','" + project.getWorkLoad() +"')";

	      stmt.executeUpdate(sql);

	      return "redirect:/projects"; // Directly returns to project homepage
	    } catch (Exception e) {
	      model.put("message", e.getMessage());
	      return "error";
	    }
	  }
	
}
