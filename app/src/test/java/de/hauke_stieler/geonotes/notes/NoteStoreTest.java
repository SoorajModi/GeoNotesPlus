package de.hauke_stieler.geonotes;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.content.Context;
import android.os.PowerManager;

import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

import androidx.test.core.app.ApplicationProvider;

import de.hauke_stieler.geonotes.notes.NoteStore;

//@RunWith(MockitoJUnitRunner.class)
public class NoteStoreTest {
//    private Context context = ApplicationProvider.getApplicationContext();

    @Mock
    Context mockContext;

    @Test
    public void should_construct_note_store() {
        NoteStore ns = new NoteStore(mockContext);
        assertNotNull(ns);
    }
}