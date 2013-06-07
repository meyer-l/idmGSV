DROP TABLE IF EXISTS assettypes_securityzones;
CREATE TABLE assettypes_securityzones(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      assettype_id int(11),
      securityzone_id int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
