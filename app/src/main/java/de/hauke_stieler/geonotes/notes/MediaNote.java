package de.hauke_stieler.geonotes.notes;

import android.net.Uri;

public class MediaNote extends Note {
    enum MediaType {
        NULL,
        IMAGE,
        AUDIO
    }

    public final Uri mediaURI;
    public final MediaType mediaType;

    public MediaNote(long id, String description, double lat, double lon, Uri mediaURI, MediaType mediaType) {
        super(id, description, lat, lon);
        this.mediaURI = mediaURI;
        this.mediaType = mediaType;
    }
}
