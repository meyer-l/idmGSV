DROP TABLE IF EXISTS assettypes_modules;
CREATE TABLE assettypes_modules(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      assettype_id int(11),
      module_id int(11),
      responsible_person_id int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
