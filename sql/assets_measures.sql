DROP TABLE IF EXISTS assets_measures;
CREATE TABLE assets_measures(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      asset_id int(11),
      measure_id int(11),
      responsible_person_id int(11),
      status VARCHAR(255),
      cost VARCHAR(255),
      pass_on BOOLEAN
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
