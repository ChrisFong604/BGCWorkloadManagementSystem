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
public class DashboardComponent {
	
	@Autowired
	private DataSource dataSource;
	
	String workloadComponent(Map<String, Object> model, boolean flag, boolean edit) {
	    try (Connection connection = dataSource.getConnection()) {
	      Statement stmt = connection.createStatement();
	      stmt.executeUpdate(
	          "CREATE TABLE IF NOT EXISTS employees (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
	        		+ "team varchar(40), status boolean, startdate date, enddate date)");
	      stmt.executeUpdate(
	          "CREATE TABLE IF NOT EXISTS employees2 (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
	        		+ "team varchar(40), status boolean, startdate date, enddate date)");

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
	      // System.out.println(start);
	      // System.out.println(end);

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
	      /*
	       * for (int i = 0; i < listOfDatesAll.size()-1; i++) { if (i > 1) {
	       * listOfDates.add(listOfDatesAll.get(i)); } }
	       */
	      for (int i = 0; i < listOfDatesAll.size() - 1; i++) {
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
	        // startDates.add(strsDate);
	        // System.out.println(strsDate);
	        // System.out.println(streDate);
	        boolean isBefore = LocalDate.parse(strsDate).isBefore(startRange);
	        boolean isAfter = LocalDate.parse(strsDate).isAfter(end);
	        if (!isAfter) {
	          empInRange.add(employee);

	          /*** ramp up ***/
	          String position = employee.getPosition();
	          RampUp empRU = new RampUp();
	          String st = "permanent";
	          if (position.equals(st)) {
	            empRU.setWeek1(0.1);
	            empRU.setWeek2(0.25);
	            empRU.setWeek3(0.5);
	            empRU.setWeek4(0.875);
	            empRU.setWeek5(0.875);
	          } else {
	            empRU.setWeek1(0.1);
	            empRU.setWeek2(0.25);
	            empRU.setWeek3(0.4);
	            empRU.setWeek4(0.65);
	            empRU.setWeek5(0.65);
	          }

	          boolean flag1 = false;
	          int week = 0;
	          int wk = 0;
	          for (int i = 0; i < listOfDates.size(); i++) {
	            // check if emp starts <2 weeks before start of range
	            if (!flag1 && LocalDate.parse(strsDate).isBefore(start.minusWeeks(2))) {
	              empRampUp.add(empRU.getWeek4());
	              flag1 = true;
	              week = 4;
	            } else if (!flag1 && LocalDate.parse(strsDate).isBefore(start.minusWeeks(1))) {
	              empRampUp.add(empRU.getWeek3());
	              flag1 = true;
	              week = 3;
	            } else if (!flag1 && LocalDate.parse(strsDate).isBefore(start)) {
	              empRampUp.add(empRU.getWeek2());
	              flag1 = true;
	              week = 2;
	            } else if (flag1 && week >= 1 && week <= 4) {
	              if (LocalDate.parse(streDate).isBefore(listOfDatesAll.get(i)) ||
	              LocalDate.parse(streDate).equals(listOfDatesAll.get(i))) {
	                if (week == 1) {
	                  empRampUp.add(0.0);
	                  week++;
	                } else if (week == 2) {
	                  empRampUp.add(0.0);
	                  week++;
	                } else if (week == 3) {
	                  empRampUp.add(0.0);
	                  week++;
	                } else if (week == 4) {
	                  empRampUp.add(0.0);
	                  week = 0;
	                }
	              } else {
	                if (week == 1) {
	                  empRampUp.add(empRU.getWeek2());
	                  week++;
	                } else if (week == 2) {
	                  empRampUp.add(empRU.getWeek3());
	                  week++;
	                } else if (week == 3) {
	                  empRampUp.add(empRU.getWeek4());
	                  week++;
	                } else if (week == 4) {
	                  empRampUp.add(empRU.getWeek5());
	                  week = 0;
	                }
	              }
	            } else if (!flag1 && (LocalDate.parse(strsDate).equals(listOfDatesAll.get(i)) ||
	                     LocalDate.parse(strsDate).isBefore(listOfDatesAll.get(i+1)))) {
	              //System.out.println(listOfDatesAll.get(i));
	              //System.out.println(listOfDatesAll.get(i+1));
	              empRampUp.add(empRU.getWeek1());
	              flag1 = true;
	              week++;
	            } else if (LocalDate.parse(strsDate).isAfter(listOfDatesAll.get(i)) || 
	                     LocalDate.parse(streDate).isBefore(listOfDatesAll.get(i)) ||
	                     LocalDate.parse(streDate).equals(listOfDatesAll.get(i))) {
	              empRampUp.add(0.0);
	            } else {
	              empRampUp.add(empRU.getWeek5());
	            }
	          }
	          employee.setRampUp(empRampUp);
	        }
	        // System.out.println(empRampUp.size());
	        /*
	         * for (double r : empRampUp) { System.out.println(r); }
	         */
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
	
	String dashboardComponent(Map<String, Object> model, boolean flag, boolean edit) {
		/*
	     * RangeInput range = new RangeInput(); model.put("rangeEmpty", range);
	     */
	    try (Connection connection = dataSource.getConnection()) {
	      Statement stmt = connection.createStatement();
	      
	      stmt.executeUpdate(
	          "CREATE TABLE IF NOT EXISTS employees (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
	        		+ "team varchar(40), status boolean, startdate date, enddate date)");
	      stmt.executeUpdate(
	          "CREATE TABLE IF NOT EXISTS employees2 (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
	        		+ "team varchar(40), status boolean, startdate date, enddate date)");

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
	      // System.out.println(start);
	      // System.out.println(end);

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
	      /*
	       * for (int i = 0; i < listOfDatesAll.size()-1; i++) { if (i > 1) {
	       * listOfDates.add(listOfDatesAll.get(i)); } }
	       */
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
	        // startDates.add(strsDate);
	        // System.out.println(strsDate);
	        // System.out.println(streDate);
	        boolean isBefore = LocalDate.parse(strsDate).isBefore(startRange);
	        boolean isAfter = LocalDate.parse(strsDate).isAfter(end);
	        if (!isBefore && !isAfter) {
	          empInRange.add(employee);

	          /*** ramp up ***/
	          String position = employee.getPosition();
	          RampUp empRU = new RampUp();
	          String st = "permanent";
	          if (position.equals(st)) {
	            empRU.setWeek1(0.1);
	            empRU.setWeek2(0.25);
	            empRU.setWeek3(0.5);
	            empRU.setWeek4(0.875);
	            empRU.setWeek5(0.875);
	          } else {
	            empRU.setWeek1(0.1);
	            empRU.setWeek2(0.25);
	            empRU.setWeek3(0.4);
	            empRU.setWeek4(0.65);
	            empRU.setWeek5(0.65);
	          }

	          boolean flag1 = false;
	          int week = 0;
	          int wk = 0;
	          for (int i = 0; i < listOfDates.size(); i++) {
	            // check if emp starts <2 weeks before start of range
	            if (!flag1 && LocalDate.parse(strsDate).isBefore(start.minusWeeks(1))) {
	              empRampUp.add(empRU.getWeek3());
	              flag1 = true;
	              week = 3;
	            } else if (!flag1 && LocalDate.parse(strsDate).isBefore(start)) {
	              empRampUp.add(empRU.getWeek2());
	              flag1 = true;
	              week = 2;
	            } else if (flag1 && week >= 1 && week <= 4) {
	              if (LocalDate.parse(streDate).isBefore(listOfDatesAll.get(i)) ||
	              LocalDate.parse(streDate).equals(listOfDatesAll.get(i))) {
	                if (week == 1) {
	                  empRampUp.add(0.0);
	                  week++;
	                } else if (week == 2) {
	                  empRampUp.add(0.0);
	                  week++;
	                } else if (week == 3) {
	                  empRampUp.add(0.0);
	                  week++;
	                } else if (week == 4) {
	                  empRampUp.add(0.0);
	                  week = 0;
	                }
	              } else {
	                if (week == 1) {
	                  empRampUp.add(empRU.getWeek2());
	                  week++;
	                } else if (week == 2) {
	                  empRampUp.add(empRU.getWeek3());
	                  week++;
	                } else if (week == 3) {
	                  empRampUp.add(empRU.getWeek4());
	                  week++;
	                } else if (week == 4) {
	                  empRampUp.add(empRU.getWeek5());
	                  week = 0;
	                }
	              }
	            } else if (!flag1 && (LocalDate.parse(strsDate).equals(listOfDatesAll.get(i)) ||
	                     LocalDate.parse(strsDate).isBefore(listOfDatesAll.get(i+1)))) {
	              //System.out.println(listOfDatesAll.get(i));
	              //System.out.println(listOfDatesAll.get(i+1));
	              empRampUp.add(empRU.getWeek1());
	              flag1 = true;
	              week++;
	            } else if (LocalDate.parse(strsDate).isAfter(listOfDatesAll.get(i)) || 
	                     LocalDate.parse(streDate).isBefore(listOfDatesAll.get(i)) ||
	                     LocalDate.parse(streDate).equals(listOfDatesAll.get(i))) {
	              empRampUp.add(0.0);
	            } else {
	              empRampUp.add(empRU.getWeek5());
	            }
	          }
	          employee.setRampUp(empRampUp);
	        }
	        // System.out.println(empRampUp.size());
	        /*
	         * for (double r : empRampUp) { System.out.println(r); }
	         */
	      }
	      model.put("empInRange", empInRange);
	      
	      /*
	       * for (Employee emp : empInRange) { System.out.println(emp.getName()); }
	       */
	      
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
	
	public String getRangeComponent(Map<String, Object> model, RangeInput range) throws Exception {
	    try (Connection connection = dataSource.getConnection()) {
	      Statement stmt = connection.createStatement();
	      
	      stmt.executeUpdate(
	          "CREATE TABLE IF NOT EXISTS employees2 (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
	        	  + "team varchar(40), status boolean, startdate date, enddate date)");
	      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS range (id serial, startdate varchar(20), enddate varchar(20))");
	      String sql = "INSERT INTO range (startdate, enddate) VALUES ('" + range.getStart() + "','" + range.getEnd() + "')";
	      stmt.executeUpdate(sql);
	      
	      return "redirect:/dashboard";
	    } catch (Exception e) {
	      model.put("message", e.getMessage());
	      return "error";
	    }
	}
	
	public String deletetabledataComponent(Map<String, Object> model) throws Exception {
	    try (Connection connection = dataSource.getConnection()) {
	      Statement stmt = connection.createStatement();
	      stmt.executeUpdate("delete from employees");
	      stmt.executeUpdate("delete from employees2");
	      return "redirect:/dashboard";
	    } catch (Exception e) {
	      model.put("message", e.getMessage());
	      return "error";
	    }
	  }

	/*
	String assignEmployeeToProjectComponent(Map<String, Object> model, Employee emp, Project proj, WorksOn workon) throws Exception {
		
	    try (Connection connection = dataSource.getConnection()) {
	      Statement stmt = connection.createStatement();
		  model.put("employee");
	      return "redirect:/dashboard";
	    } catch (Exception e) {
	      model.put("message", e.getMessage());
	      return "error";
	    }
	}
	*/
	
}
