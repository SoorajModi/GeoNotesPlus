package de.hauke_stieler.geonotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import de.hauke_stieler.geonotes.notes.Note;
import de.hauke_stieler.geonotes.notes.NoteAdapter;
import de.hauke_stieler.geonotes.notes.NoteStore;

public class ListNotesActivity extends AppCompatActivity {
    private NoteStore noteStore;
    private ArrayAdapter<Note> arrayAdapter;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get all notes from the database
        final Context context = getApplicationContext();
        noteStore = new NoteStore(context);
        List<Note> list = noteStore.getAllNotes();
        for (Note n : list) {
            System.out.print("Lat: " + n.lat + "  Lon: " + n.lon + " Desc: " + n.description + "  ");
//            System.out.println("Date: " + n.date);
        }

        // Display notes from database in app
        arrayAdapter = new NoteAdapter(this, (ArrayList)list);
        ListView lv = (ListView)findViewById(R.id.note_list);
        lv.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
