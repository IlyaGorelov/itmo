package Objects.Helpers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {
    private static final int SALT_LENGTH = 16;
    private static final String PEPPER = "lab7_pepper";

    private static final SecureRandom secureRandom = new SecureRandom();

    public static PasswordData hashPassword(String password) {
        byte[] salt = generateSalt();
        byte[] hash = sha512(password, salt);

        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        String hashBase64 = Base64.getEncoder().encodeToString(hash);

        return new PasswordData(hashBase64, saltBase64);
    }

    private static byte[] sha512(String password, byte[] salt) {
        try {
            String pepper = getPepper();

            MessageDigest digest = MessageDigest.getInstance("SHA-512");

            digest.update(salt);
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            digest.update(pepper.getBytes(StandardCharsets.UTF_8));

            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 algorithm not found", e);
        }
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private static String getPepper() {
        String pepper = System.getenv(PEPPER);

        if (pepper == null || pepper.isBlank()) {
            throw new IllegalStateException(PEPPER + " environment variable is not set");
        }

        return pepper;
    }

    public record PasswordData(String hash, String salt) {
    }

    public static boolean checkPassword(String password, String storedHashPassword, String storedSalt) {
        byte[] salt = Base64.getDecoder().decode(storedSalt);
        byte[] newHash = sha512(password, salt);

        byte[] storedHash = Base64.getDecoder().decode(storedHashPassword);

        return MessageDigest.isEqual(storedHash, newHash);
    }
}
