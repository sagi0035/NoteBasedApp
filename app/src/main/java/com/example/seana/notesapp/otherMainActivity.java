package com.example.seana.notesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.util.HashSet;

public class otherMainActivity extends AppCompatActivity {

    EditText editText;
    String toPutInToDo;
    int noteId;
    int numberAllowance = 0;
    String perm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_main);


        editText = (EditText) findViewById(R.id.editText);



        Intent intent = getIntent();

        // so this is an extra that we picked up so as to keep note of the index of the array item that we may revise
        // if we are adding a new note this has a default value of -1
        noteId = intent.getIntExtra("noteId",-1);

        // and this is to track whether or not there is in fact permission to change (if not permission granted that means we are adding a new note)
        perm = intent.getStringExtra("permissionGiver");

        // so if it is the case that we opened this activity to revise we will get the text from the arraylist and set the text here to its value
        if (noteId != -1) {
            editText.setText(MainActivity.toDoList.get(noteId));
            numberAllowance = 1;
        }




        // so this will now be to save the changes to the edit text
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                // so only in the case were we are adding a new note we will add a new list item and notify the adapter of the changes
                if (numberAllowance == 0) {
                    MainActivity.toDoList.add("");
                    MainActivity.arrayAdapter.notifyDataSetChanged();
                    numberAllowance = 1;
                }

                // so here if a new note we set the index to the last array item so the new note is saved to the adapter as such
                noteId = MainActivity.toDoList.size() - 1;


                if (perm.equals("granted")) {
                    Intent intent = getIntent();
                    // and if we are revising an existing note we get the index that said note represents
                    noteId = intent.getIntExtra("noteId",-1);
                }

                // and then we set the new or revised note to be the types
                MainActivity.toDoList.set(noteId,String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                // and we and save it permanently
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.seana.notesapp", Context.MODE_PRIVATE);
                try {
                    sharedPreferences.edit().putString("notesAgain15",ObjectSerializer.serialize(MainActivity.toDoList)).apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {


            }

        });





    }
}
