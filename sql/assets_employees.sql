DROP TABLE IF EXISTS assets_employees;
CREATE TABLE assets_employees(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      asset_id int(11),
      employee_id int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
