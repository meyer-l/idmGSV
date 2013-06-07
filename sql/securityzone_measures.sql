DROP TABLE IF EXISTS securityzones_measures;
CREATE TABLE securityzones_measures(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      securityzone_id int(11),
      measure_id int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
