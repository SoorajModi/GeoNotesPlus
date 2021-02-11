package de.hauke_stieler.geonotes.notes;

import android.net.Uri;

public class Note {
    enum MediaType {
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

    public Note(long id, String description, double lat, double lon, Uri mediaURI, MediaType mediaType) {
        this.id = id;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.mediaURI = mediaURI;
        this.mediaType = mediaType;
    }
}
