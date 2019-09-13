package com.ue.uebook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ue.uebook.Data.ApiRequest;
import com.ue.uebook.HomeActivity.HomeFragment.Adapter.Bookmark_List_Adapter;
import com.ue.uebook.HomeActivity.HomeFragment.Bookmark_Fragment;
import com.ue.uebook.HomeActivity.HomeFragment.Pojo.UserBookmarkResponse;

import java.io.IOException;

public class NotepadScreen extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back_btn ,edit_btn,delete_btn;
    private Intent intent;
    private Button updateNote;
    private EditText notes_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad_screen);
        back_btn = findViewById(R.id.back_btn_notes);
        edit_btn=findViewById(R.id.edit_Post);
        delete_btn=findViewById(R.id.delete_Btn);
        notes_view=findViewById(R.id.notes_view);
        updateNote=findViewById(R.id.updateNote);
        updateNote.setOnClickListener(this);
        edit_btn.setOnClickListener(this);
        delete_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        intent = getIntent();
        int id = intent.getIntExtra("id",0);
        if (id==1){
            edit_btn.setVisibility(View.VISIBLE);
            delete_btn.setVisibility(View.VISIBLE);
            notes_view.setEnabled(false);
        }
        else {

            edit_btn.setVisibility(View.GONE);
            delete_btn.setVisibility(View.GONE);
            notes_view.setEnabled(true);
            notes_view.setFocusable(true);
            notes_view.requestFocus();
        }

    }

    @Override
    public void onClick(View view) {
        if (view==back_btn)
        {
            finish();
        }
        else if (view==delete_btn){
            confirmDeleteDialog();
        }
        else if (view==edit_btn){
            notes_view.setEnabled(true);;
            notes_view.setFocusable(true);
            notes_view.requestFocus();
        }
        else if (view==updateNote){
         if (!notes_view.getText().toString().isEmpty()){
             updateNotes(new SessionManager(getApplicationContext()).getUserID(),notes_view.getText().toString());
         }
         else {
             Toast.makeText(getApplicationContext(),"Please Add Notes For Update",Toast.LENGTH_SHORT).show();
         }

        }
    }
    private void confirmDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You sure, that you want Delete ")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void updateNotes(String user_id,String desc) {
        ApiRequest request = new ApiRequest();
        request.requestforAddNotes(user_id,desc,new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("error", "error");
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                final String myResponse = response.body().string();
                Gson gson = new GsonBuilder().create();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Notes Saved",Toast.LENGTH_SHORT).show();
                        notes_view.setEnabled(false);;
                        delete_btn.setVisibility(View.VISIBLE);
                        edit_btn.setVisibility(View.VISIBLE);
                    }
                });


            }
        });
    }

}
