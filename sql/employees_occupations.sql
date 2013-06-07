DROP TABLE IF EXISTS employees_occupations;
CREATE TABLE employees_occupations(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      employee_id int(11),
      occupation_id int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
