DROP TABLE IF EXISTS assets_measures_securityzones_measures;
CREATE TABLE assets_measures_securityzones_measures(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
     assets_measures_id int(11),
     securityzones_measures_id int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
