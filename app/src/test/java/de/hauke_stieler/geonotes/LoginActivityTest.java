package de.hauke_stieler.geonotes;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class LoginActivityTest {
    @Test
    public void should_construct_settings_activity() {
        LoginActivity activity = new LoginActivity();
        assertNotNull(activity);
    }
}
