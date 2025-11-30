-- ==========================================================
-- CS157A | Food Bank Database Project 
-- Complete setup: create DB, tables, seed data, constraints,
-- a reporting VIEW, and a stored procedure.
-- ==========================================================

-- Safety: drop and recreate the database so grader starts clean
DROP DATABASE IF EXISTS foodbank_b;
CREATE DATABASE foodbank_b;
USE foodbank_b;

-- Make sure we use InnoDB so that FK constraints + transactions work
SET default_storage_engine = INNODB;

-- ==========================================================
-- BASE TABLES
-- ==========================================================

-- Donor: people/organizations that donate to the food bank
CREATE TABLE Donor (
  donor_id INT AUTO_INCREMENT PRIMARY KEY,        -- surrogate key
  name VARCHAR(100) NOT NULL,                     -- donor name
  contact_info VARCHAR(200),                      -- typically an email or phone
  type ENUM('individual','organization') NOT NULL -- whether a person or an org
);

-- Recipient: families/households that receive help
CREATE TABLE Recipient (
  recipient_id INT AUTO_INCREMENT PRIMARY KEY,         -- surrogate key
  name VARCHAR(100) NOT NULL,                          -- family name/label
  contact_info VARCHAR(200),                           -- contact (email)
  family_size INT NOT NULL CHECK (family_size >= 0),   -- # of people in household
  status ENUM('active','paused') NOT NULL              -- paused recipients shouldn't get new distributions
);

-- Volunteer: volunteers and staff who help with distributions
CREATE TABLE Volunteer (
  volunteer_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  role ENUM('volunteer','staff') NOT NULL, -- simple role flag
  shift_date DATE NOT NULL,                -- when they are scheduled
  contact_info VARCHAR(200)
);

-- Inventory: storage locations (e.g., warehouse, fridge)
-- and total dollar value of food at that location
CREATE TABLE Inventory (
  inventory_id INT AUTO_INCREMENT PRIMARY KEY,
  storage_location VARCHAR(100) NOT NULL,         -- e.g., 'Main Warehouse A'
  category VARCHAR(80) NOT NULL,                  -- e.g., 'Canned', 'Dairy'
  itemValue DECIMAL(10,2) NOT NULL DEFAULT 0.00   -- overall $ value in that location
    CHECK (itemValue >= 0)
);

-- FoodItem: specific items stored in an Inventory location
-- Weak-style key: (inventory_id, food_item_id) is the PK
CREATE TABLE FoodItem (
  inventory_id INT NOT NULL,                          -- parent inventory
  food_item_id INT NOT NULL,                          -- item ID within that inventory
  name VARCHAR(120) NOT NULL,                         -- name of the food item
  quantity INT NOT NULL CHECK (quantity >= 0),        -- # of units on hand
  PRIMARY KEY (inventory_id, food_item_id),
  FOREIGN KEY (inventory_id) REFERENCES Inventory(inventory_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE                                 -- delete food items if inventory is removed
);

-- Donation: records a donation from a Donor to an Inventory
-- amount_added is tracked in dollar value
CREATE TABLE Donation (
  donation_id INT AUTO_INCREMENT PRIMARY KEY,
  donor_id INT NOT NULL,                              -- FK to Donor
  inventory_id INT NOT NULL,                          -- which inventory got restocked
  date_given DATE NOT NULL,
  amount_added DECIMAL(10,2) NOT NULL
    CHECK (amount_added >= 0),                        -- non-negative $ total
  notes TEXT NULL,                                    -- optional free text
  FOREIGN KEY (donor_id) REFERENCES Donor(donor_id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,                               -- cannot delete donor if donations exist
  FOREIGN KEY (inventory_id) REFERENCES Inventory(inventory_id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT                                -- cannot delete inventory if donations exist
);

-- Distribution: one distribution event per row (total $ value, not per item)
CREATE TABLE Distribution (
  distribution_id INT AUTO_INCREMENT PRIMARY KEY,
  recipient_id INT NOT NULL,                          -- who received the distribution
  itemValue DECIMAL(10,2) NOT NULL
    CHECK (itemValue >= 0),                           -- $ value of items delivered
  city_location VARCHAR(80) NOT NULL,                 -- city where distribution happened
  FOREIGN KEY (recipient_id) REFERENCES Recipient(recipient_id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);

-- Supplies: many-to-many link between Inventory and Recipient
-- Indicates which inventories are allowed to supply which recipients
CREATE TABLE Supplies (
  inventory_id INT NOT NULL,
  recipient_id INT NOT NULL,
  PRIMARY KEY (inventory_id, recipient_id),
  FOREIGN KEY (inventory_id) REFERENCES Inventory(inventory_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  FOREIGN KEY (recipient_id) REFERENCES Recipient(recipient_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

-- Works: many-to-many link showing which volunteers handled which distributions
CREATE TABLE Works (
  volunteer_id INT NOT NULL,
  distribution_id INT NOT NULL,
  PRIMARY KEY (volunteer_id, distribution_id),
  FOREIGN KEY (volunteer_id) REFERENCES Volunteer(volunteer_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  FOREIGN KEY (distribution_id) REFERENCES Distribution(distribution_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

-- ==========================================================
-- SEED DATA (initial rows for testing/demo)
-- ==========================================================

USE foodbank_b;

-- Donors: mixture of organization and individual
INSERT INTO Donor(name, contact_info, type) VALUES
('Bay Fresh Foods','bayfresh@example.org','organization'),
('Alice Chen','alice.chen@example.com','individual'),
('Community Harvest','contact@harvest.org','organization');

-- Recipients: 2 active families, 1 paused family
INSERT INTO Recipient(name, contact_info, family_size, status) VALUES
('Gonzalez Family','gonzalez@example.com',5,'active'),
('Nguyen Family','nguyen@example.com',3,'active'),
('Patel Family','patel@example.com',4,'paused');

-- Volunteers and staff with shift dates
INSERT INTO Volunteer(name, role, shift_date, contact_info) VALUES
('Marco Lopez','volunteer','2025-10-20','marco@sjsu.edu'),
('Aaminah Mohammad','staff','2025-10-20','aaminah@sjsu.edu'),
('Aaron Mundanilkunathi','volunteer','2025-10-21','aaron@sjsu.edu');

-- Inventory locations with total $ value of food
INSERT INTO Inventory(storage_location, category, itemValue) VALUES
('Main Warehouse A','Canned',1200.00),
('Fridge B','Dairy',800.00);

-- Food items in each inventory (note composite PK inventory_id + food_item_id)
INSERT INTO FoodItem(inventory_id, food_item_id, name, quantity) VALUES
(1,1,'Canned Beans',300),
(1,2,'Canned Soup',150),
(1,3,'Canned Corn',50),
(2,1,'Milk Gallon',40),
(2,2,'Yogurt 6-pack',60);

-- Donations from donors to specific inventory locations
INSERT INTO Donation(donor_id, inventory_id, date_given, amount_added, notes) VALUES
(1,1,'2025-10-18',600.00,'Monthly pallet delivery (canned)'),
(1,2,'2025-10-18',200.00,'Monthly pallet delivery (refrigerated)'),
(2,1,'2025-10-19',250.00,'Private donation'),
(3,2,'2025-10-20',300.00,'Refrigerated goods');

-- Supplies mapping: which inventories can supply which families
INSERT INTO Supplies(inventory_id, recipient_id) VALUES
(1,1),(1,2),(2,1),(2,2);

-- Distributions: total $ value delivered to each active family
INSERT INTO Distribution(recipient_id, itemValue, city_location) VALUES
(1,75.00,'San Jose'),
(2,55.00,'Santa Clara');

-- Works: which volunteers helped which distributions
INSERT INTO Works(volunteer_id, distribution_id) VALUES
(1,1),(2,1),(1,2),(3,2);

-- ==========================================================
-- EXTRA CONSTRAINTS (Part 6 requirement)
-- ==========================================================

-- Donor: 
--  1) contact_info should look like an email (must contain '@')
--  2) combination (name, contact_info) must be unique
ALTER TABLE Donor
  ADD CONSTRAINT chk_donor_email
    CHECK (contact_info LIKE '%@%'),
  ADD CONSTRAINT uq_donor_name_email
    UNIQUE (name, contact_info);

-- Recipient: simple email-style check on contact_info
ALTER TABLE Recipient
  ADD CONSTRAINT chk_recipient_email
    CHECK (contact_info LIKE '%@%');

-- ==========================================================
-- VIEW FOR REPORTING
-- vw_active_recipient_summary:
--   Shows only ACTIVE recipients and the total distribution
--   value they have received (in dollars).
--   If a family has no distributions yet, total_distributed_value = 0.
-- ==========================================================

CREATE OR REPLACE VIEW vw_active_recipient_summary AS
SELECT
    r.recipient_id,
    r.name,
    r.family_size,
    r.status,
    COALESCE(SUM(d.itemValue), 0) AS total_distributed_value
FROM Recipient r
LEFT JOIN Distribution d
    ON r.recipient_id = d.recipient_id
WHERE r.status = 'active'
GROUP BY
    r.recipient_id,
    r.name,
    r.family_size,
    r.status;

-- ==========================================================
-- STORED PROCEDURE
-- sp_add_active_distribution:
--   Safely insert a new Distribution row, but ONLY if:
--     • the recipient exists, and
--     • the recipient is currently 'active'.
--   Otherwise, raise an error using SIGNAL.
-- Usage example:
--   CALL sp_add_active_distribution(1, 25.00, 'San Jose');
-- ==========================================================

DELIMITER $$

CREATE PROCEDURE sp_add_active_distribution(
    IN p_recipient_id INT,
    IN p_itemValue DECIMAL(10,2),
    IN p_city VARCHAR(80)
)
BEGIN
    DECLARE v_status VARCHAR(10);

    -- Look up the recipient's status into v_status
    SELECT status INTO v_status
    FROM Recipient
    WHERE recipient_id = p_recipient_id;

    -- If no such recipient was found, v_status stays NULL
    IF v_status IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Recipient does not exist';

    -- If recipient exists but is not active, block the insert
    ELSEIF v_status <> 'active' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Recipient is not active';

    -- Otherwise, recipient is active: OK to insert a new distribution row
    ELSE
        INSERT INTO Distribution(recipient_id, itemValue, city_location)
        VALUES (p_recipient_id, p_itemValue, p_city);
    END IF;
END$$

DELIMITER ;

-- End of script
