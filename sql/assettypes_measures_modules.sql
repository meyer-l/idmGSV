DROP TABLE IF EXISTS assettypes_measures_modules;
CREATE TABLE assettypes_measures_modules(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      assettypes_measures_id int(11),
      module_id int(11),
      assettype_id int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
