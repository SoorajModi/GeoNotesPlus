package de.hauke_stieler.geonotes.password;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Test for the class SaltedHashPassword
 *
 * @author Xiaotian Dai
 * @date - Feb.11, 2021
 */
public class SaltedHashPasswordTest {
    /*
     * 	Password	Salt				Salted_Hash_Password
     * 	admin123	C78CED15EDD3A8CC	1C9D79B8AAC78E03D991955F9EEAD7DDB431EA7B885CDBC8
     */
    private SaltedHashPassword s;
    private final String salt = "C78CED15EDD3A8CC";
    private final String result = "1C9D79B8AAC78E03D991955F9EEAD7DDB431EA7B885CDBC8";

    @Before
    public void before() {
        s = new SaltedHashPassword();
    }

    @Test
    public void should_salt_and_hash_string() {
        String password = "admin123";

        assertEquals(s.getSaltedHashPassword(password, salt), result);
    }

    @Test
    public void should_generate_salt() {
        String salt = s.generateSalt();
        assertEquals(16, salt.length());
    }

    @Test
    public void should_convert_byte_array_to_hex_string() {
        byte[] bytes = { 1, 2, 3, 4, 5, 6, 7, 8 };

        String hex = s.byteToHex(bytes);

        assertEquals("0102030405060718", hex);
    }

    @Test
    public void should_get_salt_from_hash() {
        assertEquals(salt, s.getSaltFromHash(result));
    }
}
