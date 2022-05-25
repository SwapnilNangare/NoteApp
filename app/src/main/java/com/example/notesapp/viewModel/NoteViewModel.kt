package com.example.notesapp.viewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.firebase.NoteFireBase
import com.example.notesapp.room.Note
import com.example.notesapp.room.NoteDatabase
import com.example.notesapp.room.NoteRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var database: DatabaseReference

    private val dataBaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    val allNotes: LiveData<List<Note>>
    private val repository: NoteRepository

    init {
        val dao = NoteDatabase.getDatabase(application).getNotesDao()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)

        //firebase delete
        deleteFromFireBase(note)


    }

    private fun deleteFromFireBase(note: Note) {
        database = FirebaseDatabase.getInstance().getReference("Note")
        database.child(note.noteTitle).removeValue().addOnSuccessListener {

            Log.d(TAG,"Deleted to firebase")

        }.addOnFailureListener {
            Log.d(TAG,"Note to firebase")
        }



    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
        //firebase update
        updateFirebase(note)


    }
    private fun updateFirebase(note: Note) {

        database = FirebaseDatabase.getInstance().getReference("Note")
        val user = mapOf(
            "title" to note.noteTitle,
            "description" to note.noteDescription,
            "id" to note.id
        )
        database.child(note.noteTitle).updateChildren(user).addOnSuccessListener {


        }.addOnFailureListener {

        }

    }


    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)

        // Add To Firebase

        dataBaseReference.child(note.noteTitle).push().also {
           it.key
            it.setValue(Note(note.noteTitle, note.noteDescription, note.id))
        }


    }


}

