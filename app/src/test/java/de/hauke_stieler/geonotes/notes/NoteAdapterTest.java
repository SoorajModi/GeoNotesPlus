package de.hauke_stieler.geonotes.notes;

import android.content.Context;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

public class NoteAdapterTest {
    @Mock
    Context mockContext;

    @Test
    public void should_construct_settings_activity() {
        ArrayList<Note> notes = new ArrayList<Note>();
        notes.add(new Note(1234567890, "description", 43.653225, -79.383186, Note.MediaType.NULL, null, "2021-3-11 21:23"));

        NoteAdapter adapter = new NoteAdapter(mockContext, notes);

        assertNotNull(adapter);
    }
}
