package com.example.notesapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


    val shouldCloseLiveData = MutableLiveData<Void?>()
    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    val allNotes: LiveData<List<Note>>
    val repository: NoteRepository

    // FireBase


    init {
        val dao = NoteDatabase.getDatabase(application).getNotesDao()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes


    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)

        //firebase deletion
        val database = FirebaseDatabase.getInstance().reference
        database.child("notes").child(note.id.toString()).removeValue()

    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)

        //firebase for update



    }

    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)

        //firebase

        databaseReference.child("notes").push().also {
            val id = it.key
            it.setValue(NoteFireBase(id.toString(), note.noteTitle, note.noteDescription))
        }


    }


}





