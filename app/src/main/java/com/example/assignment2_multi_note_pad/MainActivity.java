package com.example.assignment2_multi_note_pad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private List<Notes> noteList;
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private boolean create;
    private int changePos;

    private static final int ADD_REQUEST_CODE = 101;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteList = new ArrayList<>();
        //test
        noteList = loadFile();
        //////

        titleNum(noteList.size());

        recyclerView = findViewById(R.id.recycler);
        notesAdapter = new NotesAdapter(noteList, this);

        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //
        notesAdapter.notifyDataSetChanged();
        //


    }



    @Override
    protected void onPause() {

        //test
        saveList();
        ///////

        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_manu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.addMenu:
                create = true;
                Intent intent = new Intent(this, EditActivity.class);
                startActivityForResult(intent, ADD_REQUEST_CODE);
                return true;

            case R.id.infoMenu:
                Intent intent2 = new Intent(this, AboutActivity.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String title = data.getStringExtra("USER_TEXT_Title");
                String content = data.getStringExtra("USER_TEXT_Content");

                if (create) {
                    Notes note = new Notes(title, content);

                    noteList.add(0, note);
                    notesAdapter.notifyDataSetChanged();
                    titleNum(noteList.size());
                } else {
                    if (noteList.get(changePos).getTitle().equals(title) && noteList.get(changePos).getContent().equals(content)) {
                        Toast.makeText(this,"Note is not changed", Toast.LENGTH_SHORT).show();

                    } else {
                        Notes note = new Notes(title, content);

                        noteList.remove(changePos);
                        noteList.add(0, note);
                        notesAdapter.notifyDataSetChanged();

                    }

                }

            } else {
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }

        } else {
            Log.d(TAG, "onActivityResult: Request Code " + requestCode);
        }
    }

    @Override
    public void onClick(View v) {
        create = false;
        Intent intent = new Intent(this, EditActivity.class);
        changePos = recyclerView.getChildAdapterPosition(v);
        intent.putExtra("title", noteList.get(changePos).getTitle());
        intent.putExtra("content", noteList.get(changePos).getContent());

        startActivityForResult(intent, ADD_REQUEST_CODE);


    }

//    @Override
//    public boolean onLongClick(View v) {
//
//        if (!noteList.isEmpty()) {
//            int pos = recyclerView.getChildLayoutPosition(v);
//            noteList.remove(pos);
//            notesAdapter.notifyDataSetChanged();
//            titleNum(noteList.size());
//        }
//
//        return false;
//    }

    @Override
    public boolean onLongClick(View v) {

        final int pos = recyclerView.getChildLayoutPosition(v);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete note '" + noteList.get(pos).getTitle() +"'?");

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (!noteList.isEmpty()) {
                    noteList.remove(pos);
                    notesAdapter.notifyDataSetChanged();
                    titleNum(noteList.size());
                }
            }
        });
        builder.show();

        return false;
    }




    private void titleNum(int numNote) {
        setTitle(getString(R.string.app_name) + "(" + numNote + ")");
    }


    private List loadFile() {
        List<Notes> notesList = new ArrayList<>();
        try {
            InputStream is = getApplicationContext().
                    openFileInput(getString(R.string.file_name));

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }


            JSONArray jsonArray = new JSONArray(sb.toString());


            for (int i = 0 ; i < jsonArray.length();i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                String title = obj.getString("title");
                String content = obj.getString("content");
                String date = obj.getString("date");

                Notes notes = new Notes(title,content);
                notes.setDate(date);
                notesList.add(notes);
                Log.d(TAG, "loadList: Load JSON File" + notes.getTitle() + content + date);

            }


        } catch (FileNotFoundException e) {
            Toast.makeText(this, "No JSON File Present", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notesList;
    }
//
    private void saveList() {

        Log.d(TAG, "saveList: Saving JSON File");
        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, "UTF-8"));
            writer.setIndent("  ");
            writer.beginArray();
            for (int i = 0; i < noteList.size(); i++) {
                writer.beginObject();

                writer.name("title").value(noteList.get(i).getTitle());
                writer.name("content").value(noteList.get(i).getContent());
                writer.name("date").value(noteList.get(i).getDate());
                writer.endObject();

            }
            writer.endArray();
            writer.close();

            ////

            StringWriter sw = new StringWriter();
            writer = new JsonWriter(sw);
            writer.setIndent("  ");
            writer.beginArray();
            for (int i = 0; i < noteList.size(); i++) {
                writer.beginObject();

                writer.name("title").value(noteList.get(i).getTitle());
                writer.name("content").value(noteList.get(i).getContent());
                writer.name("date").value(noteList.get(i).getDate());
                writer.endObject();

            }
            writer.endArray();
            writer.close();
            Log.d(TAG, "saveProduct: JSON:\n" + sw.toString());


        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
