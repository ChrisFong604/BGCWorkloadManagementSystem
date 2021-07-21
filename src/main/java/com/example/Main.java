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
        if (/*!isBefore &&*/ !isAfter) {
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

  // deleting managers
  @GetMapping("/manager/deleted")
  public String deleteManager(Map<String, Object> model, @RequestParam String m_id) {
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

  // filter by attributes
  @GetMapping("/employees")
  String returnEmployeeHomepage(Map<String, Object> model) {
    Property prop = new Property();
    model.put("property", prop);

    try (Connection connection = dataSource.getConnection()) {
      
      Statement stmt = connection.createStatement();
      
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS employees (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
              + "team varchar(40), status boolean, capacity float, startdate date, enddate date)");
      stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS employees2 (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
              + "team varchar(40), status boolean, capacity float, startdate date, enddate date)");
      String sql = "SELECT * FROM employees ORDER BY startdate ASC";
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
      }
      model.put("empInRange", empInRange);

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
      String sql2 = "DELETE FROM employees2 WHERE id =?";
      PreparedStatement ps = connection.prepareStatement(sql);
      PreparedStatement ps2 = connection.prepareStatement(sql2);
      ps.setString(1, e_id);
      ps2.setString(1, e_id);
      ps.executeUpdate();
      ps2.executeUpdate();

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

      String sql2 = "UPDATE employees2 SET " + "startdate = '" + employee.getStart() + "', "
          + "enddate = '" + employee.getEnd() + "' " + "WHERE id = '" + employee.getId() + "';";
      
      /*
       * String sql =
       * "INSERT INTO employees (id, name, position, role, team, status, capacity, startdate, enddate) VALUES ('"
       * + UniqueID + "','" + employee.getName() + "','" + employee.getPosition() +
       * "','" + employee.getRole() + "','" + employee.getTeam() + "'," +
       * employee.getStatus() + "," + 0.1 + ",'" + employee.getStart() + "','" +
       * employee.getEnd() + "')";
       */

      stmt.executeUpdate(sql);
      stmt.executeUpdate(sql2);
      return "redirect:/employees"; // Directly returns to employee homepage
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
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