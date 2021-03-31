package de.hauke_stieler.geonotes.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hauke_stieler.geonotes.R;

public class NoteAdapter extends ArrayAdapter<Note> {
    public NoteAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Note note = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
        }

        // Lookup view for data population
        TextView noteDesc = (TextView) convertView.findViewById(R.id.noteDescription);
        TextView noteLoc = (TextView) convertView.findViewById(R.id.noteLocation);
        TextView noteDate = (TextView) convertView.findViewById(R.id.noteDate);

        // Populate the data into the template view using the data object
        noteDesc.setText(note.description);
        noteLoc.setText(String.format("Location: %s, %s", note.lat, note.lon));
        noteDate.setText(String.format("Date: %s", note.date));

        // Return the completed view to render on screen
        return convertView;
    }
}
