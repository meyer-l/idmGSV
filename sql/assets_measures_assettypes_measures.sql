DROP TABLE IF EXISTS assets_measures_assettypes_measures;
CREATE TABLE assets_measures_assettypes_measures(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      assettypes_measures_id int(11),
      assets_measures_id int(11),
      measure_id int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
