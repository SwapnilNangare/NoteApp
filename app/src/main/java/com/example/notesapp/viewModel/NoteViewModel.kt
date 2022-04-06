package com.example.notesapp.viewModel

import android.app.Application
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
        database = FirebaseDatabase.getInstance().getReference("NoteFireBase")
        database.child(note.noteTitle).removeValue().addOnSuccessListener {

        }.addOnFailureListener {

        }

    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
     //firebase update
        updateFirebase(note)


    }

    private fun updateFirebase(note: Note) {

        database = FirebaseDatabase.getInstance().getReference("NoteFireBase")
        val user = mapOf(
            "title" to note.noteTitle,
            "description" to note.noteDescription,
            "id" to note.id.toString()
        )
        database.child(note.noteTitle).updateChildren(user).addOnSuccessListener {


        }.addOnFailureListener {

        }

    }


    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)

        // Add To Firebase

        dataBaseReference.child("NoteFireBase").push().also {
            val id = it.key
            it.setValue(NoteFireBase(note.id.toString(), note.noteTitle, note.noteDescription))
        }


    }


}





