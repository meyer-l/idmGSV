DROP TABLE IF EXISTS employees;
CREATE TABLE employees (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      domain_id int(11),
      name VARCHAR(56),
      surname VARCHAR(56),
      telefon VARCHAR(56)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
