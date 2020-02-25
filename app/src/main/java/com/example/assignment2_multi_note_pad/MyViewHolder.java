package com.example.assignment2_multi_note_pad;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView content;
    public TextView time;


    public MyViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.title_list);
        content = view.findViewById(R.id.content_list);
        time = view.findViewById(R.id.time);
    }
}
