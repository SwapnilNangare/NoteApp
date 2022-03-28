package com.example.notesapp

import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.room.Note
import com.example.notesapp.ui.Listener

class NoteAdapter(
    val listener: Listener, val activity: AppCompatActivity
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>(),androidx.appcompat.view.ActionMode.Callback {

    private var allNotes = ArrayList<Note>()
    private var selectedNotes = ArrayList<Note>()
    private var multiSelect: Boolean = false


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById<TextView>(R.id.idTvNotesTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentSelected = allNotes[position]
        holder.title.text = allNotes.get(position).noteTitle

        holder.itemView.setOnClickListener{

            if(multiSelect){
                selectItem(holder,currentSelected)
            }
            else{
                multiSelect = false
                listener.onClick(allNotes.get(position))
            }

        }

        if(allNotes.contains(currentSelected)){
            holder.itemView.background = ContextCompat.getDrawable(activity,R.drawable.selected_note_drawable)
        }
        else
        {
            holder.itemView.background = ContextCompat.getDrawable(activity,R.drawable.notes_drawable)

        }
        holder.itemView.setOnLongClickListener {
            if(!multiSelect){
                multiSelect = true
                activity.startSupportActionMode(this@NoteAdapter)
                selectItem(holder,allNotes[holder.adapterPosition])
                true
            }
            else{

                false
            }
        }
    }
    override fun getItemCount(): Int {
        return allNotes.size
    }

    private fun selectItem(holder: NoteAdapter.ViewHolder, note: Note) {
        if(selectedNotes.contains(note)){
            selectedNotes.remove(note)
            holder.itemView.background = ContextCompat.getDrawable(activity,R.drawable.notes_drawable)
        }
        else{
            selectedNotes.add(note)
            notifyDataSetChanged()
            holder.itemView.background = ContextCompat.getDrawable(activity,R.drawable.selected_note_drawable)
        }

    }

    fun updateFilter(note: ArrayList<Note>) {
        allNotes = note
        notifyDataSetChanged()
    }



    fun updateList(newList: List<Note>) {
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }



    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        val inflater : MenuInflater = mode?.menuInflater!!
        inflater.inflate(R.menu.action_menu,menu)
        return true    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.btnDelete){
            listener.onLongClick(selectedNotes)
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        multiSelect = false
        allNotes.clear()
        notifyDataSetChanged()
    }
}


