package com.example.assignment2_multi_note_pad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";

    private EditText title;
    private EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        title = findViewById(R.id.title);
        title.setMovementMethod(new ScrollingMovementMethod());
        content = findViewById(R.id.content);
        content.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = getIntent();
        if (intent.hasExtra("title") || intent.hasExtra("content")) {
            String titleS = intent.getStringExtra("title");
            String contentS = intent.getStringExtra("content");

            title.setText(titleS);
            content.setText(contentS);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_manu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.saveMenu:

                if (title.getText().toString().length() == 0) {
                    toast();
                    //
                    finish();
                    //
                    return false;
                }
                Intent data = new Intent(); // Used to hold results data to be returned to original activity
                data.putExtra("USER_TEXT_Title", title.getText().toString());
                data.putExtra("USER_TEXT_Content", content.getText().toString());
                setResult(RESULT_OK, data);
                Log.d(TAG, "onOptionsItemSelected: " + title.getText().toString() + ": " + content.getText().toString());

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

//    @Override
//    public void onBackPressed() {
//        if (content.getText().toString().length() == 0 || title.getText().toString().length() == 0) {
//            Toast.makeText(this, "Need title and content", Toast.LENGTH_SHORT).show();
//        }else {
//            Intent data = new Intent();
//            data.putExtra("USER_TEXT_Title", title.getText().toString());
//            data.putExtra("USER_TEXT_Content", content.getText().toString());
//            setResult(RESULT_OK, data);
//            super.onBackPressed();
//        }
//    }


    @Override
    public void onBackPressed() {
//        if (title.getText().toString().length() == 0) {
//            Toast.makeText(this, "Need title", Toast.LENGTH_SHORT).show();
//        }else
        {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your note is not saved!\nSave note '" + title.getText().toString() + "'?");

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                    finish();
                }
            });

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    if (title.getText().toString().length() == 0) {
                        //
                        finish();
                        //
                        toast();

                    } else {
                        Intent data = new Intent();
                        data.putExtra("USER_TEXT_Title", title.getText().toString());
                        data.putExtra("USER_TEXT_Content", content.getText().toString());
                        setResult(RESULT_OK, data);
                        finish();
                    }

                }
            });
            builder.show();


        }
    }

    public void toast() {
        Toast.makeText(this, "Un-titled note was not saved", Toast.LENGTH_LONG).show();
    }
}
