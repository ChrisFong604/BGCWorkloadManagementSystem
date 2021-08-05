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
		/*
		 * Property prop = new Property(); model.put("property", prop);
		 */

		try (Connection connection = dataSource.getConnection()) {

			Statement stmt = connection.createStatement();

			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS projects (id varchar(40), name varchar(40), startdate date, enddate date, resources text, capacities text, capacities2 text, color varchar(40))");

			String sql = "SELECT * FROM projects ORDER BY startdate ASC";
			ResultSet rs = stmt.executeQuery(sql);

			ArrayList<Project> output = new ArrayList<>();
			while (rs.next()) {
				Project proj = new Project();
				proj.setId(rs.getString("id"));
				proj.setName(rs.getString("name"));
				proj.setStart(rs.getDate("startdate"));
				proj.setEnd(rs.getDate("enddate"));
				proj.setResources(rs.getString("resources"));
				proj.setCapacities(rs.getString("capacities"));
				proj.setCapacities2(rs.getString("capacities2"));
				proj.setColor(rs.getString("color"));
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

	/*
	 * public String filterByPropertyProjComponent(Map<String, Object> model,
	 * Property prop, boolean flag, boolean edit) { String filterBy =
	 * prop.getFilterBy(); String value = prop.getValue();
	 * 
	 * try (Connection connection = dataSource.getConnection()) { Statement stmt =
	 * connection.createStatement(); String sql = "SELECT * FROM projects WHERE " +
	 * filterBy + " = '" + value + "' "; // System.out.println(sql);
	 * 
	 * ResultSet rs = stmt.executeQuery(sql);
	 * 
	 * ArrayList<Project> output = new ArrayList<>(); while (rs.next()) { Project
	 * proj = new Project(); proj.setId(rs.getInt("id"));
	 * proj.setName(rs.getString("name")); proj.setStart(rs.getDate("startdate"));
	 * proj.setEnd(rs.getDate("enddate"));
	 * 
	 * output.add(proj); } model.put("projects", output);
	 * 
	 * if (flag && edit) { return "projects/allProjects"; } else { return
	 * "readOnly/allProjects_r"; } } catch (Exception e) { model.put("message",
	 * e.getMessage()); return "error"; } }
	 */

	public String returnProjectCreateComponent(Map<String, Object> model, boolean flag, boolean edit) throws Exception {
		Project project = new Project();
		model.put("project", project);
		if (flag && edit) {
			return "projects/createProject";
		} else {
			return "userNotFound";
		}
	}

	public String handleProjectSubmitComponent(Map<String, Object> model, Project project) throws Exception {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();

			final String UniqueID = UUID.randomUUID().toString().replace("-", "");

			stmt.executeUpdate(
					"CREATE TABLE IF NOT EXISTS projects (id varchar(40), name varchar(40), startdate date, enddate date, resources text, capacities text, capacities2 text)");
			// Creates a universally unique ID for each employee (Only exists in Database)

			String sql = "INSERT INTO projects ( id, name, startdate, enddate, resources, capacities, capacities2, color ) VALUES ('"
					+ UniqueID + "','" + project.getName() + "','" + project.getStart() + "','" + project.getEnd()
					+ "','" + project.getResources() + "','" + project.getCapacities() + "','"
					+ project.getCapacities2() + "','" + project.getColor() + "')";
			stmt.executeUpdate(sql);

			return "redirect:/projects"; // Directly returns to project homepage
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
	}

	public String deleteProjectComponent(Map<String, Object> model, @RequestParam String pid) {
		try (Connection connection = dataSource.getConnection()) {
			String sql = "DELETE FROM projects WHERE id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, pid);
			ps.executeUpdate();
			return "redirect:/projects";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
	}

	public String editProjectComponent(Map<String, Object> model, @RequestParam String pid) throws Exception {
		try (Connection connection = dataSource.getConnection()) {
			String sql = "SELECT * FROM projects WHERE id = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, pid);
			ResultSet rs = pstmt.executeQuery();
			Project proj = new Project();
			if (rs.next()) {
				proj.setId(rs.getString("id"));
				proj.setName(rs.getString("name"));
				proj.setStart(rs.getDate("startdate"));
				proj.setEnd(rs.getDate("enddate"));
				proj.setCapacities(rs.getString("capacities"));
				proj.setResources(rs.getString("resources"));
				proj.setCapacities2(rs.getString("capacities2"));
				proj.setColor(rs.getString("color"));
			}

			model.put("project", proj);
			return "projects/editProject";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
	}

	public String handleProjectEditSubmitComponent(Map<String, Object> model, Project project, @RequestParam String pid)
			throws Exception {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();

			String sql = "UPDATE projects SET " + "name='" + project.getName() + "', " + "startdate='"
					+ project.getStart() + "', " + "enddate= '" + project.getEnd() + "', " + "capacities='"
					+ project.getCapacities() + "', " + "capacities2='" + project.getCapacities2() + "', "
					+ "resources='" + project.getResources() + "' " + "WHERE id = '" + project.getId() + "';";

			stmt.executeUpdate(sql);
			return "redirect:/projects"; // Directly returns to employee homepage
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
	}

	public String viewProjectComponent(Map<String, Object> model, @RequestParam String pid) throws Exception {
		try (Connection connection = dataSource.getConnection()) {
			String sql = "SELECT * FROM projects WHERE id =?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, pid);
			ResultSet rs = ps.executeQuery();

			Project proj = new Project();
			if (rs.next()) {
				proj.setId(rs.getString("id"));
				proj.setName(rs.getString("name"));
				proj.setStart(rs.getDate("startdate"));
				proj.setEnd(rs.getDate("enddate"));
				proj.setCapacities(rs.getString("capacities"));
				proj.setResources(rs.getString("resources"));
				proj.setCapacities2(rs.getString("capacities2"));
			}
			model.put("project", proj);

			return "projects/viewProject";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}

	}
}
