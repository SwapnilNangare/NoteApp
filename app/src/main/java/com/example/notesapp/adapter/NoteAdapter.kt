package com.example.notesapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.room.Note

class NoteAdapter(val context: Context, private val noteClickDeleteInterface: NoteClickDeleteInterface,
    private val noteClickInterface: NoteClickInterface) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var allNotes = ArrayList<Note>()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById<TextView>(R.id.idTvNotesTitle)
        val delete: ImageView = itemView.findViewById<ImageView>(R.id.idIDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.setText(allNotes.get(position).noteTitle)
        //for interface
        holder.delete.setOnClickListener {
            noteClickDeleteInterface.onDeleteIconClick(allNotes.get(position))
        }

        holder.itemView.setOnClickListener {
            noteClickInterface.onNoteClick(allNotes.get(position))
        }
    }
    fun updateFilter(note : ArrayList<Note>){
        allNotes = note
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }


    fun updateList(newList: List<Note>) {
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }


}

interface NoteClickDeleteInterface {
    fun onDeleteIconClick(note: Note)
}

interface NoteClickInterface {
    fun onNoteClick(note: Note)
}


