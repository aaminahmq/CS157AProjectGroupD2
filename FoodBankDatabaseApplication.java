package foodbankdatabaseapplication;


import java.sql.*;
import java.util.Scanner;
import java.text.SimpleDateFormat;
/**
 *
 * 
 *      Group D2
 */
public class FoodBankDatabaseApplication {

    /**
     * @param args the command line arguments
     */
    private static final String url = "jdbc:mysql://localhost:3306/foodbank_b";
    private static final String user = "root";
    private static final String password = "";
    
    // Used to take in user input
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        displayMenu();
    }
    
    
    /**
     * 
     * Attempts to establish a connection to the database.
     * 
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    
    /**
     * 
     * Displays the food bank database menu
     * 
     */
    public static void displayMenu() {
        int choice = -1;
        while (choice != 6) {
            System.out.println("\n--- FoodBank Database Menu ---");
            System.out.println("1. View Foodbank Database");
            System.out.println("2. Insert into FoodBank Database");
            System.out.println("3. Update FoodBank Database");
            System.out.println("4. Delete FoodBank Database");
            System.out.println("5. Run Transaction ( Commit/Rollback )");
            System.out.println("6. Exit View of FoodBank Database");
            System.out.print("Enter choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                try { 
                    switch (choice) {
                        case 1:
                            viewData();
                            break;
                        case 2:
                            insertData();
                            break;
                        case 3:
                            updateData();
                            break;
                        case 4:
                            deleteData();
                            break;
                        case 5:
                            runTransaction();
                            break;
                        case 6:
                            System.out.println("Exiting View of FoodBank Database Application.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println("SQL Error during operation: " + e.getMessage() + "\n");
                }
            } 
            
            else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }
    
    // 1. VIEW DATA FROM FOOD BANK DATABASE
    private static void viewData() throws SQLException {
        
        int choice = -1;
        while (choice != 8) {
            System.out.println("\n * - * - VIEW ALL FOODBANK ENTITIES/TABLES * - * -\n");
            System.out.println("1. DISTRIBUTION");
            System.out.println("2. DONATION");
            System.out.println("3. DONOR");
            System.out.println("4. FOOD ITEM");
            System.out.println("5. INVENTORY");
            System.out.println("6. RECIPIENT");
            System.out.println("7. VOLUNTEER");
            System.out.println("8. EXIT VIEW");
            System.out.print("Enter choice: ");
            
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                try { 
                    switch (choice) {
                        case 1:
                            String distribution_sql = "SELECT * FROM Distribution ORDER BY distribution_id";
                            
                            try (Connection conn = getConnection();
                                    PreparedStatement ps = conn.prepareStatement(distribution_sql);
                                    ResultSet rs = ps.executeQuery()) {
                                
                                // Check if the result set is empty
                                if (!rs.isBeforeFirst()) { 
                                    System.out.println("No products found in the database.");
                                    return;
                                }

                                System.out.printf("\n%-15s | %-15s | %-10s | %s%n", "Distribution ID", "Recipient ID", "Value", "City");
                                System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                                while (rs.next()) {
                                    int dis_id = rs.getInt("distribution_id");
                                    int recip_id = rs.getInt("recipient_id");
                                    double value = rs.getDouble("itemValue");
                                    String city = rs.getString("city_location");
                                    System.out.printf("%-15d | %-15d | %-10s | %-15s  \n\n", dis_id, recip_id, String.format("$%.2f", value), city);
                                }
                            }
                            
                            break;
                        case 2:
                            String donation_sql = "SELECT * FROM Donation ORDER BY donation_id";
                            
                            try (Connection conn = getConnection();
                                    PreparedStatement ps = conn.prepareStatement(donation_sql);
                                    ResultSet rs = ps.executeQuery()) {
                                
                                // Check if the result set is empty
                                if (!rs.isBeforeFirst()) { 
                                    System.out.println("No products found in the database.");
                                    return;
                                }

                                System.out.printf("\n%-12s | %-12s | %-12s | %-10s | %-15s | %s%n", "Donation ID", "Donor ID", "Inventory ID", "Date", "Amount Added", "Notes");
                                System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                                while (rs.next()) {
                                    int dis_id = rs.getInt("donation_id");
                                    int recip_id = rs.getInt("donor_id");
                                    int invent_id = rs.getInt("inventory_id");
                                    Date date = rs.getDate("date_given"); 
                                    double amount = rs.getDouble("amount_added");
                                    String notes = rs.getString("notes");
                                    // Format and display the date
                                    // Date format
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    String formattedDate = dateFormat.format(date);
                                    System.out.printf("%-12d | %-12d | %-12d | %-10s | %-15s | %-15s  \n\n", dis_id, recip_id, invent_id, formattedDate, String.format("$%.2f", amount), notes);
                                }
                            }
                            break;
                        case 3:
                            String donor_sql = "SELECT * FROM Donor ORDER BY donor_id";
                            
                            try (Connection conn = getConnection();
                                    PreparedStatement ps = conn.prepareStatement(donor_sql);
                                    ResultSet rs = ps.executeQuery()) {
                                
                                // Check if the result set is empty
                                if (!rs.isBeforeFirst()) { 
                                    System.out.println("No products found in the database.");
                                    return;
                                }

                                System.out.printf("\n%-12s | %-20s | %-25s | %s%n", "Donor ID", "Name", "Contact Info", "Type");
                                System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                                while (rs.next()) {
                                    int don_id = rs.getInt("donor_id");
                                    String name = rs.getString("name");
                                    String contact = rs.getString("contact_info");
                                    String type = rs.getString("type");

                                    System.out.printf("%-12d | %-20s | %-25s | %-10s   \n\n", don_id, name, contact, type);
                                }
                            }
                            break;
                        case 4:
                            String foodItem_sql = "SELECT * FROM  FoodItem ORDER BY food_item_id";
                            
                            try (Connection conn = getConnection();
                                    PreparedStatement ps = conn.prepareStatement(foodItem_sql);
                                    ResultSet rs = ps.executeQuery()) {
                                
                                // Check if the result set is empty
                                if (!rs.isBeforeFirst()) { 
                                    System.out.println("No products found in the database.");
                                    return;
                                }

                                System.out.printf("\n%-12s | %-20s | %-25s | %s%n", "Inventory ID", "Food Item ID", "Name", "Quantity");
                                System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                                while (rs.next()) {
                                    int inv_id = rs.getInt("inventory_id");
                                    String food_id = rs.getString("food_item_id");
                                    String name = rs.getString("name");
                                    int qty = rs.getInt("quantity");

                                    System.out.printf("%-12d | %-20s | %-25s | %-10d   \n\n", inv_id, food_id, name, qty);
                                }
                            }
                            break;
                        case 5:
                            String inventory_sql = "SELECT * FROM  Inventory ORDER BY inventory_id";
                            
                            try (Connection conn = getConnection();
                                    PreparedStatement ps = conn.prepareStatement(inventory_sql);
                                    ResultSet rs = ps.executeQuery()) {
                                
                                // Check if the result set is empty
                                if (!rs.isBeforeFirst()) { 
                                    System.out.println("No products found in the database.");
                                    return;
                                }

                                System.out.printf("\n%-12s | %-20s | %-25s | %s%n", "Inventory ID", "Storage Location", "Category", "Item Value");
                                System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                                while (rs.next()) {
                                    int inv_id = rs.getInt("inventory_id");
                                    String location = rs.getString("storage_location");
                                    String category = rs.getString("category");
                                    double itemValue = rs.getDouble("itemValue");

                                    System.out.printf("%-12d | %-20s | %-25s | %-10s   \n\n", inv_id, location, category, String.format("$%.2f", itemValue));
                                }
                            }
                            break;
                        case 6:
                            String recipient_sql = "SELECT * FROM  Recipient ORDER BY recipient_id";
                            
                            try (Connection conn = getConnection();
                                    PreparedStatement ps = conn.prepareStatement(recipient_sql);
                                    ResultSet rs = ps.executeQuery()) {
                                
                                // Check if the result set is empty
                                if (!rs.isBeforeFirst()) { 
                                    System.out.println("No products found in the database.");
                                    return;
                                }

                                System.out.printf("\n%-12s | %-20s | %-25s | %-12s | %s%n", "Recipient ID", "Name", "Contact Info", "Family Size", "Status");
                                System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                                while (rs.next()) {
                                    int recip_id = rs.getInt("recipient_id");
                                    String name = rs.getString("name");
                                    String contact = rs.getString("contact_info");
                                    int family_size = rs.getInt("family_size");
                                    String status = rs.getString("status");

                                    System.out.printf("%-12d | %-20s | %-25s | %-12d | %-10s     \n\n", recip_id, name, contact, family_size, status);
                                }
                            }
                            break;
                        case 7:
                            String volunteer_sql = "SELECT * FROM  Volunteer ORDER BY volunteer_id";
                            
                            try (Connection conn = getConnection();
                                    PreparedStatement ps = conn.prepareStatement(volunteer_sql);
                                    ResultSet rs = ps.executeQuery()) {
                                
                                // Check if the result set is empty
                                if (!rs.isBeforeFirst()) { 
                                    System.out.println("No products found in the database.");
                                    return;
                                }

                                System.out.printf("\n%-12s | %-25s | %-15s | %-12s | %s%n", "Volunteer ID", "Name", "Role", "Shift Date", "Contact Info");
                                System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
                                while (rs.next()) {
                                    int recip_id = rs.getInt("volunteer_id");
                                    String name = rs.getString("name");
                                    String role = rs.getString("role");
                                    Date date = rs.getDate("shift_date");
                                    String contact = rs.getString("contact_info");
                                    
                                    
                                    // Format and display the date
                                    // Date format
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    String formattedDate = dateFormat.format(date);
                                    System.out.printf("%-12d | %-25s | %-15s | %-12s | %-20s     \n\n", recip_id, name, role, formattedDate, contact);
                                }
                            }
                            break;
                        case 8:
                            System.out.println("Exiting FoodBank Database Application. Goodbye!");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println("SQL Error during operation: " + e.getMessage() +"\n");
                }
            } 
            
            else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }
    
    // 2. INSERT DATA FROM FOOD BANK DATABASE
    private static void insertData() throws SQLException {
        int choice = -1;
        while (choice != 4) {
            System.out.println("\n * - * - INSERT INTO FOODBANK DATABASE * - * -\n");
            System.out.println("1. DISTRIBUTION");
            System.out.println("2. DONOR");
            System.out.println("3. RECIPIENT");
            System.out.println("4. EXIT INSERTION INTO DATABASE");
            System.out.print("Enter choice: ");
            
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();                                             // Consume newline
                try { 
                    switch (choice) {
                        case 1:
                            int dist_id;
                            int dist_recip_id;
                            Double dist_value = -1.00;
                            String dist_location;
                                   
                            System.out.print("Enter Distribution ID: ");
                            dist_id = scanner.nextInt();
                            System.out.print("Enter Recipient ID: ");
                            dist_recip_id = scanner.nextInt();
                            scanner.nextLine();                                 // Consume newline
                            System.out.print("Enter City Location: ");
                            dist_location = scanner.nextLine();
                            
                            while (dist_value < 0) {
                                System.out.print("Enter product price: ");
                                if (scanner.hasNextDouble()) {
                                    dist_value = scanner.nextDouble();
                                    if (dist_value < 0) System.out.println("Price must be non-negative.");
                                } else {
                                    System.out.println("Invalid price format.");
                                    scanner.nextLine();                         // Consume invalid input
                                }
                            }
                            
                            scanner.nextLine();                                 // Consume newline after reading price
                            
                            String dis_insert_sql = "INSERT INTO Distribution (distribution_id, recipient_id, itemValue, city_location) VALUES (?, ?, ?, ?)";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(dis_insert_sql)) {
                                
                                ps.setInt(1, dist_id);
                                ps.setInt(2, dist_recip_id);
                                ps.setDouble(3, dist_value);
                                ps.setString(4, dist_location);

                                int rows = ps.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("SUCCESS: Distribution '" + dist_id + "' inserted.");
                                } else {
                                    System.out.println("FAILURE: Distribution insertion failed.");
                                }
                            }
                            break;




                        case 2:
                            int don_id;
                            String don_name;
                            String don_contact;
                            String don_type;
                                   
                            System.out.print("Enter Donor ID: ");
                            don_id = scanner.nextInt();
                            scanner.nextLine();                                 // Consume newline
                            System.out.print("Enter Donation Name: ");
                            don_name = scanner.nextLine();
                            System.out.print("Enter Contact Information: ");
                            don_contact = scanner.nextLine();
                            System.out.print("Enter Type of Donor: ");
                            don_type = scanner.nextLine();
                            
                            
                            String don_insert_sql = "INSERT INTO Donor (donor_id, name, contact, type) VALUES (?, ?, ?, ?)";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(don_insert_sql)) {
                                
                                ps.setInt(1, don_id);
                                ps.setString(2, don_name);
                                ps.setString(3, don_contact);
                                ps.setString(4, don_type);

                                int rows = ps.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("SUCCESS: Donor '" + don_id + "' inserted.");
                                } else {
                                    System.out.println("FAILURE: Donor insertion failed.");
                                }
                            }
                            break;

                            
                        case 3:
                            int recip_id;
                            int recip_family_size;
                            String recip_name;
                            String recip_contact;
                            String recip_status;
                                   
                            System.out.print("Enter Recipient ID: ");
                            recip_id = scanner.nextInt();
                            scanner.nextLine();                                 // Consume newline
                            System.out.print("Enter Donation Name: ");
                            recip_name = scanner.nextLine();
                            System.out.print("Enter Contact Information: ");
                            recip_contact = scanner.nextLine();
                            System.out.print("Enter Family Size: ");
                            recip_family_size = scanner.nextInt();
                            scanner.nextLine();                                 // Consume newline
                            System.out.print("Enter Status: ");
                            recip_status = scanner.nextLine();
                            
                            
                            String recip_insert_sql = "INSERT INTO Recipient (recipient_id, name, contact, family_size, status) VALUES (?, ?, ?, ?, ?)";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(recip_insert_sql)) {
                                
                                ps.setInt(1, recip_id);
                                ps.setString(2, recip_name);
                                ps.setString(3, recip_contact);
                                ps.setInt(4, recip_family_size);
                                ps.setString(5, recip_status);

                                int rows = ps.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("SUCCESS: Recipient '" + recip_id + "' inserted.");
                                } else {
                                    System.out.println("FAILURE: Recipient insertion failed.");
                                }
                            }
                            break;

                        case 4:
                            System.out.println("Exiting Insertion of FoodBank Database Application.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println("SQL Error during operation: " + e.getMessage() + "\n");
                }
            }
            
            else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }
    
    // 3. UPDATE DATA FROM FOOD BANK DATABASE
    private static void updateData() throws SQLException {
        int choice = -1;
        while (choice != 4) {
            System.out.println("\n * - * - * - UPDATE FOODBANK DATABASE * _ * - * -\n");
            System.out.println("1. DISTRIBUTION");
            System.out.println("2. DONOR");
            System.out.println("3. RECIPIENT");
            System.out.println("4. EXIT INSERTION INTO DATABASE");
            System.out.print("Enter choice: ");
            
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();                                             // Consume newline
                try { 
                    switch (choice) {
                        case 1:
                            updateData();
                            break;
                        case 2:
                            
                            break;
                        case 3:
                            
                            break;
                        case 4:
                            System.out.println("Exiting Insertion of FoodBank Database Application.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println("SQL Error during operation: " + e.getMessage() + "\n");
                }
            }
            
            else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }
    
    // 4. DELETE DATA FROM FOOD BANK DATABASE
    private static void deleteData() throws SQLException {
        int choice = -1;
        while (choice != 4) {
            System.out.println("\n * - * - DELETE FROM FOODBANK DATABASE * - * -\n");
            System.out.println("1. DISTRIBUTION");
            System.out.println("2. DONOR");
            System.out.println("3. RECIPIENT");
            System.out.println("4. EXIT DELETION OF DATABASE");
            System.out.print("Enter choice: ");
            
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();                                             // Consume newline
                try { 
                    switch (choice) {
                        case 1:
                            int dist_id;
                                   
                            System.out.print("Enter Distribution ID: ");
                            dist_id = scanner.nextInt();
                            
                            String dis_delete_sql = "DELETE FROM Distribution WHERE distribution_id = ?";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(dis_delete_sql)) {
                                
                                ps.setInt(1, dist_id);

                                int rows = ps.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("SUCCESS: Distribution '" + dist_id + "' deleted.");
                                } else {
                                    System.out.println("FAILURE: Distribution deletion failed.");
                                }
                            }
                            break;
                        case 2:
                            String donor_name;
                                   
                            System.out.print("Enter Distribution ID: ");
                            donor_name = scanner.nextLine();
                            
                            String don_delete_sql = "DELETE FROM Donor WHERE name = ?";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(don_delete_sql)) {
                                
                                ps.setString(1, donor_name);

                                int rows = ps.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("SUCCESS: Donor '" + donor_name + "' deleted.");
                                } else {
                                    System.out.println("FAILURE: Donor deletion failed.");
                                }
                            }
                            break;
                        case 3:
                            String recip_name;
                                   
                            System.out.print("Enter Recipient ID: ");
                            recip_name = scanner.nextLine();
                            
                            String recip_delete_sql = "DELETE FROM Recipient WHERE name = ?";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(recip_delete_sql)) {
                                
                                ps.setString(1, recip_name);

                                int rows = ps.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("SUCCESS: Recipient '" + recip_name + "' deleted.");
                                } else {
                                    System.out.println("FAILURE: Recipient deletion failed.");
                                }
                            }
                            break;
                        case 4:
                            System.out.println("Exiting Deletion of FoodBank Database Application.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println("SQL Error during operation: " + e.getMessage() + "\n");
                }
            }
            
            else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }
    
    // 5. RUN TRANSACTION
    private static void runTransaction() throws SQLException {
        
    }
    
}
