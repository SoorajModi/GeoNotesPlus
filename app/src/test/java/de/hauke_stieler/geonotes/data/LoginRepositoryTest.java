package de.hauke_stieler.geonotes.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LoginRepositoryTest {
    LoginDataSource loginData;

    @Before
    public void before() {
        loginData = new LoginDataSource();
    }

    @Test
    public void should_construct_settings_activity() {
        assertNotNull(LoginRepository.getInstance(loginData));
    }

    @Test
    public void should_return_false_if_user_not_logged_in() {
        assertFalse(LoginRepository.getInstance(loginData).isLoggedIn());
    }

    @Test
    public void should_login_user() {
        Result res = LoginRepository.getInstance(loginData).login("username", "password");

        assertTrue(res.toString().startsWith("Success"));
        assertTrue(LoginRepository.getInstance(loginData).isLoggedIn());
    }
}
