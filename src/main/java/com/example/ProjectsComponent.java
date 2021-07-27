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
		/*Property prop = new Property();
		model.put("property", prop);*/
	  
	      try (Connection connection = dataSource.getConnection()) {
	        
	        Statement stmt = connection.createStatement();
	        
	        stmt.executeUpdate(
	            "CREATE TABLE IF NOT EXISTS projects (id serial, name varchar(40), startdate date, enddate date)");
	  
	        String sql = "SELECT * FROM projects ORDER BY startdate ASC";
	        ResultSet rs = stmt.executeQuery(sql);
	  
	        ArrayList<Project> output = new ArrayList<>();
	        while (rs.next()) {
	          Project proj = new Project();
	          proj.setId(rs.getInt("id"));
	          proj.setName(rs.getString("name"));
	          proj.setStart(rs.getDate("startdate"));
	          proj.setEnd(rs.getDate("enddate"));
	  
	          output.add(proj);
	        }
	        model.put("projects", output);
	        
	        /*** visual ****/ 
	  
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
	
    /*public String filterByPropertyProjComponent(Map<String, Object> model, Property prop, boolean flag, boolean edit) {
      String filterBy = prop.getFilterBy();
      String value = prop.getValue();
  
      try (Connection connection = dataSource.getConnection()) {
        Statement stmt = connection.createStatement();
        String sql = "SELECT * FROM projects WHERE " + filterBy + " = '" + value + "' ";
        // System.out.println(sql);
  
        ResultSet rs = stmt.executeQuery(sql);
  
        ArrayList<Project> output = new ArrayList<>();
        while (rs.next()) {
          Project proj = new Project();
          proj.setId(rs.getInt("id"));
          proj.setName(rs.getString("name"));
          proj.setStart(rs.getDate("startdate"));
          proj.setEnd(rs.getDate("enddate"));
  
          output.add(proj);
        }
        model.put("projects", output);
  
        if (flag && edit) {
          return "projects/allProjects";
        } else {
          return "readOnly/allProjects_r";
        }
      } catch (Exception e) {
        model.put("message", e.getMessage());
        return "error";
      }
    }*/
	
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
		          "CREATE TABLE IF NOT EXISTS projects (id serial, name varchar(40), startdate date, enddate date)");
		      // Creates a universally unique ID for each employee (Only exists in Database)

		      String sql = "INSERT INTO projects ( name, startdate, enddate ) VALUES ('" + project.getName() + "','"
		          + project.getStart() + "','" + project.getEnd() + "')";
		      stmt.executeUpdate(sql);

		      return "redirect:/projects"; // Directly returns to project homepage
	    } catch (Exception e) {
	      model.put("message", e.getMessage());
	      return "error";
	    }
	}

	public String returnProjectCreateWeeklyWorkloadComponent(Map<String, Object> model, boolean flag, boolean edit) throws Exception {
		Project project = new Project;
		model.put("project", project)
		if (flag && edit) {
	      return "projects/createProjectWorkload";
	    } else {
	      return "userNotFound";
	    }
	}

	public String handleProjectWorkloadSubmitComponent (Map<String, Object> model, Project project, @RequestParam p_id) throws Exception {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();
			stmt.executeQuery('SELECT P.ProjectID, (CEILING(DATE_PART('day', P.enddate – P.startdate)/7)) AS week_num
			FROM Projects P');
	}
	
	public String deleteProjectComponent(Map<String, Object> model, @RequestParam String p_id) {
		try (Connection connection = dataSource.getConnection()) {
			String sql = "DELETE FROM projects WHERE id =?";
		    PreparedStatement ps = connection.prepareStatement(sql);
		    ps.setInt(1, Integer.parseInt(p_id));
		    ps.executeUpdate();
		    return "redirect:/projects";
	    } catch (Exception e) {
	      model.put("message", e.getMessage());
	      return "error";
	    }
	}
	
}
