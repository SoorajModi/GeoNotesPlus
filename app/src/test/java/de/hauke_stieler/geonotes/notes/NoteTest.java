package de.hauke_stieler.geonotes.notes;

import org.junit.Before;
import org.junit.Test;

import de.hauke_stieler.geonotes.notes.Note.MediaType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NoteTest {
    private Note note;

    @Before
    public void before() {
        note = new Note(1234567890, "description", 43.653225, -79.383186, MediaType.NULL, null, "2021-3-11 21:23");
    }

    @Test
    public void should_construct_note() {
        assertNotNull(note);
    }

    @Test
    public void should_set_note_id() {
        assertEquals(1234567890, note.id);
    }

    @Test
    public void should_set_note_description() {
        assertEquals("description", note.description);
    }

    @Test
    public void should_set_note_latitude() {
        //        assertEquals(43.653225, note.lat);
        assertTrue(43.653225 == note.lat);
    }

    @Test
    public void should_set_note_longitude() {
        //        assertEquals(-79.383186, note.lon);
        assertTrue(-79.383186 == note.lon);
    }

    @Test
    public void should_set_the_note_date() {
        assertEquals("2021-3-11 21:23", note.date);
    }

    @Test
    public void should_set_media_types() {
        Note imgNote = new Note(1234567890, "description", 43.653225, -79.383186, MediaType.IMAGE, null, "2021-3-11 21:23");
        Note audioNote = new Note(1234567890, "description", 43.653225, -79.383186, MediaType.AUDIO, null, "2021-3-11 21:23");

        assertEquals(MediaType.NULL, note.mediaType);
        assertEquals(MediaType.IMAGE, imgNote.mediaType);
        assertEquals(MediaType.AUDIO, audioNote.mediaType);
    }
}