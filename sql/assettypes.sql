DROP TABLE IF EXISTS assettypes;
CREATE TABLE assettypes (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      domain_id int(11),
      name VARCHAR(255),
      assettypecategory_id int(11),
      location VARCHAR(255),
      description text,
      it_asset BOOLEAN,
      architecture VARCHAR(255),
      status VARCHAR(255),
      propagate_security_assesment BOOLEAN,
      manual_security_assesment BOOLEAN,     
      integrity int(11),
      integrity_explanation text,
      confidentiality int(11),
      confidentiality_explanation text,
      availability int(11),
      availability_explanation text,
      personal_data BOOL,
      responsible_person_id int(11),
      icon_name varchar(255)
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
