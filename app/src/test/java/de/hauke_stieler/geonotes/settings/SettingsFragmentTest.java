package de.hauke_stieler.geonotes.settings;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SettingsFragmentTest {
    @Test
    public void should_construct_settings_activity() {
        SettingsFragment settingsFragment = new SettingsFragment();
        assertNotNull(settingsFragment);
    }
}
