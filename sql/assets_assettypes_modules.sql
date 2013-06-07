DROP TABLE IF EXISTS assets_assettypes_modules;
CREATE TABLE assets_assettypes_modules(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      assettypes_modules_id int(11),
      asset_id int(11),
      responsible_person_id int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
