package hotel.reservation.system;

import java.io.File;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Centralized Database Manager supporting zero-configuration SQLite by default
 * on macOS, with transparent fallback and optional MySQL connectivity.
 * Automatically initializes database schema and realistic Indian demo records.
 */
public class DatabaseManager {

    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    private static volatile DatabaseManager instance;

    private final String dbType;
    private final String url;
    private final String user;
    private final String password;

    private DatabaseManager() {
        String configuredType = System.getProperty("db.type", System.getenv().getOrDefault("DB_TYPE", "sqlite")).toLowerCase();
        if ("mysql".equals(configuredType)) {
            this.dbType = "mysql";
            this.url = System.getProperty("db.url", System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/hotelmanagementsystem"));
            this.user = System.getProperty("db.user", System.getenv().getOrDefault("DB_USER", "root"));
            this.password = System.getProperty("db.password", System.getenv().getOrDefault("DB_PASSWORD", "codeforinterview"));
        } else {
            this.dbType = "sqlite";
            File dbFile = new File("hotel_management.db");
            this.url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            this.user = "";
            this.password = "";
        }
        initDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        try {
            if ("mysql".equals(dbType)) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                return DriverManager.getConnection(url, user, password);
            } else {
                Class.forName("org.xerial.sqlite-jdbc".equals("org.xerial.sqlite-jdbc") ? "org.sqlite.JDBC" : "org.sqlite.JDBC");
                Connection conn = DriverManager.getConnection(url);
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON;");
                }
                return conn;
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found for " + dbType, e);
        }
    }

    public String getDbType() {
        return dbType;
    }

    private void initDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            LOGGER.info("Initializing database: " + dbType + " at " + url);

            // 1. Login table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS login (" +
                "username VARCHAR(50) PRIMARY KEY, " +
                "password VARCHAR(255) NOT NULL, " +
                "role VARCHAR(30) NOT NULL DEFAULT 'Receptionist', " +
                "name VARCHAR(100)" +
                ")"
            );

            // 2. Room table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS room (" +
                "roomNo VARCHAR(20) PRIMARY KEY, " +
                "available VARCHAR(30) NOT NULL DEFAULT 'Available', " +
                "status VARCHAR(30) NOT NULL DEFAULT 'Cleaned', " +
                "price VARCHAR(30) NOT NULL, " +
                "type VARCHAR(40) NOT NULL" +
                ")"
            );

            // 3. Department table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS department (" +
                "department VARCHAR(50) PRIMARY KEY, " +
                "budget VARCHAR(30) NOT NULL" +
                ")"
            );

            // 4. Employee table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS employee (" +
                "id INTEGER PRIMARY KEY " + (isSQLite() ? "AUTOINCREMENT" : "AUTO_INCREMENT") + ", " +
                "name VARCHAR(100) NOT NULL, " +
                "age VARCHAR(10) NOT NULL, " +
                "gender VARCHAR(15) NOT NULL, " +
                "job VARCHAR(50) NOT NULL, " +
                "salary VARCHAR(30) NOT NULL, " +
                "phone VARCHAR(20) NOT NULL, " +
                "email VARCHAR(80) NOT NULL, " +
                "aadhar VARCHAR(25) NOT NULL" +
                ")"
            );

            // 5. Driver table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS driver (" +
                "id INTEGER PRIMARY KEY " + (isSQLite() ? "AUTOINCREMENT" : "AUTO_INCREMENT") + ", " +
                "name VARCHAR(100) NOT NULL, " +
                "age VARCHAR(10) NOT NULL, " +
                "gender VARCHAR(15) NOT NULL, " +
                "company VARCHAR(50) NOT NULL, " +
                "brand VARCHAR(50) NOT NULL, " +
                "available VARCHAR(20) NOT NULL DEFAULT 'Available', " +
                "location VARCHAR(100) NOT NULL" +
                ")"
            );

            // 6. Customer table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS customer (" +
                "id INTEGER PRIMARY KEY " + (isSQLite() ? "AUTOINCREMENT" : "AUTO_INCREMENT") + ", " +
                "document VARCHAR(30) NOT NULL, " +
                "number VARCHAR(50) NOT NULL, " +
                "name VARCHAR(100) NOT NULL, " +
                "gender VARCHAR(15) NOT NULL, " +
                "country VARCHAR(50) NOT NULL, " +
                "room VARCHAR(20) NOT NULL, " +
                "checkintime VARCHAR(80) NOT NULL, " +
                "deposit VARCHAR(30) NOT NULL, " +
                "status VARCHAR(30) NOT NULL DEFAULT 'Checked-In'" +
                ")"
            );

            // 7. Booking / Reservation table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS booking (" +
                "booking_id VARCHAR(30) PRIMARY KEY, " +
                "customer_name VARCHAR(100) NOT NULL, " +
                "phone VARCHAR(20) NOT NULL, " +
                "email VARCHAR(80), " +
                "room_type VARCHAR(40) NOT NULL, " +
                "room_no VARCHAR(20), " +
                "checkin_date VARCHAR(30) NOT NULL, " +
                "checkout_date VARCHAR(30) NOT NULL, " +
                "status VARCHAR(30) NOT NULL DEFAULT 'Confirmed', " +
                "deposit VARCHAR(30) NOT NULL DEFAULT '0'" +
                ")"
            );

            // 8. Billing History table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS billing_history (" +
                "id INTEGER PRIMARY KEY " + (isSQLite() ? "AUTOINCREMENT" : "AUTO_INCREMENT") + ", " +
                "bill_no VARCHAR(50) NOT NULL UNIQUE, " +
                "customer_name VARCHAR(100) NOT NULL, " +
                "customer_id VARCHAR(50) NOT NULL, " +
                "room_no VARCHAR(20) NOT NULL, " +
                "checkin_time VARCHAR(80) NOT NULL, " +
                "checkout_time VARCHAR(80) NOT NULL, " +
                "days INTEGER NOT NULL, " +
                "room_rate REAL NOT NULL, " +
                "room_charges REAL NOT NULL, " +
                "tax_amount REAL NOT NULL, " +
                "deposit_paid REAL NOT NULL, " +
                "net_amount REAL NOT NULL, " +
                "payment_mode VARCHAR(30) NOT NULL DEFAULT 'UPI', " +
                "payment_status VARCHAR(30) NOT NULL DEFAULT 'Settled'" +
                ")"
            );

            seedDemoData(conn);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize database", e);
        }
    }

    private boolean isSQLite() {
        return "sqlite".equalsIgnoreCase(dbType);
    }

    private void seedDemoData(Connection conn) {
        try {
            // Seed Logins
            try (Statement s = conn.createStatement();
                 ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM login")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    LOGGER.info("Seeding demo logins...");
                    String insertLogin = "INSERT INTO login (username, password, role, name) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertLogin)) {
                        // Admin: Vikram Malhotra (Admin@123)
                        ps.setString(1, "admin");
                        ps.setString(2, SecurityUtils.hashPassword("Admin@123"));
                        ps.setString(3, "Admin");
                        ps.setString(4, "Vikram Malhotra");
                        ps.executeUpdate();

                        // Receptionist: Sunita Rao (Sunita@123)
                        ps.setString(1, "sunita");
                        ps.setString(2, SecurityUtils.hashPassword("Sunita@123"));
                        ps.setString(3, "Receptionist");
                        ps.setString(4, "Sunita Rao");
                        ps.executeUpdate();

                        // Legacy demo user: Youraj (12345)
                        ps.setString(1, "reception");
                        ps.setString(2, SecurityUtils.hashPassword("12345"));
                        ps.setString(3, "Receptionist");
                        ps.setString(4, "Front Desk Reception");
                        ps.executeUpdate();
                    }
                }
            }

            // Seed Rooms
            try (Statement s = conn.createStatement();
                 ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM room")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    LOGGER.info("Seeding demo rooms...");
                    String insertRoom = "INSERT INTO room (roomNo, available, status, price, type) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertRoom)) {
                        String[][] demoRooms = {
                            {"101", "Available", "Cleaned", "2500", "Single Bed"},
                            {"102", "Occupied", "Cleaned", "2500", "Single Bed"},
                            {"103", "Available", "Cleaned", "2800", "Single Bed"},
                            {"104", "Available", "Dirty", "2800", "Single Bed"},
                            {"201", "Occupied", "Cleaned", "4500", "Double Bed"},
                            {"202", "Available", "Cleaned", "4500", "Double Bed"},
                            {"203", "Available", "Cleaned", "5000", "Double Bed"},
                            {"301", "Occupied", "Cleaned", "7500", "Double Bed"},
                            {"302", "Available", "Cleaned", "8500", "Double Bed"}
                        };
                        for (String[] r : demoRooms) {
                            ps.setString(1, r[0]);
                            ps.setString(2, r[1]);
                            ps.setString(3, r[2]);
                            ps.setString(4, r[3]);
                            ps.setString(5, r[4]);
                            ps.executeUpdate();
                        }
                    }
                }
            }

            // Seed Departments
            try (Statement s = conn.createStatement();
                 ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM department")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    LOGGER.info("Seeding departments...");
                    String insertDept = "INSERT INTO department (department, budget) VALUES (?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertDept)) {
                        String[][] depts = {
                            {"Front Office", "500000"},
                            {"Housekeeping", "80000"},
                            {"Food and Beverage", "100000"},
                            {"Accounts", "450000"},
                            {"Security", "700000"}
                        };
                        for (String[] d : depts) {
                            ps.setString(1, d[0]);
                            ps.setString(2, d[1]);
                            ps.executeUpdate();
                        }
                    }
                }
            }

            // Seed Employees
            try (Statement s = conn.createStatement();
                 ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM employee")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    LOGGER.info("Seeding demo employees...");
                    String insertEmp = "INSERT INTO employee (name, age, gender, job, salary, phone, email, aadhar) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertEmp)) {
                        String[][] emps = {
                            {"Vikram Malhotra", "38", "male", "Manager", "85000", "9876543210", "vikram.malhotra@tajhotels.in", "453210987654"},
                            {"Sunita Rao", "29", "female", "Front Desk Clerks", "42000", "9811223344", "sunita.rao@tajhotels.in", "567890123456"},
                            {"Ramesh Patel", "44", "male", "Chef", "60000", "9723456789", "ramesh.patel@tajhotels.in", "678901234567"},
                            {"Neha Gupta", "31", "female", "Housekeeping", "28000", "9654321098", "neha.gupta@tajhotels.in", "789012345678"},
                            {"Amitabh Sen", "35", "male", "Accountant", "52000", "9845123456", "amitabh.sen@tajhotels.in", "890123456789"}
                        };
                        for (String[] e : emps) {
                            for (int i = 0; i < 8; i++) {
                                ps.setString(i + 1, e[i]);
                            }
                            ps.executeUpdate();
                        }
                    }
                }
            }

            // Seed Drivers
            try (Statement s = conn.createStatement();
                 ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM driver")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    LOGGER.info("Seeding demo drivers...");
                    String insertDriver = "INSERT INTO driver (name, age, gender, company, brand, available, location) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertDriver)) {
                        String[][] drivers = {
                            {"Rajesh Yadav", "35", "Male", "Toyota", "Innova Crysta", "Available", "IGI Airport Terminal 3"},
                            {"Suresh Kumar", "41", "Male", "Maruti Suzuki", "Ertiga", "Available", "Connaught Place"},
                            {"Manoj Singh", "32", "Male", "Hyundai", "Creta", "Busy", "New Delhi Railway Station"}
                        };
                        for (String[] d : drivers) {
                            for (int i = 0; i < 7; i++) {
                                ps.setString(i + 1, d[i]);
                            }
                            ps.executeUpdate();
                        }
                    }
                }
            }

            // Seed Active Customers (Realistic Indian Profiles)
            try (Statement s = conn.createStatement();
                 ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM customer WHERE status = 'Checked-In'")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    LOGGER.info("Seeding active demo guests...");
                    String insertCust = "INSERT INTO customer (document, number, name, gender, country, room, checkintime, deposit, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertCust)) {
                        String[][] guests = {
                            {"Passport", "P8972341", "Aman Sharma", "Male", "India", "102", "2026-10-25 14:30:00", "2000", "Checked-In"},
                            {"Aadhar Card", "782345619012", "Priya Singh", "Female", "India", "201", "2026-10-26 11:15:00", "4000", "Checked-In"},
                            {"Driving license", "DL042019087", "Aditya Verma", "Male", "India", "301", "2026-10-27 16:00:00", "5000", "Checked-In"}
                        };
                        for (String[] g : guests) {
                            for (int i = 0; i < 9; i++) {
                                ps.setString(i + 1, g[i]);
                            }
                            ps.executeUpdate();
                        }
                    }
                }
            }

            // Seed Past Billing History (Completed Stays)
            try (Statement s = conn.createStatement();
                 ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM billing_history")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    LOGGER.info("Seeding past billing history...");
                    String insertBill = "INSERT INTO billing_history (bill_no, customer_name, customer_id, room_no, checkin_time, checkout_time, days, room_rate, room_charges, tax_amount, deposit_paid, net_amount, payment_mode, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertBill)) {
                        ps.setString(1, "INV-2026-001");
                        ps.setString(2, "Rahul Kumar");
                        ps.setString(3, "DL9834125");
                        ps.setString(4, "104");
                        ps.setString(5, "2026-10-22 10:00:00");
                        ps.setString(6, "2026-10-24 12:00:00");
                        ps.setInt(7, 2);
                        ps.setDouble(8, 2500.0);
                        ps.setDouble(9, 5000.0);
                        ps.setDouble(10, 600.0); // 12% GST
                        ps.setDouble(11, 2000.0);
                        ps.setDouble(12, 3600.0);
                        ps.setString(13, "Credit Card");
                        ps.setString(14, "Settled");
                        ps.executeUpdate();
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error seeding demo data: " + e.getMessage(), e);
        }
    }
}
