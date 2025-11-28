import java.sql.*;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *  FoodBank Database Console Application
 *  Group D2
 */
public class FoodBankDatabaseApplication {

    // Scanner for user input
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Read database connection info from app.properties
     */
    private static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("app.properties")) {
            props.load(fis);
        } catch (IOException e) {
            throw new SQLException("Failed to load app.properties: " + e.getMessage(), e);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        if (url == null || user == null) {
            throw new SQLException("Missing db.url or db.user in app.properties");
        }

        // JDBC driver is on the classpath (mysql-connector-j)
        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        displayMenu();
    }

    /**
     * Main menu
     */
    public static void displayMenu() {
        int choice = -1;
        while (choice != 6) {
            System.out.println("\n--- FoodBank Database Menu ---");
            System.out.println("1. View Foodbank Database");
            System.out.println("2. Insert into FoodBank Database");
            System.out.println("3. Update FoodBank Database");
            System.out.println("4. Delete from FoodBank Database");
            System.out.println("5. Run Transaction (Commit/Rollback)");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

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
                            System.out.println("Exiting FoodBank Database Application.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println("SQL Error during operation: " + e.getMessage() + "\n");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume invalid input
            }
        }
    }

    // 1. VIEW DATA FROM FOOD BANK DATABASE
    private static void viewData() throws SQLException {

        int choice = -1;
        while (choice != 8) {
            System.out.println("\n * - * - VIEW ALL FOODBANK TABLES * - * -\n");
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
                        case 1: {
                            String sql = "SELECT * FROM Distribution ORDER BY distribution_id";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(sql);
                                 ResultSet rs = ps.executeQuery()) {

                                if (!rs.isBeforeFirst()) {
                                    System.out.println("No distributions found.");
                                    break;
                                }

                                System.out.printf("\n%-15s | %-15s | %-10s | %s%n",
                                        "Distribution ID", "Recipient ID", "Value", "City");
                                System.out.println("------------------------------------------------------------------");
                                while (rs.next()) {
                                    int disId = rs.getInt("distribution_id");
                                    int recipId = rs.getInt("recipient_id");
                                    double value = rs.getDouble("itemValue");
                                    String city = rs.getString("city_location");
                                    System.out.printf("%-15d | %-15d | %-10s | %-15s%n",
                                            disId, recipId, String.format("$%.2f", value), city);
                                }
                            }
                            break;
                        }
                        case 2: {
                            String sql = "SELECT * FROM Donation ORDER BY donation_id";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(sql);
                                 ResultSet rs = ps.executeQuery()) {

                                if (!rs.isBeforeFirst()) {
                                    System.out.println("No donations found.");
                                    break;
                                }

                                System.out.printf("\n%-12s | %-12s | %-12s | %-10s | %-15s | %s%n",
                                        "Donation ID", "Donor ID", "Inventory ID", "Date", "Amount Added", "Notes");
                                System.out.println("----------------------------------------------------------------------------------------------------------");
                                while (rs.next()) {
                                    int donationId = rs.getInt("donation_id");
                                    int donorId = rs.getInt("donor_id");
                                    int inventId = rs.getInt("inventory_id");
                                    Date date = rs.getDate("date_given");
                                    double amount = rs.getDouble("amount_added");
                                    String notes = rs.getString("notes");

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    String formattedDate = date != null ? dateFormat.format(date) : "N/A";

                                    System.out.printf("%-12d | %-12d | %-12d | %-10s | %-15s | %s%n",
                                            donationId, donorId, inventId, formattedDate,
                                            String.format("$%.2f", amount),
                                            (notes == null ? "" : notes));
                                }
                            }
                            break;
                        }
                        case 3: {
                            String sql = "SELECT * FROM Donor ORDER BY donor_id";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(sql);
                                 ResultSet rs = ps.executeQuery()) {

                                if (!rs.isBeforeFirst()) {
                                    System.out.println("No donors found.");
                                    break;
                                }

                                System.out.printf("\n%-12s | %-20s | %-25s | %s%n",
                                        "Donor ID", "Name", "Contact Info", "Type");
                                System.out.println("--------------------------------------------------------------------------------------------");
                                while (rs.next()) {
                                    int donId = rs.getInt("donor_id");
                                    String name = rs.getString("name");
                                    String contact = rs.getString("contact_info");
                                    String type = rs.getString("type");

                                    System.out.printf("%-12d | %-20s | %-25s | %-10s%n",
                                            donId, name, contact, type);
                                }
                            }
                            break;
                        }
                        case 4: {
                            String sql = "SELECT * FROM FoodItem ORDER BY inventory_id, food_item_id";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(sql);
                                 ResultSet rs = ps.executeQuery()) {

                                if (!rs.isBeforeFirst()) {
                                    System.out.println("No food items found.");
                                    break;
                                }

                                System.out.printf("\n%-12s | %-15s | %-25s | %s%n",
                                        "Inventory ID", "Food Item ID", "Name", "Quantity");
                                System.out.println("--------------------------------------------------------------------------------");
                                while (rs.next()) {
                                    int invId = rs.getInt("inventory_id");
                                    int foodId = rs.getInt("food_item_id");
                                    String name = rs.getString("name");
                                    int qty = rs.getInt("quantity");

                                    System.out.printf("%-12d | %-15d | %-25s | %-10d%n",
                                            invId, foodId, name, qty);
                                }
                            }
                            break;
                        }
                        case 5: {
                            String sql = "SELECT * FROM Inventory ORDER BY inventory_id";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(sql);
                                 ResultSet rs = ps.executeQuery()) {

                                if (!rs.isBeforeFirst()) {
                                    System.out.println("No inventory records found.");
                                    break;
                                }

                                System.out.printf("\n%-12s | %-20s | %-25s | %s%n",
                                        "Inventory ID", "Storage Location", "Category", "Item Value");
                                System.out.println("--------------------------------------------------------------------------------");
                                while (rs.next()) {
                                    int invId = rs.getInt("inventory_id");
                                    String location = rs.getString("storage_location");
                                    String category = rs.getString("category");
                                    double itemValue = rs.getDouble("itemValue");

                                    System.out.printf("%-12d | %-20s | %-25s | %-10s%n",
                                            invId, location, category, String.format("$%.2f", itemValue));
                                }
                            }
                            break;
                        }
                        case 6: {
                            String sql = "SELECT * FROM Recipient ORDER BY recipient_id";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(sql);
                                 ResultSet rs = ps.executeQuery()) {

                                if (!rs.isBeforeFirst()) {
                                    System.out.println("No recipients found.");
                                    break;
                                }

                                System.out.printf("\n%-12s | %-20s | %-25s | %-12s | %s%n",
                                        "Recipient ID", "Name", "Contact Info", "Family Size", "Status");
                                System.out.println("------------------------------------------------------------------------------------------------");
                                while (rs.next()) {
                                    int recipId = rs.getInt("recipient_id");
                                    String name = rs.getString("name");
                                    String contact = rs.getString("contact_info");
                                    int familySize = rs.getInt("family_size");
                                    String status = rs.getString("status");

                                    System.out.printf("%-12d | %-20s | %-25s | %-12d | %-10s%n",
                                            recipId, name, contact, familySize, status);
                                }
                            }
                            break;
                        }
                        case 7: {
                            String sql = "SELECT * FROM Volunteer ORDER BY volunteer_id";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(sql);
                                 ResultSet rs = ps.executeQuery()) {

                                if (!rs.isBeforeFirst()) {
                                    System.out.println("No volunteers found.");
                                    break;
                                }

                                System.out.printf("\n%-12s | %-25s | %-15s | %-12s | %s%n",
                                        "Volunteer ID", "Name", "Role", "Shift Date", "Contact Info");
                                System.out.println("------------------------------------------------------------------------------------------------");
                                while (rs.next()) {
                                    int volId = rs.getInt("volunteer_id");
                                    String name = rs.getString("name");
                                    String role = rs.getString("role");
                                    Date date = rs.getDate("shift_date");
                                    String contact = rs.getString("contact_info");

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    String formattedDate = date != null ? dateFormat.format(date) : "N/A";

                                    System.out.printf("%-12d | %-25s | %-15s | %-12s | %-20s%n",
                                            volId, name, role, formattedDate, contact);
                                }
                            }
                            break;
                        }
                        case 8:
                            System.out.println("Exiting view.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println("SQL Error during operation: " + e.getMessage() + "\n");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    // 2. INSERT DATA INTO FOOD BANK DATABASE
    private static void insertData() throws SQLException {
        int choice = -1;
        while (choice != 4) {
            System.out.println("\n * - * - INSERT INTO FOODBANK DATABASE * - * -\n");
            System.out.println("1. DISTRIBUTION");
            System.out.println("2. DONOR");
            System.out.println("3. RECIPIENT");
            System.out.println("4. EXIT");
            System.out.print("Enter choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                try {
                    switch (choice) {
                        case 1: { // Insert Distribution
                            System.out.print("Enter Recipient ID: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("Invalid recipient ID.");
                                scanner.nextLine();
                                break;
                            }
                            int recipId = scanner.nextInt();
                            scanner.nextLine(); // newline

                            System.out.print("Enter City Location: ");
                            String city = scanner.nextLine().trim();
                            if (city.isEmpty()) {
                                System.out.println("City cannot be empty.");
                                break;
                            }

                            double value = -1.0;
                            while (value < 0) {
                                System.out.print("Enter distribution value (>= 0): ");
                                if (scanner.hasNextDouble()) {
                                    value = scanner.nextDouble();
                                    if (value < 0) {
                                        System.out.println("Value must be non-negative.");
                                    }
                                } else {
                                    System.out.println("Invalid value format.");
                                    scanner.nextLine();
                                }
                            }
                            scanner.nextLine(); // consume newline

                            String sql = "INSERT INTO Distribution (recipient_id, itemValue, city_location) VALUES (?, ?, ?)";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(sql)) {

                                ps.setInt(1, recipId);
                                ps.setDouble(2, value);
                                ps.setString(3, city);

                                int rows = ps.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("SUCCESS: Distribution inserted.");
                                } else {
                                    System.out.println("FAILURE: Distribution insertion failed.");
                                }
                            }
                            break;
                        }
                        case 2: { // Insert Donor
                            System.out.print("Enter Donor Name: ");
                            String name = scanner.nextLine().trim();
                            if (name.isEmpty()) {
                                System.out.println("Name cannot be empty.");
                                break;
                            }

                            System.out.print("Enter Contact Email: ");
                            String contact = scanner.nextLine().trim();

                            System.out.print("Enter Type of Donor (individual/organization): ");
                            String type = scanner.nextLine().trim().toLowerCase();

                            if (!type.equals("individual") && !type.equals("organization")) {
                                System.out.println("Invalid donor type. Must be 'individual' or 'organization'.");
                                break;
                            }

                            String sql = "INSERT INTO Donor (name, contact_info, type) VALUES (?, ?, ?)";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(sql)) {

                                ps.setString(1, name);
                                ps.setString(2, contact);
                                ps.setString(3, type);

                                int rows = ps.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("SUCCESS: Donor inserted.");
                                } else {
                                    System.out.println("FAILURE: Donor insertion failed.");
                                }
                            }
                            break;
                        }
                        case 3: { // Insert Recipient
                            System.out.print("Enter Recipient Name: ");
                            String name = scanner.nextLine().trim();
                            if (name.isEmpty()) {
                                System.out.println("Name cannot be empty.");
                                break;
                            }

                            System.out.print("Enter Contact Email: ");
                            String contact = scanner.nextLine().trim();

                            System.out.print("Enter Family Size: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("Invalid family size.");
                                scanner.nextLine();
                                break;
                            }
                            int familySize = scanner.nextInt();
                            scanner.nextLine();
                            if (familySize < 0) {
                                System.out.println("Family size must be >= 0.");
                                break;
                            }

                            System.out.print("Enter Status (active/paused): ");
                            String status = scanner.nextLine().trim().toLowerCase();

                            if (!status.equals("active") && !status.equals("paused")) {
                                System.out.println("Invalid status. Must be 'active' or 'paused'.");
                                break;
                            }

                            String sql = "INSERT INTO Recipient (name, contact_info, family_size, status) VALUES (?, ?, ?, ?)";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(sql)) {

                                ps.setString(1, name);
                                ps.setString(2, contact);
                                ps.setInt(3, familySize);
                                ps.setString(4, status);

                                int rows = ps.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("SUCCESS: Recipient inserted.");
                                } else {
                                    System.out.println("FAILURE: Recipient insertion failed.");
                                }
                            }
                            break;
                        }
                        case 4:
                            System.out.println("Exiting insertion menu.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println("SQL Error during operation: " + e.getMessage() + "\n");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    // 3. UPDATE DATA FROM FOOD BANK DATABASE
    private static void updateData() throws SQLException {
        int choice = -1;
        while (choice != 4) {
            System.out.println("\n * - * - UPDATE FOODBANK DATABASE * - * -\n");
            System.out.println("1. UPDATE DISTRIBUTION (city/itemValue)");
            System.out.println("2. UPDATE DONOR " + getContact() + " INFO");
            System.out.println("3. UPDATE RECIPIENT STATUS");
            System.out.println("4. EXIT");
            System.out.print("Enter choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                try {
                    switch (choice) {
                        case 1:
                            updateDistribution();
                            break;
                        case 2:
                            updateDonorContact();
                            break;
                        case 3:
                            updateRecipientStatus();
                            break;
                        case 4:
                            System.out.println("Exiting update menu.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println("SQL Error during operation: " + e.getMessage() + "\n");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    private static String getContact() {
        return "CONTACT";
    }

    private static void updateDistribution() throws SQLException {
        System.out.print("Enter Distribution ID to update: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid ID. Must be a number.");
            scanner.nextLine();
            return;
        }
        int distId = scanner.nextInt();
        scanner.nextLine();

        System.out.println("What do you want to update?");
        System.out.println("1. City location");
        System.out.println("2. Item value");
        System.out.print("Enter choice: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid choice.");
            scanner.nextLine();
            return;
        }
        int opt = scanner.nextInt();
        scanner.nextLine();

        String sql;
        if (opt == 1) {
            System.out.print("Enter new city location: ");
            String city = scanner.nextLine().trim();
            if (city.isEmpty()) {
                System.out.println("City cannot be empty.");
                return;
            }
            sql = "UPDATE Distribution SET city_location = ? WHERE distribution_id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, city);
                ps.setInt(2, distId);
                int rows = ps.executeUpdate();
                System.out.println("Updated rows: " + rows);
            }
        } else if (opt == 2) {
            System.out.print("Enter new item value: ");
            if (!scanner.hasNextDouble()) {
                System.out.println("Invalid value.");
                scanner.nextLine();
                return;
            }
            double value = scanner.nextDouble();
            scanner.nextLine();
            if (value < 0) {
                System.out.println("Value must be >= 0.");
                return;
            }
            sql = "UPDATE Distribution SET itemValue = ? WHERE distribution_id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDouble(1, value);
                ps.setInt(2, distId);
                int rows = ps.executeUpdate();
                System.out.println("Updated rows: " + rows);
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void updateDonorContact() throws SQLException {
        System.out.print("Enter Donor ID to update: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid ID. Must be a number.");
            scanner.nextLine();
            return;
        }
        int donorId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new contact email: ");
        String contact = scanner.nextLine().trim();

        String sql = "UPDATE Donor SET contact_info = ? WHERE donor_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, contact);
            ps.setInt(2, donorId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("SUCCESS: Donor updated.");
            } else {
                System.out.println("No donor found with that ID.");
            }
        }
    }

    private static void updateRecipientStatus() throws SQLException {
        System.out.print("Enter Recipient ID to update: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid ID. Must be a number.");
            scanner.nextLine();
            return;
        }
        int recipId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new status (active/paused): ");
        String status = scanner.nextLine().trim().toLowerCase();

        if (!status.equals("active") && !status.equals("paused")) {
            System.out.println("Invalid status. Must be 'active' or 'paused'.");
            return;
        }

        String sql = "UPDATE Recipient SET status = ? WHERE recipient_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, recipId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("SUCCESS: Recipient updated.");
            } else {
                System.out.println("No recipient found with that ID.");
            }
        }
    }

    // 4. DELETE DATA FROM FOOD BANK DATABASE
    private static void deleteData() throws SQLException {
        int choice = -1;
        while (choice != 4) {
            System.out.println("\n * - * - DELETE FROM FOODBANK DATABASE * - * -\n");
            System.out.println("1. DISTRIBUTION (by ID)");
            System.out.println("2. DONOR (by name)");
            System.out.println("3. RECIPIENT (by name)");
            System.out.println("4. EXIT");
            System.out.print("Enter choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                try {
                    switch (choice) {
                        case 1: {
                            System.out.print("Enter Distribution ID: ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("Invalid ID.");
                                scanner.nextLine();
                                break;
                            }
                            int distId = scanner.nextInt();
                            scanner.nextLine();

                            String sql = "DELETE FROM Distribution WHERE distribution_id = ?";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(sql)) {

                                ps.setInt(1, distId);
                                int rows = ps.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("SUCCESS: Distribution deleted.");
                                } else {
                                    System.out.println("No distribution found with that ID.");
                                }
                            }
                            break;
                        }
                        case 2: {
                            System.out.print("Enter Donor Name: ");
                            String donorName = scanner.nextLine().trim();

                            String sql = "DELETE FROM Donor WHERE name = ?";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(sql)) {

                                ps.setString(1, donorName);
                                int rows = ps.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("SUCCESS: Donor(s) deleted.");
                                } else {
                                    System.out.println("No donor found with that name.");
                                }
                            }
                            break;
                        }
                        case 3: {
                            System.out.print("Enter Recipient Name: ");
                            String recipName = scanner.nextLine().trim();

                            String sql = "DELETE FROM Recipient WHERE name = ?";

                            try (Connection conn = getConnection();
                                 PreparedStatement ps = conn.prepareStatement(sql)) {

                                ps.setString(1, recipName);
                                int rows = ps.executeUpdate();
                                if (rows > 0) {
                                    System.out.println("SUCCESS: Recipient(s) deleted.");
                                } else {
                                    System.out.println("No recipient found with that name.");
                                }
                            }
                            break;
                        }
                        case 4:
                            System.out.println("Exiting delete menu.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (SQLException e) {
                    System.out.println("SQL Error during operation: " + e.getMessage() + "\n");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    // 5. RUN TRANSACTION (Distribution + FoodItem quantity update)
    private static void runTransaction() throws SQLException {
        System.out.println("\n--- Run Distribution Transaction (COMMIT/ROLLBACK Demo) ---");

        try {
            System.out.print("Enter Recipient ID: ");
            int recipientId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Inventory ID: ");
            int inventoryId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Food Item ID: ");
            int foodItemId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter quantity to distribute: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());
            if (qty <= 0) {
                System.out.println("Quantity must be > 0.");
                return;
            }

            System.out.print("Enter city location: ");
            String city = scanner.nextLine().trim();
            if (city.isEmpty()) {
                System.out.println("City cannot be empty.");
                return;
            }

            System.out.print("Enter itemValue for this distribution (e.g., 50.00): ");
            double itemValue = Double.parseDouble(scanner.nextLine().trim());
            if (itemValue < 0) {
                System.out.println("Item value must be >= 0.");
                return;
            }

            try (Connection conn = getConnection()) {
                boolean oldAutoCommit = conn.getAutoCommit();
                conn.setAutoCommit(false);

                try {
                    // 1. Check recipient status
                    String sqlCheckRecipient =
                            "SELECT status FROM Recipient WHERE recipient_id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sqlCheckRecipient)) {
                        ps.setInt(1, recipientId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next()) {
                                System.out.println("Recipient not found.");
                                conn.rollback();
                                return;
                            }
                            String status = rs.getString("status");
                            if (!"active".equalsIgnoreCase(status)) {
                                System.out.println("Recipient is not active. Rolling back.");
                                conn.rollback();
                                return;
                            }
                        }
                    }

                    // 2. Check FoodItem quantity (FOR UPDATE locks the row)
                    String sqlCheckQty =
                            "SELECT quantity FROM FoodItem " +
                                    "WHERE inventory_id = ? AND food_item_id = ? FOR UPDATE";
                    int currentQty;
                    try (PreparedStatement ps = conn.prepareStatement(sqlCheckQty)) {
                        ps.setInt(1, inventoryId);
                        ps.setInt(2, foodItemId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (!rs.next()) {
                                System.out.println("Food item not found for given inventory.");
                                conn.rollback();
                                return;
                            }
                            currentQty = rs.getInt("quantity");
                        }
                    }

                    if (currentQty < qty) {
                        System.out.println("Not enough quantity in inventory. Current = "
                                + currentQty + ", requested = " + qty);
                        conn.rollback(); // demonstrate ROLLBACK
                        return;
                    }

                    // 3. Insert into Distribution
                    String sqlInsertDist =
                            "INSERT INTO Distribution(recipient_id, itemValue, city_location) " +
                                    "VALUES(?,?,?)";
                    try (PreparedStatement ps = conn.prepareStatement(sqlInsertDist)) {
                        ps.setInt(1, recipientId);
                        ps.setDouble(2, itemValue);
                        ps.setString(3, city);
                        ps.executeUpdate();
                    }

                    // 4. Update FoodItem quantity
                    String sqlUpdateQty =
                            "UPDATE FoodItem SET quantity = quantity - ? " +
                                    "WHERE inventory_id = ? AND food_item_id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sqlUpdateQty)) {
                        ps.setInt(1, qty);
                        ps.setInt(2, inventoryId);
                        ps.setInt(3, foodItemId);
                        ps.executeUpdate();
                    }

                    // 5. Commit
                    conn.commit();
                    System.out.println("Transaction committed successfully.");

                } catch (SQLException e) {
                    System.out.println("Error in transactional workflow: " + e.getMessage());
                    try {
                        conn.rollback();
                        System.out.println("Transaction rolled back.");
                    } catch (SQLException ex) {
                        System.out.println("Error rolling back: " + ex.getMessage());
                    }
                } finally {
                    conn.setAutoCommit(oldAutoCommit);
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input: " + e.getMessage());
        }
    }
}
