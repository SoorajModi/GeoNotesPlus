package de.hauke_stieler.geonotes;

import org.junit.Test;

import static org.junit.Assert.*;

import android.net.Uri;

import de.hauke_stieler.geonotes.notes.Note;
import de.hauke_stieler.geonotes.notes.Note.MediaType;

public class NoteTest {
    @Test
    public void should_construct_note() {
        Note note = new Note(1234567890, "description", 43.653225, -79.383186, MediaType.IMAGE, null);

        assertEquals(1234567890, note.id);
        assertEquals("description", note.description);
//        assertEquals(43.653225, note.lat);
        assertTrue(43.653225 == note.lat);
//        assertEquals(-79.383186, note.lon);
        assertTrue(-79.383186 == note.lon);
        assertEquals(MediaType.IMAGE, note.mediaType);
        assertNull(note.mediaURI);
    }
}