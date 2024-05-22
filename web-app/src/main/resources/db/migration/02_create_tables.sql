CREATE TABLE IF NOT EXISTS users (
  user_id BIGINT NOT NULL AUTO_INCREMENT,
  user_name VARCHAR(100) NOT NULL,
  address VARCHAR(255) NULL,
  phone VARCHAR(50) NULL,
  update_date DATETIME NOT NULL,
  create_date DATETIME NOT NULL,
  delete_date DATETIME NULL,
  PRIMARY KEY (user_id)
);
