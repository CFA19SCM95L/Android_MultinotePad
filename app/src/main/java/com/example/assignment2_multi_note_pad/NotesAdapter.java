package com.example.assignment2_multi_note_pad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "NotesAdapter";
    private List<Notes> notesList;
    private MainActivity mainAct;

    public NotesAdapter(List<Notes> notesList, MainActivity ma) {
        this.notesList = notesList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_note, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Notes note = notesList.get(position);

        holder.title.setText(note.getTitle());
        if (note.getContent().length() > 80) {
            holder.content.setText(note.getContent().substring(0,81)+ "...");
        } else {
            holder.content.setText(note.getContent());
        }
        holder.time.setText(note.getDate());

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
