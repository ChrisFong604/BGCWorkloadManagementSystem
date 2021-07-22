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

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
    //SpringApplication.run(createManager.class, args);
  }

  @RequestMapping("/")
  String index(Map<String, Object> model) {
    return "redirect:/login";
  }

  // Change to PostMapping or whatever for login page later
  @GetMapping("/login")
  String loginPageHandler(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS login (id serial, username varchar(20), password varchar(20), access varchar(20))");
      flag = false;
      edit = false;
    
      
      UserLogin user = new UserLogin();
      model.put("user", user);
      return "login";
    }catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @PostMapping(path = "/login", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String login(Map<String, Object> model, UserLogin user) throws Exception {
    String username = user.getUsername();
    String pw = user.getPassword();
    String access = user.getAccess();

    if (username.equals("admin") && pw.equals("123")){
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

  @GetMapping("/dashboard/workload")
  String workload(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS employees (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
              + "team varchar(40), status boolean, capacity float, startdate date, enddate date)");
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS employees2 (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
              + "team varchar(40), status boolean, capacity float, startdate date, enddate date)");

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS range (id serial, startdate varchar(20), enddate varchar(20))");

      
      String sql = "SELECT * FROM employees ORDER BY startdate ASC";
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

      
      Statement stmt3 = connection.createStatement();
      String sql3 = "SELECT * FROM range";
      ResultSet rs3 = stmt.executeQuery(sql3);

      RangeInput output3 = new RangeInput();
      while (rs3.next()) {
        output3.setStart(rs3.getString("startdate"));
        output3.setEnd(rs3.getString("enddate"));
      }
      model.put("range", output3);

      /*** visual ****/ 
      Statement stmt4 = connection.createStatement();
      String sql4 = "SELECT * FROM range";
      ResultSet rs4 = stmt.executeQuery(sql4);

      LocalDate start = LocalDate.now();
      LocalDate end = LocalDate.now();
      while (rs4.next()) {
        start = LocalDate.parse(rs4.getString("startdate"));
        end = LocalDate.parse(rs4.getString("enddate"));
      }
      //System.out.println(start);
      //System.out.println(end);

      /*** setting up the range of dates ***/
      LocalDate startRange = start.minusWeeks(2);
      LocalDate endOfRange = end.plusWeeks(1);
      LocalDate localDate = start;
      ArrayList<LocalDate> listOfDates = new ArrayList<>();
      ArrayList<LocalDate> listOfDatesAll = new ArrayList<>();
      while (localDate.isBefore(endOfRange)) {
        listOfDatesAll.add(localDate);
        localDate = localDate.plusWeeks(1);
      }
      /*for (int i = 0; i < listOfDatesAll.size()-1; i++) {
        if (i > 1) {
          listOfDates.add(listOfDatesAll.get(i));
        }
      }*/
      for (int i = 0; i < listOfDatesAll.size()-1; i++) {
        listOfDates.add(listOfDatesAll.get(i));
      }
      model.put("listOfDates", listOfDates);
    
      /*** getting employees that start within the range ***/
      ArrayList<Employee> empInRange = new ArrayList<>();
      for (Employee employee : output) {
        ArrayList<Double> empRampUp = new ArrayList<>();
    
        Date date = employee.getStart();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strsDate = dateFormat.format(date);  
        Date edate = employee.getEnd();
        String streDate = dateFormat.format(edate);  
        //startDates.add(strsDate);
        //System.out.println(strsDate);
        //System.out.println(streDate);
        boolean isBefore = LocalDate.parse(strsDate).isBefore(startRange);
        boolean isAfter = LocalDate.parse(strsDate).isAfter(end);
        if (!isAfter) {
          empInRange.add(employee);

          /*** ramp up ***/
          String position = employee.getPosition();
          RampUp empRU =  new RampUp();
          String st = "permanent";
          if (position.equals(st)) {
            empRU.setWeek1(0.1);
            empRU.setWeek2(0.25);
            empRU.setWeek3(0.5);
            empRU.setWeek4(0.875);
            empRU.setWeek5(0.875);
          }
          else {
            empRU.setWeek1(0.1);
            empRU.setWeek2(0.25);
            empRU.setWeek3(0.4);
            empRU.setWeek4(0.65);
            empRU.setWeek5(0.65);
          }

          boolean flag = false;
          int week = 0;
          int wk = 0;
          for (int i = 0; i < listOfDates.size(); i++) {
            // check if emp starts <2 weeks before start of range
            if (!flag && LocalDate.parse(strsDate).isBefore(start.minusWeeks(2))) {
              empRampUp.add(empRU.getWeek4());
              flag = true;
              week = 4;
            }
            else if (!flag && LocalDate.parse(strsDate).isBefore(start.minusWeeks(1))) {
              empRampUp.add(empRU.getWeek3());
              flag = true;
              week = 3;
            }
            else if (!flag && LocalDate.parse(strsDate).isBefore(start)) {
              empRampUp.add(empRU.getWeek2());
              flag = true;
              week = 2;
            }
            else if (flag && week >= 1 && week <= 4) {
              if (LocalDate.parse(streDate).isBefore(listOfDatesAll.get(i)) ||
              LocalDate.parse(streDate).equals(listOfDatesAll.get(i))) {
                if (week == 1) {
                  empRampUp.add(0.0);
                  week++;
                }
                else if (week == 2) {
                  empRampUp.add(0.0);
                  week++;
                }
                else if (week == 3) {
                  empRampUp.add(0.0);
                  week++;
                }
                else if (week == 4) {
                  empRampUp.add(0.0);
                  week = 0;
                }
              }
              else {
                if (week == 1) {
                  empRampUp.add(empRU.getWeek2());
                  week++;
                }
                else if (week == 2) {
                  empRampUp.add(empRU.getWeek3());
                  week++;
                }
                else if (week == 3) {
                  empRampUp.add(empRU.getWeek4());
                  week++;
                }
                else if (week == 4) {
                  empRampUp.add(empRU.getWeek5());
                  week = 0;
                }
              }
            }
            else if (!flag && (LocalDate.parse(strsDate).equals(listOfDatesAll.get(i)) ||
                     LocalDate.parse(strsDate).isBefore(listOfDatesAll.get(i+1)))) {
              //System.out.println(listOfDatesAll.get(i));
              //System.out.println(listOfDatesAll.get(i+1));
              empRampUp.add(empRU.getWeek1());
              flag = true;
              week++;
            }
            else if (LocalDate.parse(strsDate).isAfter(listOfDatesAll.get(i)) || 
                     LocalDate.parse(streDate).isBefore(listOfDatesAll.get(i)) ||
                     LocalDate.parse(streDate).equals(listOfDatesAll.get(i))) {
              empRampUp.add(0.0);
            }
            else {
              empRampUp.add(empRU.getWeek5());
            }
          }
          employee.setRampUp(empRampUp);
        }
        //System.out.println(empRampUp.size());
        /*for (double r : empRampUp) {
          System.out.println(r);
        }*/
      }
      model.put("empInRange", empInRange);
      if (flag && edit) {
        return "workload";
      } else if (flag && !edit) {
        return "readOnly/workload_r";
      } else {
        return "userNotFound";
      }
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }


  @GetMapping("/dashboard")
  String dashboard(Map<String, Object> model) {
    /*RangeInput range = new RangeInput();
    model.put("rangeEmpty", range);*/
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS employees (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
              + "team varchar(40), status boolean, capacity float, startdate date, enddate date)");
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS employees2 (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
              + "team varchar(40), status boolean, capacity float, startdate date, enddate date)");

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS range (id serial, startdate varchar(20), enddate varchar(20))");

      String sql = "SELECT * FROM employees ORDER BY startdate ASC";
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
      String sql2 = "SELECT * FROM employees2 ORDER BY startdate ASC";
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

      /*** range ***/
      Statement stmt3 = connection.createStatement();
      String sql3 = "SELECT * FROM range";
      ResultSet rs3 = stmt.executeQuery(sql3);

      RangeInput output3 = new RangeInput();
      while (rs3.next()) {
        output3.setStart(rs3.getString("startdate"));
        output3.setEnd(rs3.getString("enddate"));
      }
      model.put("range", output3);

      /*** visual ****/ 
      Statement stmt4 = connection.createStatement();
      String sql4 = "SELECT * FROM range";
      ResultSet rs4 = stmt.executeQuery(sql4);

      LocalDate start = LocalDate.now();
      LocalDate end = LocalDate.now();
      while (rs4.next()) {
        start = LocalDate.parse(rs4.getString("startdate"));
        end = LocalDate.parse(rs4.getString("enddate"));
      }
      //System.out.println(start);
      //System.out.println(end);

      /*** setting up the range of dates ***/
      LocalDate startRange = start.minusWeeks(2);
      LocalDate endOfRange = end.plusWeeks(1);
      LocalDate localDate = start;
      ArrayList<LocalDate> listOfDates = new ArrayList<>();
      ArrayList<LocalDate> listOfDatesAll = new ArrayList<>();
      while (localDate.isBefore(endOfRange)) {
        listOfDatesAll.add(localDate);
        localDate = localDate.plusWeeks(1);
      }
      /*for (int i = 0; i < listOfDatesAll.size()-1; i++) {
        if (i > 1) {
          listOfDates.add(listOfDatesAll.get(i));
        }
      }*/
      for (int i = 0; i < listOfDatesAll.size()-1; i++) {
        listOfDates.add(listOfDatesAll.get(i));
      }
      model.put("listOfDates", listOfDates);
    
      /*** getting employees that start within the range ***/
      ArrayList<Employee> empInRange = new ArrayList<>();
      for (Employee employee : output) {
        ArrayList<Double> empRampUp = new ArrayList<>();
    
        Date date = employee.getStart();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strsDate = dateFormat.format(date);  
        Date edate = employee.getEnd();
        String streDate = dateFormat.format(edate);  
        //startDates.add(strsDate);
        //System.out.println(strsDate);
        //System.out.println(streDate);
        boolean isBefore = LocalDate.parse(strsDate).isBefore(startRange);
        boolean isAfter = LocalDate.parse(strsDate).isAfter(end);
        if (!isBefore && !isAfter) {
          empInRange.add(employee);

          /*** ramp up ***/
          String position = employee.getPosition();
          RampUp empRU =  new RampUp();
          String st = "permanent";
          if (position.equals(st)) {
            empRU.setWeek1(0.1);
            empRU.setWeek2(0.25);
            empRU.setWeek3(0.5);
            empRU.setWeek4(0.875);
            empRU.setWeek5(0.875);
          }
          else {
            empRU.setWeek1(0.1);
            empRU.setWeek2(0.25);
            empRU.setWeek3(0.4);
            empRU.setWeek4(0.65);
            empRU.setWeek5(0.65);
          }

          boolean flag = false;
          int week = 0;
          int wk = 0;
          for (int i = 0; i < listOfDates.size(); i++) {
            // check if emp starts <2 weeks before start of range
            if (!flag && LocalDate.parse(strsDate).isBefore(start.minusWeeks(1))) {
              empRampUp.add(empRU.getWeek3());
              flag = true;
              week = 3;
            }
            else if (!flag && LocalDate.parse(strsDate).isBefore(start)) {
              empRampUp.add(empRU.getWeek2());
              flag = true;
              week = 2;
            }
            else if (flag && week >= 1 && week <= 4) {
              if (LocalDate.parse(streDate).isBefore(listOfDatesAll.get(i)) ||
              LocalDate.parse(streDate).equals(listOfDatesAll.get(i))) {
                if (week == 1) {
                  empRampUp.add(0.0);
                  week++;
                }
                else if (week == 2) {
                  empRampUp.add(0.0);
                  week++;
                }
                else if (week == 3) {
                  empRampUp.add(0.0);
                  week++;
                }
                else if (week == 4) {
                  empRampUp.add(0.0);
                  week = 0;
                }
              }
              else {
                if (week == 1) {
                  empRampUp.add(empRU.getWeek2());
                  week++;
                }
                else if (week == 2) {
                  empRampUp.add(empRU.getWeek3());
                  week++;
                }
                else if (week == 3) {
                  empRampUp.add(empRU.getWeek4());
                  week++;
                }
                else if (week == 4) {
                  empRampUp.add(empRU.getWeek5());
                  week = 0;
                }
              }
            }
            else if (!flag && (LocalDate.parse(strsDate).equals(listOfDatesAll.get(i)) ||
                     LocalDate.parse(strsDate).isBefore(listOfDatesAll.get(i+1)))) {
              //System.out.println(listOfDatesAll.get(i));
              //System.out.println(listOfDatesAll.get(i+1));
              empRampUp.add(empRU.getWeek1());
              flag = true;
              week++;
            }
            else if (LocalDate.parse(strsDate).isAfter(listOfDatesAll.get(i)) || 
                     LocalDate.parse(streDate).isBefore(listOfDatesAll.get(i)) ||
                     LocalDate.parse(streDate).equals(listOfDatesAll.get(i))) {
              empRampUp.add(0.0);
            }
            else {
              empRampUp.add(empRU.getWeek5());
            }
          }
          employee.setRampUp(empRampUp);
        }
        //System.out.println(empRampUp.size());
        /*for (double r : empRampUp) {
          System.out.println(r);
        }*/
      }
      model.put("empInRange", empInRange);
      
      // print to check
      /*for (Employee emp : empInRange) {
        System.out.println(emp.getName());
      }*/
      
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

  @PostMapping(path = "/dashboard", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String getRange(Map<String, Object> model, RangeInput range) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS employees2 (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
              + "team varchar(40), status boolean, capacity float, startdate date, enddate date)");
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS range (id serial, startdate varchar(20), enddate varchar(20))");
      String sql = "INSERT INTO range (startdate, enddate) VALUES ('" + range.getStart() + "','" + range.getEnd() + "')";
      stmt.executeUpdate(sql);
      
      return "redirect:/dashboard";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @PostMapping(path = "/deletetabledata")
  public String deletetabledata(Map<String, Object> model) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate(
        "delete from employees"
      );
      stmt.executeUpdate(
        "delete from employees2"
      );
      return "redirect:/dashboard";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }


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

  @GetMapping("/projects/create")
  public String returnProjectCreate(Map<String, Object> model) throws Exception {
    Project project = new Project();
    model.put("project", project);
    if (flag && edit) {
      return "projects/createProject";
    } else {
      return "userNotFound";
    }
  }
  @PostMapping(path = "/projects/create", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String handleProjectSubmit(Map<String, Object> model,Project project) throws Exception {
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