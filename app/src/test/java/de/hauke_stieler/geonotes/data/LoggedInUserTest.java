package de.hauke_stieler.geonotes.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoggedInUserTest {
    private LoggedInUser user;

    @Before
    public void before() {
        user = new LoggedInUser("id", "name");
    }

    @Test
    public void should_construct_logged_in_user() {
        assertNotNull(user);
    }

    @Test
    public void should_return_user_id() {
        assertEquals("id", user.getUserId());
    }

    @Test
    public void should_return_display_name() {
        assertEquals("name", user.getDisplayName());
    }
}