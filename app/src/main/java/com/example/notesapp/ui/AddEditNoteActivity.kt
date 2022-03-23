package com.example.notesapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.R
import com.example.notesapp.room.Note
import com.example.notesapp.viewModel.NoteViewModel

class AddEditNoteActivity : AppCompatActivity() {

    lateinit var noteTitle: EditText
    lateinit var noteDes: EditText
    lateinit var addUpdateBtn: Button
    lateinit var viewModel: NoteViewModel
    var noteId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Add Notes")

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            NoteViewModel::class.java)

        noteTitle = findViewById(R.id.idEdtNoteTitle)
        noteDes = findViewById(R.id.idEdtNoteDesc)
        addUpdateBtn = findViewById(R.id.idBtnAddUpdate)

        val noteType = intent.getStringExtra("noteType")
        if (noteType.equals("Edit")) {

            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDescription = intent.getStringExtra("noteDescription")
            noteId = intent.getIntExtra("noteId", -1)

            addUpdateBtn.setText("Update Note")
            this.noteTitle.setText(noteTitle)
            this.noteTitle.setText(noteDescription)
        }
        else {
            addUpdateBtn.setText("Save Note")
        }

        // on below line we are adding
        // click listener to our save button.
        addUpdateBtn.setOnClickListener {
            // on below line we are getting
            // title and desc from edit text.
            val noteTitle = noteTitle.text.toString()
            val noteDescription = noteDes.text.toString()

            if (noteType.equals("Edit")) {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    val updatedNote = Note(noteTitle, noteDescription)
                    updatedNote.id = noteId
                    viewModel.updateNote(updatedNote)
                    Toast.makeText(this, "Note Updated..", Toast.LENGTH_LONG).show()
                }
            } else {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    viewModel.addNote(Note(noteTitle, noteDescription))
                    Toast.makeText(this, "$noteTitle Added", Toast.LENGTH_LONG).show()
                }
            }
            // opening the new activity on below line
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}