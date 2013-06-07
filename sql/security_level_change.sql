DROP TABLE IF EXISTS security_level_changes;
CREATE TABLE security_level_changes (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      asset_id int(11),
      domain_id int(11),
      reviewed boolean,
      old_security_assesment int(11),
      old_availability int(11),
      old_confidentiality int(11),
      old_integrity int(11)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
