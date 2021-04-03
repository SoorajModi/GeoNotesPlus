package de.hauke_stieler.geonotes;

import org.junit.Test;
import static org.junit.Assert.*;

public class SettingsActivityTest {
    @Test
    public void should_construct_settings_activity() {
        SettingsActivity setting = new SettingsActivity();
        assertNotNull(setting);
    }
}