package de.hauke_stieler.geonotes;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SettingsActivityTest {
    @Test
    public void should_construct_settings_activity() {
        SettingsActivity activity = new SettingsActivity();
        assertNotNull(activity);
    }
}