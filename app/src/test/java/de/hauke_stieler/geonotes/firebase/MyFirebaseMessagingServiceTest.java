package de.hauke_stieler.geonotes.firebase;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MyFirebaseMessagingServiceTest {
    @Test
    public void should_construct_settings_activity() {
        MyFirebaseMessagingService service = new MyFirebaseMessagingService();
        assertNotNull(service);
    }
}
