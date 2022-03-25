package com.example.notesapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.NoteAdapter
import com.example.notesapp.R
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.room.Note
import com.example.notesapp.viewModel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_add_edit_note.*

class MainActivity : AppCompatActivity(), Listener {


    lateinit var addButton: FloatingActionButton
    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NoteAdapter
    lateinit var searchView: EditText
    lateinit var recyclerview: RecyclerView
    private var allNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerview = findViewById(R.id.recyclerview)
        addButton = findViewById(R.id.idFABAddNote)
        searchView = findViewById(R.id.edtSearch)

        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(allNotes, this, this)
        recyclerview.adapter = adapter
        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
        viewModel.allNotes.observe(this, Observer { list ->
            list?.let {
                adapter.updateList(it)
            }

        })
        addButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }

        })

    }

    fun filter(s: String) {
        val temp = ArrayList<Note>()
        for (i in allNotes) {
            if (i.noteTitle?.contains(s)!! or i.noteDescription?.contains(s)!!) {
                temp.add(i)
            }
        }
        adapter.updateFilter(temp)
    }


    override fun onLongClick(notes: ArrayList<Note>) {

        for (i in notes) {
            viewModel.deleteNote(i)
        }
        Toast.makeText(this, "Deleted", Toast.LENGTH_LONG).show()

    }

    override fun onClick(note: Note) {
        val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("noteDescription", note.noteDescription)
        intent.putExtra("noteId", note.id)
        startActivity(intent)
        this.finish()
    }


}