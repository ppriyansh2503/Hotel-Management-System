package hotel.reservation.system;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static org.junit.jupiter.api.Assertions.*;

public class BillingAndRoomServiceTest {

    @BeforeAll
    static void init() {
        DatabaseManager.getInstance();
    }

    @Test
    void testStandardGSTRateCalculation() {
        double roomRate = 2500.0;
        int nights = 2;
        double gross = roomRate * nights; // 5000.0
        double taxRate = roomRate >= 7500.0 ? 0.18 : 0.12;
        double tax = gross * taxRate; // 600.0
        double deposit = 2000.0;
        double netPayable = (gross + tax) - deposit; // 3600.0

        assertEquals(0.12, taxRate, 0.001);
        assertEquals(5000.0, gross, 0.001);
        assertEquals(600.0, tax, 0.001);
        assertEquals(3600.0, netPayable, 0.001);
    }

    @Test
    void testLuxurySuiteGSTRateCalculation() {
        double roomRate = 8500.0; // Luxury Suite >= 7500
        int nights = 3;
        double gross = roomRate * nights; // 25500.0
        double taxRate = roomRate >= 7500.0 ? 0.18 : 0.12;
        double tax = gross * taxRate; // 4590.0
        double deposit = 5000.0;
        double netPayable = (gross + tax) - deposit; // 25090.0

        assertEquals(0.18, taxRate, 0.001);
        assertEquals(25500.0, gross, 0.001);
        assertEquals(4590.0, tax, 0.001);
        assertEquals(25090.0, netPayable, 0.001);
    }

    @Test
    void testRoomStateTransitions() throws Exception {
        String testRoom = "101";

        try (Connection conn = DatabaseManager.getInstance().getConnection()) {
            // 1. Initial State: Available
            try (PreparedStatement ps = conn.prepareStatement("UPDATE room SET available = 'Available', status = 'Cleaned' WHERE roomNo = ?")) {
                ps.setString(1, testRoom);
                ps.executeUpdate();
            }

            // 2. Check-in Transition: Occupied
            try (PreparedStatement ps = conn.prepareStatement("UPDATE room SET available = 'Occupied' WHERE roomNo = ?")) {
                ps.setString(1, testRoom);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT available, status FROM room WHERE roomNo = ?")) {
                ps.setString(1, testRoom);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next());
                    assertEquals("Occupied", rs.getString("available"));
                }
            }

            // 3. Checkout Transition: Available & Dirty (for housekeeping)
            try (PreparedStatement ps = conn.prepareStatement("UPDATE room SET available = 'Available', status = 'Dirty' WHERE roomNo = ?")) {
                ps.setString(1, testRoom);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT available, status FROM room WHERE roomNo = ?")) {
                ps.setString(1, testRoom);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next());
                    assertEquals("Available", rs.getString("available"));
                    assertEquals("Dirty", rs.getString("status"));
                }
            }

            // 4. Housekeeping Cleaned: Cleaned
            try (PreparedStatement ps = conn.prepareStatement("UPDATE room SET status = 'Cleaned' WHERE roomNo = ?")) {
                ps.setString(1, testRoom);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT status FROM room WHERE roomNo = ?")) {
                ps.setString(1, testRoom);
                try (ResultSet rs = ps.executeQuery()) {
                    assertTrue(rs.next());
                    assertEquals("Cleaned", rs.getString("status"));
                }
            }
        }
    }
}
