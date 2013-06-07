DROP TABLE IF EXISTS information_employees;
CREATE TABLE information_employees(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      employee_id int(11),
      information_id int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
