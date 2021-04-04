package de.hauke_stieler.geonotes.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LoginDataSourceTest {

    LoginDataSource loginData;

    @Before
    public void before() {
        loginData = new LoginDataSource();
    }

    @Test
    public void should_construct_login_data_source() {
        assertNotNull(loginData);
    }

    @Test
    public void should_login_user() {
        Result actual = loginData.login("Jane Doe", "password");
        assertTrue(actual.toString().startsWith("Success"));
    }
}
