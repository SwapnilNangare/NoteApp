package com.example.notesapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.R
import com.example.notesapp.room.Note
import com.example.notesapp.viewModel.NoteViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddEditNoteActivity : AppCompatActivity() {

    lateinit var noteTitleEdt: EditText
    lateinit var noteEdt: EditText
    lateinit var saveBtn: Button

    lateinit var viewModal: NoteViewModel
    var noteID = -1;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        noteTitleEdt = findViewById(R.id.idEdtNoteTitle)
        noteEdt = findViewById(R.id.idEdtNoteDesc)
        saveBtn = findViewById(R.id.idBtnAddUpdate)

        saveBtn.setOnClickListener{
            saveHero()
        }


        viewModal = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)


        val noteType = intent.getStringExtra("noteType")
        if (noteType.equals("Edit")) {
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDescription = intent.getStringExtra("noteDescription")
            noteID = intent.getIntExtra("noteId", -1)
            saveBtn.setText("Update Note")
            noteTitleEdt.setText(noteTitle)
            noteEdt.setText(noteDescription)
        } else {
            saveBtn.setText("Save Note")
        }


        saveBtn.setOnClickListener {

            val noteTitle = noteTitleEdt.text.toString()
            val noteDescription = noteEdt.text.toString()

            if (noteType.equals("Edit")) {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    val updatedNote = Note(noteTitle, noteDescription)
                    updatedNote.id = noteID
                    viewModal.updateNote(updatedNote)
                    Toast.makeText(this, "Note Updated..", Toast.LENGTH_LONG).show()


                }
            } else {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    viewModal.addNote(Note(noteTitle, noteDescription))
                    Toast.makeText(this, "$noteTitle Added", Toast.LENGTH_LONG).show()
                }
            }
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Add Notes")
    }

    private fun saveHero() {
       val title=noteTitleEdt.text.toString().trim()
        val desc=noteEdt.text.toString().trim()

        if(title.isNotEmpty() && desc.isNotEmpty())
        {
            Toast.makeText(this,"error in firebase",Toast.LENGTH_SHORT).show()
            return
        }
        val ref= FirebaseDatabase.getInstance().getReference("heroes")
        val heroId= ref.push().key
        val hero=Note(noteID.toString(),title,desc.toInt())
        ref.child(noteID.toString()).setValue(hero).addOnCompleteListener{
            Toast.makeText(applicationContext,"data save successfully",Toast.LENGTH_SHORT).show()
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}