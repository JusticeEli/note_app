package com.justice.noteapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_note.*
import com.google.firebase.firestore.FirebaseFirestore

class NoteFragment  constructor(): Fragment(R.layout.fragment_note) {
    private val args: AddNoteFragmentArgs by navArgs()
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var note: Note


    init {
        firebaseFirestore.document(args.documentReferencePath!!)
                .get().addOnSuccessListener {
                    note = it.toObject(Note::class.java)!!


                }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDefaultValues()
    }

    private fun setDefaultValues() {
        noteTxtInput!!.editText!!.setText(note!!.noteData)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_menu, menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.editMenu) {
            val action=NoteFragmentDirections.actionNoteFragmentToAddNoteFragment(args.documentReferencePath);
            findNavController().navigate(action)
            goBack();
        } else if (item.itemId == R.id.deleteMenu) {
            deleteNote()
            goBack();
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goBack() {
        if (fragmentManager?.backStackEntryCount!! >0){
            fragmentManager?.popBackStackImmediate()
        }
    }

    private fun deleteNote() {

        firebaseFirestore.document(args.documentReferencePath!!).
       delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Deletetion success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}