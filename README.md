# CS157A Food Bank Database Project – Group D2

This project implements a small food bank information system using MySQL and a Java JDBC console application. The database tracks donors, recipients, volunteers, inventory, donations, distributions, supplies and work assignments. The Java program provides a menu to view, insert, update and delete records and includes additional constraints, one reporting view and one stored procedure.

## Technologies

MySQL Server 8.0.x  
MySQL Connector J 9.5.0  
Java JDK 22 (compatible with JDK 17 or higher)  
IntelliJ IDEA (used for development and running the application)

## Files Overview

sql/foodbank_part3_schema_final.sql creates the foodbank_b database and all base tables and constraints :contentReference[oaicite:0]{index=0}  
sql/foodbank_part3_seed_final.sql inserts initial sample data into all tables :contentReference[oaicite:1]{index=1}  
sql/constraint + view + stored procedure.sql adds extra constraints, creates the view vw_active_recipient_summary and the stored procedure sp_add_active_distribution :contentReference[oaicite:2]{index=2}  
sql/Testing.sql shows example SQL commands to demonstrate constraints, the view and the stored procedure in action :contentReference[oaicite:3]{index=3}  
java/FoodBankDatabaseApplication.java is the main Java console program that connects to MySQL using JDBC and implements the menus and operations  
app.properties stores the database connection information (url, user, password) and is kept local, not pushed to GitHub  

## Instructions for Building the Database

1. Start MySQL Server on your machine
2. Open MySQL Workbench and connect as a user that can create databases (for example root)
3. Open sql/foodbank_part3_schema_final.sql and execute it. This script drops any existing foodbank_b database, recreates it and creates the tables Donor, Recipient, Volunteer, Inventory, FoodItem, Donation, Distribution, Supplies and Works with primary keys, foreign keys and basic checks
4. Open sql/foodbank_part3_seed_final.sql and execute it. This populates all tables with sample data for donors, recipients, volunteers, inventory items, food items, donations, distributions and relationships
5. Open sql/constraint + view + stored procedure.sql and execute it. This adds extra constraints on contact_info fields, creates the reporting view vw_active_recipient_summary and creates the stored procedure sp_add_active_distribution that only inserts a distribution for recipients who are active
6. Optionally open sql/Testing.sql and run the statements in order to see example failures for invalid email and duplicate donor, to view vw_active_recipient_summary and to test the stored procedure with valid and invalid recipient ids

## Configuring the Java Application (app.properties)

The application reads database settings from app.properties in the project root directory

Create a file named app.properties in the top level of the project (same level as java and sql folders) with contents similar to:

db.url=jdbc:mysql://localhost:3306/foodbank_b  
db.user=root  
db.password=YOUR_PASSWORD_HERE  

Replace YOUR_PASSWORD_HERE with your MySQL password. 

## Running the Java Program

### Using IntelliJ IDEA

1. Open the project folder CS157AProjectGroupD2 in IntelliJ.
2. Add the MySQL Connector J jar as a library if it is not already added. Go to File > Project Structure > Libraries, click the plus button, choose Java and select the mysql connector jar file.
3. Make sure your Project SDK is set to a valid JDK (for example JDK 22).
4. Confirm that app.properties exists in the project root and has the correct url, user and password.
5. In the Project view open java/FoodBankDatabaseApplication.java.
6. Right click the file and choose Run FoodBankDatabaseApplication (or use the green Run button at the top right).
7. The console window will show the main menu for the application.

### Example Main Menu

The main menu looks similar to:

1. View data  
2. Insert data  
3. Update data  
4. Delete data  
5. Run transaction  
6. Exit  

The View menu allows listing rows from tables such as Distribution, Donation, Donor, FoodItem, Inventory, Recipient and Volunteer. The Insert, Update and Delete menus use prepared statements and prompt the user in the console. Recipient and donor prompts clearly say contact email to match the email based constraints.

## How the Application Was Built (Step by Step)

1. Designed the relational schema for the food bank including donors, recipients, volunteers, inventory and transactions and implemented it in foodbank_part3_schema_final.sql with primary keys, foreign keys and simple checks. :contentReference[oaicite:4]{index=4}  
2. Created realistic seed data for all tables in foodbank_part3_seed_final.sql so that views, joins and the application menus have meaningful output. :contentReference[oaicite:5]{index=5}  
3. Added additional constraints and business logic in constraint + view + stored procedure.sql. This includes email style checks on contact_info, a unique constraint on donor name plus contact_info, the view vw_active_recipient_summary that aggregates distribution values per active recipient and the stored procedure sp_add_active_distribution that enforces active recipient status when inserting distributions. :contentReference[oaicite:6]{index=6}  
4. Wrote the Java console program FoodBankDatabaseApplication.java that:
   - Loads database settings from app.properties using java.util.Properties.
   - Obtains a JDBC connection using the MySQL Connector J driver.
   - Implements menus for viewing, inserting, updating and deleting data using PreparedStatement.
   - Includes input validation for numeric fields and email input so user entries match the database constraints.
   - Provides an update path for recipient contact email so that contact_info can be corrected while still respecting the email check constraint.
5. Created Testing.sql with organized examples that demonstrate constraint failures, correct behavior of the view and both successful and failing calls to the stored procedure. This script is used when recording the demo video and when manually testing the database layer. :contentReference[oaicite:7]{index=7}  

## console Screenshots 

You can capture screenshots and place them in a screenshots folder, then link them here.

Example:

Main menu screenshot  
<img width="503" height="381" alt="image" src="https://github.com/user-attachments/assets/57b0e5da-e7f4-4428-9cfc-6f5087e6f456" />


Example of viewing recipients in the console  
<img width="1208" height="993" alt="image" src="https://github.com/user-attachments/assets/8ab4a8c1-1da7-4581-a29a-13dd49aa4da7" />


error handing known recipent update
<img width="827" height="460" alt="image" src="https://github.com/user-attachments/assets/1ee75baf-4e1a-40bf-adac-952ad56734a5" />

