package de.hauke_stieler.geonotes;

import android.app.Activity;
import android.os.Bundle;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MainActivityTest {
    @Test
    public void should_construct_main_activity() {
        MainActivity activity = new MainActivity();
        assertNotNull(activity);
    }

//    @Test
//    public void foo() {
//        assertTrue(true);
//        MainActivity scenario = new MainActivity();
//        Bundle bundle = new Bundle();
//
//        scenario.onCreate(bundle);
//    }
}