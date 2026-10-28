package hotel.reservation.system;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Security utility for cryptographic hashing and password verification
 * using SHA-256 with a unique random salt per password.
 */
public class SecurityUtils {

    private static final int SALT_LENGTH = 16;

    /**
     * Hashes a plaintext password with a freshly generated random salt.
     * Stored format: <salt_base64>$<hash_base64>
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        byte[] hash = computeHash(password, salt);

        String saltStr = Base64.getEncoder().encodeToString(salt);
        String hashStr = Base64.getEncoder().encodeToString(hash);
        return saltStr + "$" + hashStr;
    }

    /**
     * Verifies an entered password against a stored hashed string.
     * Also supports backwards-compatible verification for legacy plaintext entries.
     */
    public static boolean verifyPassword(String enteredPassword, String storedHash) {
        if (enteredPassword == null || storedHash == null) {
            return false;
        }

        // Check if stored hash is in salted format (contains '$')
        if (storedHash.contains("$")) {
            String[] parts = storedHash.split("\\$");
            if (parts.length != 2) {
                return false;
            }
            try {
                byte[] salt = Base64.getDecoder().decode(parts[0]);
                byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
                byte[] actualHash = computeHash(enteredPassword, salt);
                return MessageDigest.isEqual(expectedHash, actualHash);
            } catch (Exception e) {
                return false;
            }
        }

        // Backwards compatibility for legacy plaintext records
        return enteredPassword.equals(storedHash);
    }

    private static byte[] computeHash(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            return md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm unavailable", e);
        }
    }
}
