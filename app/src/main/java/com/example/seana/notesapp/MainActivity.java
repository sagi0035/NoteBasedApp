package com.example.seana.notesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View.OnLongClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    public void serializer() {
        try {
            // so we will try initialise the arraylist
            toDoList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notesAgain15",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }



        if (toDoList.size() == 0) {
            toDoList.add("Things to do....");
        }
    }


    ListView listView;
    // so here we will create an array list that will hold all our notes (we set static so that it is accessed within other intents too)
    static ArrayList<String> toDoList = new ArrayList<String>();
    static ArrayAdapter arrayAdapter;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // the shared preferences will exist so as to keep the notes permanent (only removed on deletion)
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.seana.notesapp", Context.MODE_PRIVATE);

        // the listview will crate a list of items (which will later form the basis for the list of notes)
        listView = (ListView) findViewById(R.id.listView);


        serializer();

        // now we will create and set the adapter to follow the items of the array list
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,toDoList);
        listView.setAdapter(arrayAdapter);





        // now on pressing every individual lsit item we will send the user to a new intent that logs all the text of the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),otherMainActivity.class);
                intent.putExtra("noteId",i);
                intent.putExtra("permissionGiver","granted");
                startActivity(intent);
            }
        });






        // now for a long click we will set the items to get deleted
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                // this gets the index of what we will be deleting
                final int itemToDelete = i;

                // we create an allert to assurre that the user actually wants to remove the item
                new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?").setMessage("Do you want to delete that note?")
                        // this is for postive
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // so we remove the item from the array lit
                                toDoList.remove(itemToDelete);
                                // and notify hte adapterv of the change so that it is reflected in the listview
                                arrayAdapter.notifyDataSetChanged();

                                // permanently save the changes
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.seana.notesapp", Context.MODE_PRIVATE);
                                try {
                                    sharedPreferences.edit().putString("notesAgain15",ObjectSerializer.serialize(MainActivity.toDoList)).apply();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                // on negative we do nothing
                            }
                        }).setNegativeButton("No",null).show();

                return true;
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // so this creates a pop-uo when the settings image is pressed
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // and in the case were the image is pressed we create a new intent for adding a new note
            case R.id.addANewNote:
                Intent intent = new Intent(getApplicationContext(),otherMainActivity.class);
                intent.putExtra("myIndex",toDoList.size());
                intent.putExtra("permissionGiver","notGranted");
                startActivity(intent);
                return true;
            default:
                return false;
        }

    }
}
