package de.hauke_stieler.geonotes;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MainActivityTest {
    @Test
    public void should_construct_main_activity() {
        MainActivity activity = new MainActivity();
        assertNotNull(activity);
    }
}