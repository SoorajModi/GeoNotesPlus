package de.hauke_stieler.geonotes.notes;

import android.content.Context;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;

public class NoteStoreTest {
    @Mock
    Context mockContext;

    @Test
    public void should_construct_note_store() {
        NoteStore ns = new NoteStore(mockContext);
        assertNotNull(ns);
    }
}