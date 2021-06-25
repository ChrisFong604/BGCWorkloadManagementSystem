<h1>BGC Workload Management System</h1>
<h1> Requirements and Specification Document</h1>

06/25/2021, version 1 (please provide a color code for each iteration so we can track changes)

Project Abstract - BGC Workload Management System

The goal of this project is to develop a web application that manages the planned and actual work capacity of BGC Engineering’s various Software Development teams, and compare them to the planned workload. On our application, users will have access to a headcount graph which will show both a projected and employed headcount for BGC to see how they are doing in terms of hiring. There will also be a working capacity graph which will show work capacity vs workload to determine BGC's hiring needs. Users will be able to create, delete, and edit both project and employee information to adjust the graphs as needed. We are seeking to improve upon the previous implementation of a simple excel document, and create a more organized, intuitive, and less cluttered application.

Customer

The customer is a software development manager within the company BGC Engineering. This software will be used to help track the team members/employees planned and actual work capacity.

Competitive Analysis

Our project will be competing with BGC’s previous product: an excel document. This document is difficult to navigate and often glitchy, so our project will need to be an improvement over this current document. It will need to be more user friendly than the current excel system employed by BGC, allow for more convenient CRUD of information, and better data visualization than its predecessor. Redundant elements of the excel document will be removed for usability (e.g. team, resource, custom, and working days columns).

Scope and Epics

Our project features can be categorized into several main epics:
Employee database and view
Project workload database and view
Login (MS Azure Directory)
Work Capacity vs Work comparison view
Proper Timetable/Calendar view (upcoming projects, employee start dates, etc.)
User Interface

User Stories

Since the only users will be Eldon and his management team, we will create an administrator role that will be able to CRUD the employee and project database, and also view all the statistics and charts created.

Administrator: 
BGC project managers would like to login to our web application to access the headcount view to compare current capacity vs planned hires, and the work capacity views to consider project workloads. They will be able to view, access, edit, and delete all information within the database through the UI. Login will take place using the Microsoft Azure Active Directory API to ensure confidentiality.

User Interface Requirements

The application UI layout can be found here:
https://276group.invisionapp.com/freehand/BGC-WorkLoad-Management-System-PExE8GEcs

Pages

User Login 
Project managers will use their Azure login which will take them to the ‘Headcount Graph’.
Headcount Graph
Displays employee headcount over an undetermined period of time.
Employee Entry Form
Allows users to add employee information. Upon saving, the user will return to  ‘Headcount Graph’.
Current Employees
Displays current employees with a column visualization at the bottom of the screen. Users can add or edit employee information, or return to the ‘Headcount Graph’.
Work Capacity vs Workload Graph
Displays work capacity vs workload graph, will also be able to view and add employees and projects.  
Project List and Timetable
Will be able to search for current projects, as well as edit and add new projects. Will also display a list of projects and a time table. 
Project Entry Form
Allows users to add a project with various attributes such as name, role, team, resource, funding, number of people, etc.


1st Iteration Requirements

For the first iteration, we are looking to implement a user login using the MS Azure Directory API, and create a working database with PostgreSQL for the employee headcount. The application will be initialized using the Spring framework and Maven as the project manager. A barebones front-end with database connection will be the goal. This will include proper display of the employee list and all related information in an intuitive and easy to read format, proper form inputs and setup for individual employees, and lastly some added visual representation of information using Chart.js or another JavaScript graphing library.

Beyond Iteration 1

As it stands, BGC’s current implementation of the Workload Management System is a “large and hard to maintain” excel spreadsheet, as described by the client. We are hoping to improve vastly over the predecessor by implementing a clean, concise, but detailed application to meet the user’s needs of viewing their work capacity in comparison to work load. Thus, the first core feature to be implemented will be the Employee database, where we will keep track of active vs planned hires, their work schedule, work capacity, and their assignments to the current projects. If feasible, additional context such as team, official role/title,and other information may be added. The second core feature to be implemented will be the project workload database, where the active/future projects will be listed. The workload, deadlines, scheduled work sprints, ramp up/work capacity are prioritized, but more information such as funding, unit cost, etc may be included upon the client’s request and time given. 



