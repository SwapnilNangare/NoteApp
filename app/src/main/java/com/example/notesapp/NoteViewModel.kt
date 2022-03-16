package com.example.notesapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.room.Note
import com.example.notesapp.room.NoteDatabase
import com.example.notesapp.room.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application):AndroidViewModel(application) {

    val allNotes:LiveData<List<Note>>
    val repository: NoteRepository

    init {
        val dao = NoteDatabase.getDatabase(application).getNotesDao()
        repository= NoteRepository(dao)
        allNotes=repository.allNotes
    }

  //all methods deleting updating...


  fun deleteNote(note: Note)= viewModelScope.launch(Dispatchers.IO){
      repository.delete(note)
  }

    fun updateNote(note: Note) =viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }
    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

}