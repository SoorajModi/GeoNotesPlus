package de.hauke_stieler.geonotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import de.hauke_stieler.geonotes.notes.Note;
import de.hauke_stieler.geonotes.notes.NoteAdapter;
import de.hauke_stieler.geonotes.notes.NoteStore;

/*
 * Activity class for listing user notes
 */
public class ListNotesActivity extends AppCompatActivity {
    private NoteStore noteStore;
    private ArrayAdapter<Note> arrayAdapter;
    private Button btn_sort;
    private boolean is_desc_order = true;

    /**
     * Create List Notes Page
     *
     * @param savedInstanceState - instance of the app
     */
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get all notes from the database
        final Context context = getApplicationContext();
        noteStore = new NoteStore(context);

        List<Note> list = noteStore.getAllNotes(is_desc_order);
        for (Note n : list) {
            System.out.print("Lat: " + n.lat + "  Lon: " + n.lon + " Desc: " + n.description + "  ");
            System.out.println("Date: " + n.date);
        }

        sortNotes(is_desc_order);
    }

    /**
     * Sort user notes
     *
     * @param is_desc_order - true/false if notes should be sorted in descending order
     */
    public void sortNotes(boolean is_desc_order) {
        List<Note> list = noteStore.getAllNotes(is_desc_order);

        // Display notes from database in app
        arrayAdapter = new NoteAdapter(this, (ArrayList) list);
        ListView lv = (ListView) findViewById(R.id.note_list);
        lv.setAdapter(arrayAdapter);
    }

    /**
     * Create Options menu
     *
     * @param menu - options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_note_menu, menu);
        return true;
    }

    /**
     * When an item in the menu is selected
     *
     * @param item - menu item selected
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_notes:
                is_desc_order = !is_desc_order;
                sortNotes(is_desc_order);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Finish activity
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
