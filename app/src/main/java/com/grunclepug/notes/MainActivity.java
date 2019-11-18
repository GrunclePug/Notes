package com.grunclepug.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Main Activity
 * The main page of the Notes application
 * @author Chad Humphries
 * @version 1.0
 * Date Created: Nov 18, 2019
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener
{
    public static ArrayList<String> notes = new ArrayList<>();
    public static ArrayAdapter arrayAdapter;

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ListView listView;

    /**
     * Initialize Application
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.grunclepug.notes", Context.MODE_PRIVATE);
        HashSet<String> stringHashSet = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if(stringHashSet != null)
        {
            notes = new ArrayList<>(stringHashSet);
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        listView = findViewById(R.id.listView);

        setSupportActionBar(toolbar);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        fab.setOnClickListener(this);

        listView.setAdapter(arrayAdapter);
    }

    /**
     * Initialize Options Menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Options Menu Item Selected Event
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.about_us)
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("About us")
                    .setMessage("Developer: Chad Humphries" +
                            "\nGithub: https://github.com/GrunclePug" +
                            "\nWebsite: https://grunclepug.com" +
                            "\nDiscord: GrunclePug#7015")
                    .setNeutralButton("Done", null)
                    .show();
            return true;
        }
        return false;
    }

    /**
     * Add Note Event
     * @param v
     */
    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.fab)
        {
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Open Note Event
     * @param parent
     * @param view
     * @param pos
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
    {
        Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
        intent.putExtra("noteId", pos);
        startActivity(intent);
    }

    /**
     * Delete Note Event
     * @param parent
     * @param view
     * @param pos
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id)
    {
        final int item = pos;

        new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        notes.remove(item);
                        arrayAdapter.notifyDataSetChanged();

                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.grunclepug.notes", Context.MODE_PRIVATE);

                        HashSet<String> stringHashSet = new HashSet<>(MainActivity.notes);
                        sharedPreferences.edit().putStringSet("notes", stringHashSet).apply();
                    }
                })
                .setNegativeButton("No", null)
                .show();
        return true;
    }
}
