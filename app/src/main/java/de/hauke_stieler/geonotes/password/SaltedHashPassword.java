package de.hauke_stieler.geonotes.password;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 * A utility class for salted hash password
 * @author - Xiaotian Dai
 * @date - Feb.11, 2021
 */
public class SaltedHashPassword {
    // algorithm used: SHA-256
    private final String ALGORITHM_NAME = "SHA-256";

    /**
     * convert byte array to hex string
     * @param bytes - byte array
     * @return - hex string
     */
    private String byteToHex(byte[] bytes) {
        String hex = "";
        for(int i = 0; i < bytes.length; i++) {
            String temp = Integer.toHexString(bytes[i] & 0xff);
            if(temp.length() == 1) {
                hex += "0";
            }
            hex += temp;
        }
        return hex;
    }

    /**
     * convert a hex string to byte array
     * @param hex - hex string
     * @return - byte array
     */
    private byte[] hexToByte(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return bytes;
    }

    /**
     * get salted in hex form
     * @return - a hex string represents the salt
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    public String getSalt() {
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return byteToHex(salt);
    }

    /**
     * generate a secure salted hash password
     * @param password - password to be salted
     * @param salt - salt to be used
     * @return - salted hash password
     * @throws NoSuchAlgorithmException
     */
    public String getSaltedPassword(String password, String salt) {
        String saltedPassword = "";
        byte[] s = hexToByte(salt);

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(ALGORITHM_NAME);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // add salt to digest
        md.update(s);
        // get password in byte form
        byte[] bytes = md.digest(password.getBytes());

        for(int i = 0; i < bytes.length; i++) {
            saltedPassword += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return saltedPassword;
    }
}