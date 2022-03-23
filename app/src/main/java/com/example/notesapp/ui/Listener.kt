package com.example.notesapp.ui

import com.example.notesapp.room.Note

interface Listener {

    fun onLongClick(notes : ArrayList<Note>)
}