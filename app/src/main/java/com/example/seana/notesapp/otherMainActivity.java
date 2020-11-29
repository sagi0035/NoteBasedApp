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

        noteId = intent.getIntExtra("noteId",-1);

        perm = intent.getStringExtra("permissionGiver");

        if (noteId != -1) {
            Log.i("Chest","Seal");
            editText.setText(MainActivity.toDoList.get(noteId));
            numberAllowance = 1;
        } else {
            //MainActivity.toDoList.add("");
            //MainActivity.arrayAdapter.notifyDataSetChanged();
            //noteId = MainActivity.toDoList.size() - 1;

        }




        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (numberAllowance == 0) {
                    MainActivity.toDoList.add("");
                    MainActivity.arrayAdapter.notifyDataSetChanged();
                    numberAllowance = 1;
                }

                noteId = MainActivity.toDoList.size() - 1;

                Log.i("We have",perm);

                if (perm.equals("granted")) {
                    Log.i("Jon","Jones");
                    Intent intent = getIntent();
                    // problem is that you are overriding to last array each time
                    noteId = intent.getIntExtra("noteId",-1);
                } else {
                    Log.i("Xi","Zobb");
                }


                Log.i("The number allowance is",Integer.toString(numberAllowance));
                Log.i("The number index is",Integer.toString(noteId));

                MainActivity.toDoList.set(noteId,String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();


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
