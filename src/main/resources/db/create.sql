SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS restaurants (
  id int PRIMARY KEY auto_increment,
  name VARCHAR,
  cuisineId int
  );

  CREATE TABLE IF NOT EXISTS cuisines (
  id int PRIMARY KEY auto_increment,
  type VARCHAR
  );