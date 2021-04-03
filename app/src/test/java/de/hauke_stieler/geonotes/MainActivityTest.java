package de.hauke_stieler.geonotes;

import android.app.Activity;
import android.os.Bundle;

import org.junit.Test;
import static org.junit.Assert.*;
//import android.os.Bundle;

import de.hauke_stieler.geonotes.MainActivity;

//import org.mockito.Mock;
//import static org.mockito.Mockito.when;

public class MainActivityTest {
//    @Mock
//    Bundle bundle;

    @Test
    public void should_construct_main_activity() {
        MainActivity main = new MainActivity();
        assertNotNull(main);
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