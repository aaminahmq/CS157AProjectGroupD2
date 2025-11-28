# CS157AProjectGroupD2

CS157A Food Bank Database Project Group D2

This project implements a food bank information system using MySQL and Java JDBC. The database stores donors, recipients, volunteers, inventory, donations, supplies and distributions. The Java console program allows viewing, inserting, updating and deleting records. Additional constraints, one view and one stored procedure were added as part of Phase C.

Technologies used
MySQL 8 or newer
JDK 22 (compatible with JDK 17 or higher)
MySQL Connector J (example version 9.5.0)
IntelliJ IDEA for development

Project files
java/FoodBankDatabaseApplication.java contains the main application
sql/foodbank_part3_schema_final.sql creates all tables and base constraints
sql/foodbank_part3_seed_final.sql inserts sample data into all tables
sql/constraint + view + stored procedure.sql adds more constraints, one view and one stored procedure
sql/Testing.sql demonstrates how to test the project
app.properties stores database login credentials locally and should not be pushed to GitHub

How to build the database
Open MySQL Workbench
Run foodbank_part3_schema_final.sql to create the foodbank_b database and all tables
Run foodbank_part3_seed_final.sql to insert seed data
Run constraint + view + stored procedure.sql to add additional constraints, create the view and create the stored procedure
(Optional) Run Testing.sql to test constraints, the view and the stored procedure

How to configure the Java program
Create a file named app.properties in the project root folder (same level as src and sql folders)
Add your MySQL login information
db.url=jdbc:mysql://localhost:3306/foodbank_b
db.user=root
db.password=YOUR_PASSWORD
This file must stay local and should not be committed to GitHub

How to add MySQL connector
Download MySQL Connector J from the official MySQL website
Extract the zip and locate the jar file
In IntelliJ go to File > Project Structure > Libraries > Add Java Library
Select the jar file and apply
This allows the Java program to connect to MySQL

How to run the program
Open FoodBankDatabaseApplication.java in IntelliJ
Make sure app.properties is configured correctly
Run the file
A menu will appear with operations on the database

Program menu features
View data for donors, recipients, volunteers, inventory, food items, donations and distributions
Insert new records
Update existing values
Delete records
Transaction menu logic supports commit and rollback behavior

Stored procedure behavior summary
The stored procedure inserts distributions only if the recipient is active
If the recipient is paused or does not exist the insert will not happen

Testing
Use Testing.sql to show constraint violations
Test insert behavior with valid and invalid emails
Test active vs inactive recipients to show stored procedure logic
Test view output after inserting distribution records
