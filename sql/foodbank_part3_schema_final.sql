-- CS157A | Food Bank Database Project | Part 3: Schema 

DROP DATABASE IF EXISTS foodbank_b;
CREATE DATABASE foodbank_b;
USE foodbank_b;

SET default_storage_engine = INNODB;

CREATE TABLE Donor (
  donor_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  contact_info VARCHAR(200),
  type ENUM('individual','organization') NOT NULL
);

CREATE TABLE Recipient (
  recipient_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  contact_info VARCHAR(200),
  family_size INT NOT NULL CHECK (family_size >= 0),
  status ENUM('active','paused') NOT NULL
);

CREATE TABLE Volunteer (
  volunteer_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  role ENUM('volunteer','staff') NOT NULL,
  shift_date DATE NOT NULL,
  contact_info VARCHAR(200)
);

CREATE TABLE Inventory (
  inventory_id INT AUTO_INCREMENT PRIMARY KEY,
  storage_location VARCHAR(100) NOT NULL,
  category VARCHAR(80) NOT NULL,
  itemValue DECIMAL(10,2) NOT NULL DEFAULT 0.00 CHECK (itemValue >= 0)
);

CREATE TABLE FoodItem (
  inventory_id INT NOT NULL,
  food_item_id INT NOT NULL,
  name VARCHAR(120) NOT NULL,
  quantity INT NOT NULL CHECK (quantity >= 0),
  PRIMARY KEY (inventory_id, food_item_id),
  FOREIGN KEY (inventory_id) REFERENCES Inventory(inventory_id)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Donation (
  donation_id INT AUTO_INCREMENT PRIMARY KEY,
  donor_id INT NOT NULL,
  inventory_id INT NOT NULL,
  date_given DATE NOT NULL,
  amount_added DECIMAL(10,2) NOT NULL CHECK (amount_added >= 0),
  notes TEXT NULL,
  FOREIGN KEY (donor_id) REFERENCES Donor(donor_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (inventory_id) REFERENCES Inventory(inventory_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE Distribution (
  distribution_id INT AUTO_INCREMENT PRIMARY KEY,
  recipient_id INT NOT NULL,
  itemValue DECIMAL(10,2) NOT NULL CHECK (itemValue >= 0),
  city_location VARCHAR(80) NOT NULL,
  FOREIGN KEY (recipient_id) REFERENCES Recipient(recipient_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE Supplies (
  inventory_id INT NOT NULL,
  recipient_id INT NOT NULL,
  PRIMARY KEY (inventory_id, recipient_id),
  FOREIGN KEY (inventory_id) REFERENCES Inventory(inventory_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (recipient_id) REFERENCES Recipient(recipient_id)
    ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Works (
  volunteer_id INT NOT NULL,
  distribution_id INT NOT NULL,
  PRIMARY KEY (volunteer_id, distribution_id),
  FOREIGN KEY (volunteer_id) REFERENCES Volunteer(volunteer_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (distribution_id) REFERENCES Distribution(distribution_id)
    ON UPDATE CASCADE ON DELETE CASCADE
);
