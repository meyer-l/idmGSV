DROP TABLE IF EXISTS assettypes_measures;
CREATE TABLE assettypes_measures(
      id  int(11) NOT NULL  auto_increment PRIMARY KEY,
      assettype_id int(11),
      measure_id int(11),
      responsible_person_id int(11),
      status VARCHAR(255),
      cost VARCHAR(255),
      pass_on BOOLEAN,
      manual_add BOOLEAN
  );
