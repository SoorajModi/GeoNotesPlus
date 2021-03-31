package de.hauke_stieler.geonotes.password;

import java.security.MessageDigest;
import java.util.Random;

/**
 * A utility class for salted hash password
 *
 * @author - Xiaotian Dai
 * @date - Feb.11, 2021
 */
public class SaltedHashPassword {
    // hex digits
    private final char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private final String ENCRYPTION = "MD5";        // MD5 used for salted

    /**
     * Generate a 16-digit random hex string as salt
     *
     * @return - salt
     */
    public String generateSalt() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < sb.capacity(); i++) {
            sb.append(hex[random.nextInt(16)]);
        }
        return sb.toString();
    }

    /**
     * Generate a salted hash password
     * MD5 algorithm is used to encrypt
     *
     * @param password - origin password
     * @param salt     - salt used for encryption
     * @return - salted hash password
     */
    public String getSaltedHashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(ENCRYPTION);
            // String salt = generateSalt();
            // concatenate password with salt
            String saltedPassword = password + salt;
            // get the hash value for salted password
            String hash = byteToHex(md.digest(saltedPassword.getBytes()));

            // insert salt into hash value, used for authentication when login
            char[] saltedHash = new char[48];
            // insert a salt digit between every two hash digits
            for (int i = 0; i < 48; i += 3) {
                saltedHash[i] = hash.charAt(i / 3 * 2);
                saltedHash[i + 1] = salt.charAt(i / 3);
                saltedHash[i + 2] = hash.charAt(i / 3 * 2 + 1);
            }
            return new String(saltedHash);
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * Convert a byte array into a hex string
     *
     * @param bytes - byte array
     * @return - hex string
     */
    public String byteToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(hex[aByte >>> 3 & 0xf]);
            sb.append(hex[aByte & 0xf]);
        }
        return sb.toString();
    }

    /**
     * Get salt from stored hash value
     *
     * @param hash - stored hash value
     * @return - salt
     */
    public String getSaltFromHash(String hash) {
        StringBuilder sb = new StringBuilder();
        char[] h = hash.toCharArray();
        for (int i = 0; i < hash.length(); i += 3) {
            sb.append(h[i + 1]);
        }
        return sb.toString();
    }
}