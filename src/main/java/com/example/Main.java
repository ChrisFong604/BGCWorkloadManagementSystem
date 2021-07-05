/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;
import java.time.*;

@Controller
@SpringBootApplication
@EnableScheduling
public class Main {

  boolean flag = false;

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index(Map<String, Object> model) {
    return "redirect:/login";
  }

  // Change to PostMapping or whatever for login page later
  @GetMapping("/login")
  String loginPageHandler(Map<String, Object> model) {
    flag = false;
    UserLogin user = new UserLogin();
    model.put("user", user);
    return "login";
  }

  @PostMapping(path = "/login", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String login(Map<String, Object> model, UserLogin user) throws Exception {
    // save the user into the database
    String username = user.getUsername();
    String pw = user.getPassword();

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String sql = "SELECT * FROM login";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        String compareToUserName = rs.getString("username");
        String compareToPW = rs.getString("password");
        if (username.equals(compareToUserName) && pw.equals(compareToPW)) {
          System.out.println("user exists");
          flag = true;
          return "redirect:/dashboard";
        }
      }
      return "userNotFound";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/dashboard")
  String dashboard(Map<String, Object> model) {
    if (flag) {
      return "index";
    } else {
      return "userNotFound";
    }
  }

  @GetMapping("/manager/create")
  public String createManager(Map<String, Object> model) {
    UserLogin user = new UserLogin();
    model.put("user", user);
    return "manager";
  }

  // adding users
  @PostMapping(path = "/manager/create", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String addManagerToDatabase(Map<String, Object> model, UserLogin user) throws Exception {
    // save the user into the database
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS login (id serial, username varchar(20), password varchar(20))");
      String sql = "INSERT INTO login (username, password) VALUES ('" + user.getUsername() + "', '" + user.getPassword()
          + "')";
      stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

      ResultSet rs = stmt.getGeneratedKeys();
      if (rs.next()) {
        int id = rs.getInt(1);
        user.setID(id);
      }
      return "index";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/employees")
  String returnEmployeeHomepage(Map<String, Object> model) {
    Employee employee = new Employee();

    model.put("employee", employee);

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS employees (name varchar(40), position varchar(10), role varchar(40),"
              + "team varchar(40), status boolean, capacity float, startdate date, enddate date)");
      ResultSet rs = stmt.executeQuery("SELECT * FROM employees");

      ArrayList<Employee> output = new ArrayList<Employee>();
      while (rs.next()) {
        Employee temp = new Employee();
        temp.setName(rs.getString("name"));
        temp.setPosition(rs.getString("position"));
        temp.setRole(rs.getString("role"));
        temp.setTeam(rs.getString("team"));
        temp.setStatus(rs.getBoolean("status"));
        temp.setCapacity(rs.getFloat("capacity"));
        temp.setStart(rs.getDate("startdate"));
        temp.setEnd(rs.getDate("enddate"));

        output.add(temp);
      }
      model.put("employees", output);

      return "employees/allEmployees";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/employees/metrics")
  String returnEmployeeMetrics() {
    return "employees/employeemetrics";
  }

  @GetMapping("/employees/create")
  public String returnEmployeeCreate(Map<String, Object> model) throws Exception {
    Employee employee = new Employee();
    model.put("employee", employee);
    return "employees/createEmployee";
  }

  @PostMapping(path = "/employees/create", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String handleEmployeeProfileSubmit(Map<String, Object> model, Employee employee) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS employees (id serial, name varchar(40), position varchar(10), role varchar(40),"
              + "team varchar(40), status boolean, capacity float, startdate date, enddate date)");
      String sql = "INSERT INTO employees (name, position, role, team, status, capacity, startdate, enddate) VALUES ('"
          + employee.getName() + "','" + employee.getPosition() + "','" + employee.getRole() + "','"
          + employee.getTeam() + "'," + employee.getStatus() + "," + 0.875 + ",'" + employee.getStart() + "','"
          + employee.getEnd() + "')";
      if (employee.getPosition() == "intern") {
        System.out.println(employee.getPosition() == "intern");
      } else {
        System.out.println(employee.getPosition() == "intern");
      }
      stmt.executeUpdate(sql);
      return "redirect:/employees"; // Directly returns to employee homepage
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @Scheduled(cron = "*/10 * * * * *", zone = "Canada/Pacific")
  public void scheduledRampCheck() {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT id,   FROM employees");

      while (rs.next()) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date start = rs.getDate("startdate");
        java.sql.Date current = new java.sql.Date(System.currentTimeMillis());

        current = start.getTime() - current.getTime();

        int daysWorked; //2021-12-15 - 2019-07-05
        switch(daysWorked) {
          case ():
            stmt.executeUpdate("UPDATE employees SET capacity=’0.1’ WHERE id=" + rs.getInt("id"));
          case < 14:

          case < 21:

          case > 21:
        }
        Employee temp = new Employee();
        temp.setStart(rs.getDate("startdate"));
        temp.setEnd(rs.getDate("enddate"));
      }

    } catch (Exception e) {
    }
  }

  @RequestMapping("/db")
  String db(Map<String, Object> model) {

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

}