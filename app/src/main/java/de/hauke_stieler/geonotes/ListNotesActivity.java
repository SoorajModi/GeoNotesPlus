package de.hauke_stieler.geonotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hauke_stieler.geonotes.notes.Note;
import de.hauke_stieler.geonotes.notes.NoteAdapter;
import de.hauke_stieler.geonotes.notes.NoteStore;

public class ListNotesActivity extends AppCompatActivity {
    private NoteStore noteStore;
    private ArrayAdapter<Note> arrayAdapter;
    private Button btn_sort;
    private boolean is_desc_order = true;

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

        List<Note> list = noteStore.getAllNotes(is_desc_order);
        for (Note n : list) {
            System.out.print("Lat: " + n.lat + "  Lon: " + n.lon + " Desc: " + n.description + "  ");
//            System.out.println("Date: " + n.date);
        }

        // Display notes from database in app
        arrayAdapter = new NoteAdapter(this, (ArrayList)list);
        ListView lv = (ListView)findViewById(R.id.note_list);
        lv.setAdapter(arrayAdapter);

        btn_sort = (Button)findViewById(R.id.btn_sort);
        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_desc_order = !is_desc_order;

                if(is_desc_order) {
                    Collections.sort(list, new Comparator<Note>() {
                        @Override
                        public int compare(Note o1, Note o2) {
                            if(o1.date.compareTo(o2.date) == 0) {
                                return 0;
                            } else {
                                return o1.date.compareTo(o2.date) > 0 ? 1 : -1;
                            }
                        }
                    });
                } else {
                    Collections.sort(list, new Comparator<Note>() {
                        @Override
                        public int compare(Note o1, Note o2) {
                            if(o1.date.compareTo(o2.date) == 0) {
                                return 0;
                            } else {
                                return o1.date.compareTo(o2.date) < 0 ? 1 : -1;
                            }
                        }
                    });
                }

                // Display notes from database in app
                arrayAdapter = new NoteAdapter(ListNotesActivity.this, (ArrayList)list);
                ListView lv = (ListView)findViewById(R.id.note_list);
                lv.setAdapter(arrayAdapter);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
