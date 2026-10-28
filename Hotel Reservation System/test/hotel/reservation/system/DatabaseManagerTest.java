package hotel.reservation.system;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    @BeforeAll
    static void setup() {
        DatabaseManager.getInstance();
    }

    @Test
    void testConnectionIsNotNullAndOpen() throws Exception {
        try (Connection conn = DatabaseManager.getInstance().getConnection()) {
            assertNotNull(conn);
            assertFalse(conn.isClosed());
        }
    }

    @Test
    void testRequiredTablesExist() throws Exception {
        String[] tables = {"login", "room", "department", "employee", "driver", "customer", "booking", "billing_history"};

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            for (String table : tables) {
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table)) {
                    assertTrue(rs.next(), "Table " + table + " should be queryable");
                }
            }
        }
    }

    @Test
    void testSeedIndianDemoDataIntegrity() throws Exception {
        try (Connection conn = DatabaseManager.getInstance().getConnection()) {

            // 1. Check admin login
            try (PreparedStatement ps = conn.prepareStatement("SELECT role, name FROM login WHERE username = ?")) {
                ps.setString(1, "admin");
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Admin user should exist");
                    assertEquals("Admin", rs.getString("role"));
                    assertEquals("Vikram Malhotra", rs.getString("name"));
                }
            }

            // 2. Check active demo guests (Indian names)
            try (PreparedStatement ps = conn.prepareStatement("SELECT name, room FROM customer WHERE name = ? AND status = 'Checked-In'")) {
                ps.setString(1, "Aman Sharma");
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Aman Sharma should be checked into a room");
                    assertEquals("102", rs.getString("room"));
                }
            }

            // 3. Check Priya Singh
            try (PreparedStatement ps = conn.prepareStatement("SELECT room FROM customer WHERE name = ? AND status = 'Checked-In'")) {
                ps.setString(1, "Priya Singh");
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Priya Singh should be checked into room 201");
                    assertEquals("201", rs.getString("room"));
                }
            }

            // 4. Check Aditya Verma
            try (PreparedStatement ps = conn.prepareStatement("SELECT room FROM customer WHERE name = ? AND status = 'Checked-In'")) {
                ps.setString(1, "Aditya Verma");
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next(), "Aditya Verma should be checked into room 301");
                    assertEquals("301", rs.getString("room"));
                }
            }
        }
    }
}
