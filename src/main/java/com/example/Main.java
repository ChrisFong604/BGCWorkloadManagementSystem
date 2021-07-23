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

//test

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
public class Main {

  boolean flag = false;
  boolean edit = false;

  @Value("${spring.datasource.url}")
  private String dbUrl; 

  @Autowired
  private DataSource dataSource;
  @Autowired
  private ManagerComponent managerComponent;
  @Autowired
  private EmployeesComponent employeesComponent;
  @Autowired
  private DashboardComponent dashboardComponent;
  @Autowired
  private ProjectsComponent projectsComponent;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
    //SpringApplication.run(createManager.class, args);
  }
  
  /************ LOGIN ************/

  @RequestMapping("/")
  String index(Map<String, Object> model) {
    return "redirect:/login";
  }

  // Change to PostMapping or whatever for login page later
  @GetMapping("/login")
  String loginPageHandler(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate(
    		  "CREATE TABLE IF NOT EXISTS login (id serial, username varchar(20), password varchar(20), access varchar(20))");
      flag = false;
      edit = false;
    
      
      UserLogin user = new UserLogin();
      model.put("user", user);
      return "login";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @PostMapping(path = "/login", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String login(Map<String, Object> model, UserLogin user) throws Exception {
    String username = user.getUsername();
    String pw = user.getPassword();
    String access = user.getAccess();

    if (username.equals("admin") && pw.equals("123")) {
      flag = true;
      edit = true;
      return "redirect:/dashboard";
    }

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
  
  /************ DASHBOARD ************/

  @GetMapping("/dashboard/workload")
  String workload(Map<String, Object> model) {
    return dashboardComponent.workloadComponent(model, flag, edit);
  }

  @GetMapping("/dashboard")
  String dashboard(Map<String, Object> model) {
	  return dashboardComponent.dashboardComponent(model, flag, edit);
  }

  @PostMapping(path = "/dashboard", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String getRange(Map<String, Object> model, RangeInput range) throws Exception {
    return dashboardComponent.getRangeComponent(model, range);
  }

  @PostMapping(path = "/deletetabledata")
  public String deletetabledata(Map<String, Object> model) throws Exception {
    return dashboardComponent.deletetabledataComponent(model);
  }
  
  /************ MANAGER ************/

  @GetMapping("/manager/create")
  public String createManager(Map<String, Object> model) {
	  System.out.println("-- pre create --");
	  return managerComponent.createManagerComponent(model, flag, edit);
  }

  // adding users
  @PostMapping(path = "/manager/create", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String addManagerToDatabase(Map<String, Object> model, UserLogin user) throws Exception {
	  System.out.println("-- post --");
	  //createManagerComponent.setDatasource(dataSource);
	  return managerComponent.addManagerToDatabaseComponent(model, user);
  }

  // deleting managers
  @GetMapping("/manager/deleted")
  public String deleteManager(Map<String, Object> model, @RequestParam String m_id) {
	  return managerComponent.deleteManagerComponent(model, m_id);
  }

  /************ EMPLOYEES ************/
  
  // filter by attributes
  @GetMapping("/employees")
  String returnEmployeeHomepage(Map<String, Object> model) {
    return employeesComponent.returnEmployeeHomepageComponent(model, flag, edit);
  }

  // filtered results
  @PostMapping(path = "/employees", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String filterByProperty(Map<String, Object> model, Property prop) {
    return employeesComponent.filterByPropertyComponent(model, prop, flag, edit);
  }

  // deleting employees
  @GetMapping("/employees/deleted")
  public String deleteEmployee(Map<String, Object> model, @RequestParam String e_id) {
    return employeesComponent.deleteEmployeeComponent(model, e_id);
  }

  @GetMapping("/employees/metrics")
  String returnEmployeeMetrics(Map<String, Object> model) {
    return employeesComponent.returnEmployeeMetricsComponent(model);
  }

  @GetMapping("/employees/create")
  public String returnEmployeeCreate(Map<String, Object> model) throws Exception {
    return employeesComponent.returnEmployeeCreateComponent(model, flag, edit);
  }

  @PostMapping(path = "/employees/create", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String handleEmployeeProfileSubmit(Map<String, Object> model, Employee employee) throws Exception {
    return employeesComponent.handleEmployeeProfileSubmitComponent(model, employee);
  }

  @GetMapping("/employees/edit")
  public String editEmployee(Map<String, Object> model, @RequestParam String rid) throws Exception {
    return employeesComponent.editEmployeeComponent(model, rid);
  }

  @PostMapping(path = "/employees/edit", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }) 
  public String handleEmployeeEditSubmit(Map<String, Object> model, Employee employee, @RequestParam String rid) throws Exception {
	  return employeesComponent.handleEmployeeEditSubmitComponent(model, employee, rid);
  }


  /************ PROJECTS ************/

  @GetMapping("/projects")
  String returnProjectHomepage(Map<String, Object> model) {
    return projectsComponent.returnProjectHomepageComponent(model, flag, edit);
  }
  
  @PostMapping(path = "/projects", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String filterByPropertyProj(Map<String, Object> model, Property prop) {
    return projectsComponent.filterByPropertyProjComponent(model, prop, flag, edit);
  }

  @GetMapping("/projects/create")
  public String returnProjectCreate(Map<String, Object> model) throws Exception {
    return projectsComponent.returnProjectCreateComponent(model, flag, edit);
  }
  
  @PostMapping(path = "/projects/create", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String handleProjectSubmit(Map<String, Object> model,Project project) throws Exception {
    return projectsComponent.handleProjectSubmitComponent(model, project);
  }
  
  @GetMapping("/projects/deleted")
  public String deleteProject(Map<String, Object> model, @RequestParam String p_id) {
	  return projectsComponent.deleteProjectComponent(model, p_id);
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