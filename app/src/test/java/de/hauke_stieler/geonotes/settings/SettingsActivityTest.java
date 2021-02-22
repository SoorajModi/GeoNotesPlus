package de.hauke_stieler.geonotes;

import org.junit.Test;
import static org.junit.Assert.*;

import de.hauke_stieler.geonotes.settings.SettingsActivity;

public class SettingsActivityTest {
    @Test
    public void should_construct_settings_activity() {
        SettingsActivity setting = new SettingsActivity();
        assertNotNull(setting);
    }
}