package com.example.notesapp

import android.content.Context
import android.graphics.ColorSpace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.room.Note

class NoteAdapter(
    val context: Context, private val noteClickDeleteInterface: NoteClickDeleteInterface,
    private val noteClickInterface: NoteClickInterface,
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var allNotes = ArrayList<Note>()



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTV = itemView.findViewById<TextView>(R.id.idTvNotesTitle)
        val deleteIV = itemView.findViewById<ImageView>(R.id.idIDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflating our layout file for each item of recycler view.
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.noteTV.setText(allNotes.get(position).noteTitle)
        //for interface
        holder.deleteIV.setOnClickListener {
            noteClickDeleteInterface.onDeleteIconClick(allNotes.get(position))
        }

        holder.itemView.setOnClickListener {
            noteClickInterface.onNoteClick(allNotes.get(position))
        }
    }


    override fun getItemCount(): Int {
        return allNotes.size
    }

    // below method is use to update our list of notes.
    fun updateList(newList: List<Note>) {
        // on below line we are clearing
        // our notes array list
        allNotes.clear()
        // on below line we are adding a
        // new list to our all notes list.
        allNotes.addAll(newList)
        // on below line we are calling notify data
        // change method to notify our adapter.
        notifyDataSetChanged()
    }


}

interface NoteClickDeleteInterface {
    // creating a method for click
    // action on delete image view.
    fun onDeleteIconClick(note: Note)
}

interface NoteClickInterface {
    // creating a method for click action
    // on recycler view item for updating it.
    fun onNoteClick(note: Note)
}
