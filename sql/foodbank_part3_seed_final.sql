-- CS157A | Food Bank Database Project | Part 3: Seed Data
USE foodbank_b;

INSERT INTO Donor(name, contact_info, type) VALUES
('Bay Fresh Foods','bayfresh@example.org','organization'),
('Alice Chen','alice.chen@example.com','individual'),
('Community Harvest','contact@harvest.org','organization');

INSERT INTO Recipient(name, contact_info, family_size, status) VALUES
('Gonzalez Family','gonzalez@example.com',5,'active'),
('Nguyen Family','nguyen@example.com',3,'active'),
('Patel Family','patel@example.com',4,'paused');

INSERT INTO Volunteer(name, role, shift_date, contact_info) VALUES
('Marco Lopez','volunteer','2025-10-20','marco@sjsu.edu'),
('Aaminah Mohammad','staff','2025-10-20','aaminah@sjsu.edu'),
('Aaron Mundanilkunathi','volunteer','2025-10-21','aaron@sjsu.edu');

INSERT INTO Inventory(storage_location, category, itemValue) VALUES
('Main Warehouse A','Canned',1200.00),
('Fridge B','Dairy',800.00);

INSERT INTO FoodItem(inventory_id, food_item_id, name, quantity) VALUES
(1,1,'Canned Beans',300),
(1,2,'Canned Soup',150),
(1,3,'Canned Corn',50),
(2,1,'Milk Gallon',40),
(2,2,'Yogurt 6-pack',60);

INSERT INTO Donation(donor_id, inventory_id, date_given, amount_added, notes) VALUES
(1,1,'2025-10-18',600.00,'Monthly pallet delivery (canned)'),
(1,2,'2025-10-18',200.00,'Monthly pallet delivery (refrigerated)'),
(2,1,'2025-10-19',250.00,'Private donation'),
(3,2,'2025-10-20',300.00,'Refrigerated goods');

INSERT INTO Supplies(inventory_id, recipient_id) VALUES
(1,1),(1,2),(2,1),(2,2);

INSERT INTO Distribution(recipient_id, itemValue, city_location) VALUES
(1,75.00,'San Jose'),
(2,55.00,'Santa Clara');

INSERT INTO Works(volunteer_id, distribution_id) VALUES
(1,1),(2,1),(1,2),(3,2);
