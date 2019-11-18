package com.grunclepug.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.HashSet;

/**
 * Note Editor Activity
 * The note editor for the application
 * @author Chad Humphries
 * @version 1.0
 * Date Created: Nov 18, 2019
 */
public class NoteEditorActivity extends AppCompatActivity implements TextWatcher
{
    private Toolbar toolbar;
    private EditText editText;
    private int noteId;

    /**
     * Initialize Note Editor
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        toolbar = findViewById(R.id.editorToolbar);
        editText = findViewById(R.id.editText);

        setSupportActionBar(toolbar);
        noteId = getIntent().getIntExtra("noteId", -1);

        if(noteId != -1)
        {
            editText.setText(MainActivity.notes.get(noteId));
        }
        else
        {
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }

        editText.addTextChangedListener(this);
    }

    /**
     * Initialize Options Menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.editor_menu, menu);

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

        if(item.getItemId() == R.id.doneButton)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            return true;
        }
        return false;
    }

    /**
     * Pre Text Update Event
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    /**
     * Live Text Save Event
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        MainActivity.notes.set(noteId, String.valueOf(s).trim());
        MainActivity.arrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.grunclepug.notes", Context.MODE_PRIVATE);

        HashSet<String> stringHashSet = new HashSet<>(MainActivity.notes);
        sharedPreferences.edit().putStringSet("notes", stringHashSet).apply();
    }

    /**
     * Post Text Change Event
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s)
    {
    }
}
