package de.hauke_stieler.geonotes;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ListNotesActivityTest {
    @Test
    public void should_construct_settings_activity() {
        ListNotesActivity activity = new ListNotesActivity();
        assertNotNull(activity);
    }
}
