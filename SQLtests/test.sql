CREATE TABLE IF NOT EXISTS employees (
  id BIGSERIAL NOT NULL UNIQUE,
  name varchar(40),
  position varchar(20),
  role varchar(40),
  team varchar(40),
  status boolean,
  startdate date,
  enddate date,
  Primary Key(id)
);

CREATE TABLE IF NOT EXISTS projects (
  id BIGSERIAL NOT NULL UNIQUE,
  Name varchar(50),
  projectstart date,
  projectend date,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS worksIn(
  emp_id BIGSERIAL NOT NULL,
  proj_id BIGSERIAL NOT NULL,
  emp_start date,
  emp_end date,
  PRIMARY KEY (emp_id, proj_id),
  FOREIGN KEY (emp_id) REFERENCES employees(id) ON UPDATE CASCADE,
  FOREIGN KEY (proj_id) REFERENCES projects(id) ON UPDATE CASCADE
);

INSERT INTO employees ( name, position, role, team, status, startdate, enddate) VALUES ( 
    'kevin', 'Co-op/Intern', 'FE Developer', 'Orb1s', TRUE, '2020-12-07', '2021-03-15');



/*
\i /Users/Chris/Desktop/CMPT276/BGCWorkloadManagementSystem/SQLtests/test.sql

\i /Users/Chris/Desktop/CMPT276/BGCWorkloadManagementSystem/SQLtests/reset.sql
*/