package de.hauke_stieler.geonotes.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class to handle adding/retrieving/deleting/modifying notes in DB
 */
public class NoteStore extends SQLiteOpenHelper {
    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "geonotes";

    private static final String NOTES_TABLE_NAME = "notes";
    private static final String NOTES_COL_ID = "id";
    private static final String NOTES_COL_LAT = "lat";
    private static final String NOTES_COL_LON = "lon";
    private static final String NOTES_COL_DESCRIPTION = "description";
    private static final String NOTES_COL_MEDIATYPE = "mediaType";
    private static final String NOTES_COL_MEDIAURI = "mediaURI";
    private static final String NOTES_COL_DATE = "date";

    /**
     * Generate a note store to hold all the user notes
     *
     * @param context - application context
     */
    public NoteStore(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Creates SQLite table if it does not already exist
     *
     * @param db - SQLite database instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(%s INTEGER PRIMARY KEY, %s DOUBLE NOT NULL, %s DOUBLE NOT NULL, %s VARCHAR NOT NULL, %s INTEGER, %s TEXT, %s VARCHAR NOT NULL);",
                NOTES_TABLE_NAME,
                NOTES_COL_ID,
                NOTES_COL_LAT,
                NOTES_COL_LON,
                NOTES_COL_DESCRIPTION,
                NOTES_COL_MEDIATYPE,
                NOTES_COL_MEDIAURI,
                NOTES_COL_DATE));
    }

    /**
     * Upgrades version
     *
     * @param db         - SQLite database instance
     * @param oldVersion - current version
     * @param newVersion - new version to upgrade to
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", NOTES_TABLE_NAME));

        // Recreate
        onCreate(db);

        Log.i("NoteStore", String.format("onUpgrade: from version %d to version %d", oldVersion, newVersion));
    }

    /**
     * Will add a note to the DB
     *
     * @param note - note to add to DB
     * @return - row id of newly inserted record, -1 if insertion fails
     */
    public long addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTES_COL_LAT, note.lat);
        values.put(NOTES_COL_LON, note.lon);
        values.put(NOTES_COL_DESCRIPTION, note.description);
        values.put(NOTES_COL_MEDIATYPE, note.mediaType.ordinal());
        values.put(NOTES_COL_MEDIAURI, note.mediaURI.toString());
        values.put(NOTES_COL_DATE, note.date);

        return db.insert(NOTES_TABLE_NAME, null, values);
    }

    /**
     * Updates description of a note
     *
     * @param id             - id of note to be updated
     * @param newDescription - new description of note
     */
    public void updateDescription(long id, String newDescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String date_str = formatter.format(date);

        ContentValues values = new ContentValues();
        values.put(NOTES_COL_ID, id);
        values.put(NOTES_COL_DESCRIPTION, newDescription);
        values.put(NOTES_COL_DATE, date_str);

        db.update(NOTES_TABLE_NAME, values, NOTES_COL_ID + " = ?", new String[]{"" + id});
    }
    /**
     * Updates geolocation of a note
     *
     * @param id       - id of note to be updated
     * @param location - new location of note
     */
    public void updateLocation(long id, GeoPoint location) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String date_str = formatter.format(date);

        ContentValues values = new ContentValues();
        values.put(NOTES_COL_ID, id);
        values.put(NOTES_COL_LAT, location.getLatitude());
        values.put(NOTES_COL_LON, location.getLongitude());
        values.put(NOTES_COL_DATE, date_str);

        db.update(NOTES_TABLE_NAME, values, NOTES_COL_ID + " = ?", new String[]{"" + id});
    }

    // TODO: Add updateMedia function??

    /**
     * Removes note from DB
     *
     * @param id - id of note to be removed
     */
    public void removeNote(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(NOTES_TABLE_NAME, NOTES_COL_ID + " = ?", new String[]{"" + id});
    }


    /**
     * Gets all notes stored in the DB
     *
     * @param is_desc_order - true/false if the order of the notes should be descending
     * @return - list of all notes stored in DB
     */
    public List<Note> getAllNotes(boolean is_desc_order) {
        SQLiteDatabase db = this.getReadableDatabase();
        String order = "";
        if (is_desc_order) {
            order = "datetime(date) desc";
        } else {
            order = "datetime(date) asc";
        }
        Cursor cursor = db.query(NOTES_TABLE_NAME,
                new String[]{NOTES_COL_ID, NOTES_COL_DESCRIPTION, NOTES_COL_LAT, NOTES_COL_LON, NOTES_COL_MEDIATYPE, NOTES_COL_MEDIAURI, NOTES_COL_DATE},
                null, null, null, null, order
        );

        List<Note> notes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                notes.add(new Note(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getDouble(3),
                        Note.MediaType.values()[cursor.getInt(4)],
                        Uri.parse(cursor.getString(5)),
                        cursor.getString(6)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return notes;
    }
}
