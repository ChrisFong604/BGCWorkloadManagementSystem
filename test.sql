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
  name varchar(50),
  projectstart date,
  projectend date,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS worksOn(
  emp_id BIGSERIAL NOT NULL,
  proj_id BIGSERIAL NOT NULL,
  emp_name varchar(40),
  proj_name varchar(40),
  emp_start date,
  emp_end date,
  CONSTRAINT worksOn_pk PRIMARY KEY (emp_id, proj_id),
  FOREIGN KEY (emp_id) REFERENCES employees(id) ON UPDATE CASCADE,
  FOREIGN KEY (proj_id) REFERENCES projects(id) ON UPDATE CASCADE
);

INSERT INTO employees ( name, position, role, team, status, startdate, enddate) VALUES ( 
    'Kevin', 'Co-op/Intern', 'FE Developer', 'Orb1s', TRUE, '2020-12-07', '2021-03-15');
INSERT INTO employees ( name, position, role, team, status, startdate, enddate) VALUES ( 
    'Chris', 'Co-op/Intern', 'BE Developer', 'Bucks', TRUE, '2020-12-21', '2021-01-25');
INSERT INTO projects ( name, projectstart, projectend ) VALUES ( 'project A', '2020-07-09', '2021-01-07');


/*
SELECT name FROM employees where id=${id};
SELECT name FROM projects where id=${id};

INSERT INTO worksOn ( emp_id, proj_id, emp_name, proj_name, workperiods WeeklyCapacity ) 
VALUES ( ${id}, ${id}, 
(SELECT name FROM employees WHERE id=${id}),
(SELECT name FROM projects WHERE id=${id}),
WeeklyCapacity here!!! 
);
*/

INSERT INTO worksOn ( emp_id, proj_id, emp_name, proj_name, emp_start, emp_end ) 
VALUES ( (SELECT id FROM employees WHERE name='Chris'),
(SELECT id FROM projects WHERE name='project A'),
(SELECT position FROM employees WHERE name='Chris'),
(SELECT name FROM projects WHERE name='project A'),
'2017-12-04', '2020-05-21');

SELECT * FROM employees;
SELECT * from projects;
SELECT * from worksOn;

/*
\i ./test.sql

\i ./reset.sql
*/