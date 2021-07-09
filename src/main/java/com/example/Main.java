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
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.time.*;
import java.time.format.DateTimeFormatter;

@Controller
@SpringBootApplication
@EnableScheduling
public class Main {

  boolean flag = false;
  boolean edit = false;

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
    edit = false;
    UserLogin user = new UserLogin();
    model.put("user", user);
    return "login";
  }

  @PostMapping(path = "/login", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String login(Map<String, Object> model, UserLogin user) throws Exception {
    String username = user.getUsername();
    String pw = user.getPassword();
    String access = user.getAccess();

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String sql = "SELECT * FROM login";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        String compareToUserName = rs.getString("username");
        String compareToPW = rs.getString("password");
        String compareToAccess = rs.getString("access");
        if (username.equals(compareToUserName) && pw.equals(compareToPW)) {
          System.out.println("user exists");
          flag = true;
          String ed = "edit";
          if (compareToAccess.equals(ed)) {
            edit = true;
          }
          /*
           * System.out.println(edit); System.out.println(compareToAccess);
           * System.out.println(s);
           */
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
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String sql = "SELECT * FROM employees";
      ResultSet rs = stmt.executeQuery(sql);

      ArrayList<Employee> output = new ArrayList<Employee>();
      while (rs.next()) {
        Employee emp = new Employee();
        emp.setName(rs.getString("name"));
        emp.setPosition(rs.getString("position"));
        emp.setRole(rs.getString("role"));
        emp.setTeam(rs.getString("team"));
        emp.setStatus(rs.getBoolean("status"));
        emp.setCapacity(rs.getFloat("capacity"));
        emp.setStart(rs.getDate("startdate"));
        emp.setEnd(rs.getDate("enddate"));
        output.add(emp);
      }
      model.put("employees", output);

      Statement stmt2 = connection.createStatement();
      String sql2 = "SELECT * FROM employees2";
      ResultSet rs2 = stmt.executeQuery(sql2);

      ArrayList<Employee> output2 = new ArrayList<Employee>();
      while (rs2.next()) {
        Employee emp2 = new Employee();
        emp2.setName(rs2.getString("name"));
        emp2.setPosition(rs2.getString("position"));
        emp2.setRole(rs2.getString("role"));
        emp2.setTeam(rs2.getString("team"));
        emp2.setStatus(rs2.getBoolean("status"));
        emp2.setCapacity(rs2.getFloat("capacity"));
        emp2.setStart(rs2.getDate("startdate"));
        emp2.setEnd(rs2.getDate("enddate"));
        output2.add(emp2);
      }
      model.put("employees2", output2);

      if (flag && edit) {
        return "index";
      } else if (flag && !edit) {
        return "readOnly/index_r";
      } else {
        return "userNotFound";
      }
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }

  }

  @GetMapping("/manager/create")
  public String createManager(Map<String, Object> model) {
    UserLogin user = new UserLogin();
    model.put("user", user);

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
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

  // adding users
  @PostMapping(path = "/manager/create", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String addManagerToDatabase(Map<String, Object> model, UserLogin user) throws Exception {
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

  // filter by attributes
  @GetMapping("/employees")
  String returnEmployeeHomepage(Map<String, Object> model) {
    Property prop = new Property();
    model.put("property", prop);

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String sql = "SELECT * FROM employees";
      ResultSet rs = stmt.executeQuery(sql);

      ArrayList<Employee> output = new ArrayList<Employee>();
      while (rs.next()) {
        Employee emp = new Employee();
        emp.setId(rs.getString("id"));
        emp.setName(rs.getString("name"));
        emp.setPosition(rs.getString("position"));
        emp.setRole(rs.getString("role"));
        emp.setTeam(rs.getString("team"));
        emp.setStatus(rs.getBoolean("status"));
        emp.setCapacity(rs.getFloat("capacity"));
        emp.setStart(rs.getDate("startdate"));
        emp.setEnd(rs.getDate("enddate"));

        output.add(emp);
      }
      model.put("employees", output);

      if (flag && edit) {
        return "employees/allEmployees";
      } else if (flag && !edit) {
        return "readOnly/allEmployees_r";
      } else {
        return "userNotFound";
      }
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  // filtered results
  @PostMapping(path = "/employees", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String filterByProperty(Map<String, Object> model, Property prop) {
    String filterBy = prop.getFilterBy();
    String value = prop.getValue();

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String sql = "SELECT * FROM employees WHERE " + filterBy + " = '" + value + "' ";
      // System.out.println(sql);

      ResultSet rs = stmt.executeQuery(sql);

      ArrayList<Employee> output = new ArrayList<Employee>();
      while (rs.next()) {
        Employee emp = new Employee();
        emp.setId(rs.getString("id"));
        emp.setName(rs.getString("name"));
        emp.setPosition(rs.getString("position"));
        emp.setRole(rs.getString("role"));
        emp.setTeam(rs.getString("team"));
        emp.setStatus(rs.getBoolean("status"));
        emp.setCapacity(rs.getFloat("capacity"));
        emp.setStart(rs.getDate("startdate"));
        emp.setEnd(rs.getDate("enddate"));

        output.add(emp);
      }
      model.put("employees", output);

      if (flag && edit) {
        return "employees/allEmployees";
      } else {
        return "readOnly/allEmployees_r";
      }
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  // deleting employees
  @GetMapping("/employees/deleted")
  public String deleteEmployee(Map<String, Object> model, @RequestParam String e_id) {
    try (Connection connection = dataSource.getConnection()) {
      String sql = "DELETE FROM employees WHERE id =?";
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setString(1, e_id);
      ps.executeUpdate();

      return "redirect:/employees";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/employees/metrics")
  String returnEmployeeMetrics(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String sql = "SELECT * FROM employees";
      ResultSet rs = stmt.executeQuery(sql);

      ArrayList<Employee> output = new ArrayList<Employee>();
      while (rs.next()) {
        Employee emp = new Employee();
        emp.setName(rs.getString("name"));
        emp.setPosition(rs.getString("position"));
        emp.setRole(rs.getString("role"));
        emp.setTeam(rs.getString("team"));
        emp.setStatus(rs.getBoolean("status"));
        emp.setCapacity(rs.getFloat("capacity"));
        emp.setStart(rs.getDate("startdate"));
        emp.setEnd(rs.getDate("enddate"));
        output.add(emp);
      }
      model.put("employees", output);

      Statement stmt2 = connection.createStatement();
      String sql2 = "SELECT * FROM employees2";
      ResultSet rs2 = stmt.executeQuery(sql2);

      ArrayList<Employee> output2 = new ArrayList<Employee>();
      while (rs2.next()) {
        Employee emp2 = new Employee();
        emp2.setName(rs2.getString("name"));
        emp2.setPosition(rs2.getString("position"));
        emp2.setRole(rs2.getString("role"));
        emp2.setTeam(rs2.getString("team"));
        emp2.setStatus(rs2.getBoolean("status"));
        emp2.setCapacity(rs2.getFloat("capacity"));
        emp2.setStart(rs2.getDate("startdate"));
        emp2.setEnd(rs2.getDate("enddate"));
        output2.add(emp2);
      }
      model.put("employees2", output2);

      return "employees/employeemetrics";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/employees/create")
  public String returnEmployeeCreate(Map<String, Object> model) throws Exception {
    Employee employee = new Employee();
    model.put("employee", employee);
    if (flag && edit) {
      return "employees/createEmployee";
    } else {
      return "userNotFound";
    }
  }

  @PostMapping(path = "/employees/create", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String handleEmployeeProfileSubmit(Map<String, Object> model, Employee employee) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS employees (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
              + "team varchar(40), status boolean, capacity float, startdate date, enddate date)");
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS employees2 (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
              + "team varchar(40), status boolean, capacity float, startdate date, enddate date)");

      // Creates a universally unique ID for each employee (Only exists in Database)
      final String UniqueID = UUID.randomUUID().toString().replace("-", "");

      String sql = "INSERT INTO employees (id, name, position, role, team, status, capacity, startdate, enddate) VALUES ('"
          + UniqueID + "','" + employee.getName() + "','" + employee.getPosition() + "','" + employee.getRole() + "','"
          + employee.getTeam() + "'," + employee.getStatus() + "," + 0.1 + ",'" + employee.getStart() + "','"
          + employee.getEnd() + "')";

      String sql2 = "INSERT INTO employees2 (id, name, position, role, team, status, capacity, startdate, enddate) VALUES ('"
          + UniqueID + "','" + employee.getName() + "','" + employee.getPosition() + "','" + employee.getRole() + "','"
          + employee.getTeam() + "'," + employee.getStatus() + "," + 0.1 + ",'" + employee.getStart() + "','"
          + employee.getEnd() + "')";

      stmt.executeUpdate(sql);
      stmt.executeUpdate(sql2);
      return "redirect:/employees"; // Directly returns to employee homepage
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/employees/edit")
  public String editEmployee(Map<String, Object> model, @RequestParam String rid) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      String sql = "SELECT * FROM employees WHERE id = ?";
      PreparedStatement pstmt = connection.prepareStatement(sql);
      pstmt.setString(1, rid);
      ResultSet rs = pstmt.executeQuery();
      Employee emp = new Employee();
      if (rs.next()) {
        emp.setId(rs.getString("id"));
        emp.setName(rs.getString("name"));
        System.out.println(emp.getName());
        emp.setPosition(rs.getString("position"));
        System.out.println(emp.getPosition());
        emp.setRole(rs.getString("role"));
        emp.setTeam(rs.getString("team"));
        emp.setStatus(rs.getBoolean("status"));
        emp.setCapacity(rs.getFloat("capacity"));
        emp.setStart(rs.getDate("startdate"));
        emp.setEnd(rs.getDate("enddate"));
      }

      model.put("employee", emp);
      return "employees/editEmployee";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @PostMapping(path = "/employees/edit", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String handleEmployeeEditSubmit(Map<String, Object> model, Employee employee, @RequestParam String rid)
      throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      /*
       * stmt.executeUpdate(
       * "CREATE TABLE IF NOT EXISTS employees (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
       * +
       * "team varchar(40), status boolean, capacity float, startdate date, enddate date)"
       * );
       * 
       * // Creates a universally unique ID for each employee (Only exists in
       * Database) final String UniqueID = UUID.randomUUID().toString().replace("-",
       * "");
       */
      String sql = "UPDATE employees SET " + "name = '" + employee.getName() + "', " + "position = '"
          + employee.getPosition() + "', " + "role = '" + employee.getRole() + "', " + "team = '" + employee.getTeam()
          + "', " + "status = " + employee.getStatus() + ", " + "startdate = '" + employee.getStart() + "', "
          + "enddate = '" + employee.getEnd() + "' " + "WHERE id = '" + employee.getId() + "';";
      System.out.println(rid);

      /*
       * String sql =
       * "INSERT INTO employees (id, name, position, role, team, status, capacity, startdate, enddate) VALUES ('"
       * + UniqueID + "','" + employee.getName() + "','" + employee.getPosition() +
       * "','" + employee.getRole() + "','" + employee.getTeam() + "'," +
       * employee.getStatus() + "," + 0.1 + ",'" + employee.getStart() + "','" +
       * employee.getEnd() + "')";
       */

      stmt.executeUpdate(sql);
      return "redirect:/employees"; // Directly returns to employee homepage
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  /*
   * Checks each day at 5:00 a.m the current capacity for all employees based on
   * their start date, and adjusts it in the database accordingly
   */
  @Scheduled(cron = "*/30 * * * * *", zone = "Canada/Pacific")
  public void scheduledRampCheck() {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM employees");

      while (rs.next()) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        java.util.Date start = rs.getDate("startdate");
        LocalDate current = LocalDate.now(ZoneId.of("Canada/America"));

        long daysWorked = start.getTime();
        daysWorked /= 86400000; // milliseconds to days
        int weeksWorked = (int) (Math.floor(daysWorked / 7));

        System.out.println("Current Time: " + current.format(formatter));
        switch (weeksWorked) {
          case 1:
            stmt.executeUpdate("UPDATE employees SET capacity=0.100 WHERE id=" + rs.getString("id"));
            break;
          case 2:
            stmt.executeUpdate("UPDATE employees SET capacity=0.250 WHERE id=" + rs.getString("id"));
            break;
          case 3:
            stmt.executeUpdate("UPDATE employees SET capacity=0.500 WHERE id=" + rs.getString("id"));
            break;
          case 4:
            stmt.executeUpdate("UPDATE employees SET capacity=0.875 WHERE id=" + rs.getString("id"));
            break;
          case 5:
            stmt.executeUpdate("UPDATE employees SET capacity=0.875 WHERE id=" + rs.getString("id"));
            break;
        }
      }

    } catch (Exception e) {
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