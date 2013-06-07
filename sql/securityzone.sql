DROP TABLE IF EXISTS securityzones;
CREATE TABLE securityzones (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      name VARCHAR(255),
      order_number int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
