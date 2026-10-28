package hotel.reservation.system;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SecurityUtilsTest {

    @Test
    void testPasswordHashingAndVerification() {
        String rawPassword = "Admin@123";
        String hashed = SecurityUtils.hashPassword(rawPassword);

        assertNotNull(hashed);
        assertTrue(hashed.contains("$"), "Hash should contain salt separator");
        assertNotEquals(rawPassword, hashed);

        // Verification success
        assertTrue(SecurityUtils.verifyPassword(rawPassword, hashed));

        // Wrong password fails
        assertFalse(SecurityUtils.verifyPassword("WrongPassword", hashed));
        assertFalse(SecurityUtils.verifyPassword("", hashed));
        assertFalse(SecurityUtils.verifyPassword(null, hashed));
    }

    @Test
    void testUniqueSaltGeneratesDifferentHashes() {
        String password = "SecretPassword123";
        String hash1 = SecurityUtils.hashPassword(password);
        String hash2 = SecurityUtils.hashPassword(password);

        assertNotEquals(hash1, hash2, "Each hash should use a unique random salt");
        assertTrue(SecurityUtils.verifyPassword(password, hash1));
        assertTrue(SecurityUtils.verifyPassword(password, hash2));
    }

    @Test
    void testLegacyPlaintextCompatibility() {
        // Backwards compatibility for legacy plain text passwords in existing database
        String legacyPlaintext = "12345";
        assertTrue(SecurityUtils.verifyPassword("12345", legacyPlaintext));
        assertFalse(SecurityUtils.verifyPassword("54321", legacyPlaintext));
    }
}
