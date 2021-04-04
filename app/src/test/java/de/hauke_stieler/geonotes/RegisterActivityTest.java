package de.hauke_stieler.geonotes;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RegisterActivityTest {
    @Test
    public void should_construct_register_activity() {
        RegisterActivity activity = new RegisterActivity();
        assertNotNull(activity);
    }
}
