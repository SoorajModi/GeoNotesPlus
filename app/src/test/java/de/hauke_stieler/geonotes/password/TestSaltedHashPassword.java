package de.hauke_stieler.geonotes.password;

import de.hauke_stieler.geonotes.password.SaltedHashPassword;

import static junit.framework.TestCase.assertEquals;

/**
 * Test for the class SaltedHashPassword
 * @author Xiaotian Dai
 * @date - Feb.11, 2021
 */
public class TestSaltedHashPassword {
    public static void main(String[] args) {
        SaltedHashPassword s = new SaltedHashPassword();
        /**
         * 	Password	Salt				Salted_Hash_Password
         * 	admin123	C78CED15EDD3A8CC	1C9D79B8AAC78E03D991955F9EEAD7DDB431EA7B885CDBC8
         */
        String password = "admin123";
        String salt = "C78CED15EDD3A8CC";
        String result = "1C9D79B8AAC78E03D991955F9EEAD7DDB431EA7B885CDBC8";
        assertEquals(s.getSaltedHashPassword(password, salt),  result);
    }
}
