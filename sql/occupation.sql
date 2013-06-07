DROP TABLE IF EXISTS occupations;
CREATE TABLE occupations (
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      domain_id int(11),
      name VARCHAR(56),
      ident VARCHAR(56),
      manual_security_assesment BOOLEAN,     
      integrity int(11),
      integrity_explanation text,
      confidentiality int(11),
      confidentiality_explanation text,
      availability int(11),
      availability_explanation text,
      personal_data Bool
  )DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
