package de.hauke_stieler.geonotes;

import de.hauke_stieler.geonotes.password.SaltedHashPassword;

/**
 * Test for the class SaltedHashPassword
 * @author Xiaotian Dai
 * @date - Feb.11, 2021
 */
public class TestSaltedHashPassword {
    public static void main(String[] args) {
        SaltedHashPassword s = new SaltedHashPassword();

        String password = "admin123";
        String salt = s.getSalt();
        System.out.println("password: " + password);
        System.out.println("salt: " + salt);
        System.out.println("salted password: " + s.getSaltedPassword(password, salt));
    }
}
