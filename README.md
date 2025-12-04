# CS157A Food Bank Database Project – Group D2

This project implements a small food bank information system using MySQL and a Java JDBC console application. The database tracks donors, recipients, volunteers, inventory, donations, distributions, supplies and work assignments. The Java program provides a menu to view, insert, update and delete records and includes additional constraints, one reporting view and one stored procedure.

## Technologies

MySQL Server 8.0.x  
MySQL Connector J 9.5.0  
Java JDK 22 (compatible with JDK 17 or higher)  
IntelliJ IDEA (used for development and running the application)

## Files Overview

create_populate.sql contains the full database setup including schema creation, constraints, view creation, stored procedure creation and seed data
java/FoodBankDatabaseApplication.java contains the main Java console program and implements all menu options
app.properties contains database connection credentials, which should be the credentials on your local device

## Instructions for Building the Database

1. Start MySQL Server  
2. Open MySQL Workbench and connect using a user who can create databases  
3. Open create_populate.sql
4. Execute the script. This will drop any existing foodbank_b database, recreate it, create all tables, apply constraints, create the view vw_active_recipient_summary, create the stored procedure sp_add_active_distribution and insert all sample data
5. Confirm the tables and data were created by running simple queries such as SELECT * FROM Donor or SELECT * FROM Recipient

## Configuring the Java Application (app.properties)

The application reads database settings from app.properties in the project root directory

Create a file named app.properties in the top level of the project (same level as java and sql folders) with contents similar to:

db.url=jdbc:mysql://localhost:3306/foodbank_b  
db.user=root  
db.password=YOUR_PASSWORD_HERE  

Replace YOUR_PASSWORD_HERE with your MySQL password

## Running the Java Program

### Using IntelliJ IDEA

1. Open the project folder CS157AProjectGroupD2 in IntelliJ
2. Add the MySQL Connector J jar as a library if it is not already added. Go to File > Project Structure > Libraries, click the plus button, choose Java and select the mysql connector jar file
3. Make sure your Project SDK is set to a valid JDK (for example JDK 22)
4. Confirm that app.properties exists in the project root and has the correct url, user and password
5. In the Project view open java/FoodBankDatabaseApplication.java
6. Right click the file and choose Run FoodBankDatabaseApplication (or use the green Run button at the top right)
7. The console window will show the main menu for the application

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

1. Designed the relational schema for the food bank including donors, recipients, volunteers, inventory items and distribution records  
2. Combined all SQL scripts into create_populate.sql containing schema creation, constraints, view setup, stored procedure creation and sample data 
3. Wrote the Java console program to connect to MySQL using JDBC with settings read from app.properties
4. Implemented menu driven operations for selecting, inserting, updating and deleting records
5. Added input validation including contact email format checks
6. Tested the program with various data scenarios to match Phase C requirements including constraints, view behavior and stored procedure behavior

## Input Validation & Error Handling (Phase C Requirement)

The application includes full input validation and SQL exception handling.

1. Invalid numeric input
```
Enter Recipient ID:
abc
Invalid recipient ID.
```

2. Negative value rejected
```
Enter distribution value (>= 0): -10
Value must be non-negative.
```

3. Invalid donor type
```
Enter Type of Donor: friend
Invalid donor type. Must be 'individual' or 'organization'.
```

4. Invalid email format
```
Enter new contact email: 4081234567
Invalid email format. Please enter a valid email address with '@'.
```

5. SQL constraint violation caught
```
SQL Error during operation: Cannot delete or update a parent row...
```

6. Transaction rollback — insufficient quantity
```
Not enough quantity in inventory.
Transaction rolled back.
```

7. Transaction rollback — paused recipient
```
Recipient is not active. Rolling back.
```

These validations ensure no invalid data enters the database and all SQL errors are safely handled.
 

## console Screenshots 

You can capture screenshots and place them in a screenshots folder, then link them here.

Example:

Main menu screenshot  
<img width="503" height="381" alt="image" src="https://github.com/user-attachments/assets/57b0e5da-e7f4-4428-9cfc-6f5087e6f456" />


Example of viewing recipients in the console  
<img width="1208" height="993" alt="image" src="https://github.com/user-attachments/assets/8ab4a8c1-1da7-4581-a29a-13dd49aa4da7" />


error handing known recipent update
<img width="827" height="460" alt="image" src="https://github.com/user-attachments/assets/1ee75baf-4e1a-40bf-adac-952ad56734a5" />

