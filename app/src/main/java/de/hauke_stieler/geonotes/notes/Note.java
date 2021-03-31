package de.hauke_stieler.geonotes.notes;

import android.net.Uri;

/**
 * Class for user notes
 */
public class Note {
    public enum MediaType {
        NULL,
        IMAGE,
        AUDIO
    }

    public final long id;
    public final String description;
    public final double lat;
    public final double lon;
    public final Uri mediaURI;
    public final MediaType mediaType;
    public final String date;


    /**
     * Constructor to create an instance of note
     *
     * @param id          - note id
     * @param description - description of note
     * @param lat         - latitude of note
     * @param lon         - longitude of note
     * @param mediaType   - type of note, either text (NULL), image, or audio
     * @param mediaURI    - media URI
     * @param date        - date note was created
     */
    public Note(long id, String description, double lat, double lon, MediaType mediaType, Uri mediaURI, String date) {
        this.id = id;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.mediaType = mediaType;
        this.mediaURI = mediaURI;
        this.date = date;
    }
}
