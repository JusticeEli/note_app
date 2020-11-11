package com.justice.noteapp

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.justice.noteapp.MainFragment.Companion.COLLECTION_NOTES
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlin.concurrent.fixedRateTimer

class AddNoteFragment : Fragment(R.layout.fragment_add_note) {

    private val args: AddNoteFragmentArgs by navArgs()

    private var progressDialog: ProgressDialog? = null
    private val firebaseFirestore = FirebaseFirestore.getInstance()
    private var updating = false
    private var noteOriginal: Note? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpProgressBar();
        setOnClickListeners()
        setUpCategorySpinner()
        checkIfWeAreUpdatingOrAddingNote()
        setDefaultValues()

    }

    private fun setUpProgressBar() {
        progressDialog = ProgressDialog(context)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setTitle("saving data...")
    }

    private fun setDefaultValues() {
        if (updating) {
            noteTxtInput!!.editText!!.setText(noteOriginal!!.noteData)
            Handler().postDelayed({ categorySpinner!!.setSelection(noteOriginal!!.category) }, 100)
        }
    }

    private fun checkIfWeAreUpdatingOrAddingNote() {
        if (args.documentReferencePath != null) {
            updating = true
            firebaseFirestore.document(args.documentReferencePath!!).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(Companion.TAG, "checkIfWeAreUpdatingOrAddingNote: success loading document")
                    noteOriginal = task.result!!.toObject(Note::class.java)

                } else {
                    Log.e(Companion.TAG, "checkIfWeAreUpdatingOrAddingNote: " + task.exception!!.message)
                }

            }
        }
    }

    private fun setUpCategorySpinner() {
        val categories = arrayOf("Medium", "High", "Low")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        categorySpinner!!.adapter = adapter
    }

    private fun setOnClickListeners() {
        cancelFob!!.setOnClickListener {
            Log.d(TAG, "setOnClickListeners: cancel button clicked")
            activity?.onBackPressed()
        }
        doneFob!!.setOnClickListener {
            saveNote()
        }

    }

    private fun saveNote() {
        Log.d(TAG, "saveNote: done button pressed")
        val noteData = noteTxtInput!!.editText!!.text.toString()
        if (noteData.isEmpty()) {
            noteTxtInput!!.error = "Please fill"
            noteTxtInput!!.requestFocus()
            return
        }
        val note = Note()
        note.noteData = noteData
        if (updating) {
            updateNote(note)
        } else {
            setNoteCategoryAndSaveToFirebase(note)
        }
    }

    private fun updateNote(note: Note) {
        progressDialog!!.show()
        when (categorySpinner!!.selectedItemPosition) {
            0 -> {
                note.category = 0
                args.documentReferencePath?.let {
                    firebaseFirestore.document(it)
                            .set(note).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Note Saved", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Error: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                                }
                                progressDialog!!.dismiss()
                            }


                }

            }
            1 -> {
                note.category = 1
                args.documentReferencePath?.let {
                    firebaseFirestore.document(it)
                            .set(note).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Note Saved", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Error: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                                }
                                progressDialog!!.dismiss()
                            }

                }

            }
            2 -> {
                note.category = 2
                args.documentReferencePath?.let {
                    firebaseFirestore.document(it)
                            .set(note).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Note Saved", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Error: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                                }
                                progressDialog!!.dismiss()
                            }

                }

            }
        }
    }

    private fun setNoteCategoryAndSaveToFirebase(note: Note) {
        progressDialog!!.show()
        when (categorySpinner!!.selectedItemPosition) {
            0 -> {
                note.category = 0
                firebaseFirestore.collection(MainFragment.COLLECTION_NOTES).document(FirebaseAuth.getInstance().uid!!).collection(MainFragment.COLLECTION_MEDIUM).add(note).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Note Saved", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                    progressDialog!!.dismiss()
                }
            }
            1 -> {
                note.category = 1
                firebaseFirestore.collection(MainFragment.COLLECTION_NOTES).document(FirebaseAuth.getInstance().uid!!).collection(MainFragment.COLLECTION_HIGH).add(note).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Note Saved", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                    progressDialog!!.dismiss()
                }
            }
            2 -> {
                note.category = 2
                firebaseFirestore.collection(MainFragment.COLLECTION_NOTES).document(FirebaseAuth.getInstance().uid!!).collection(MainFragment.COLLECTION_LOW).add(note).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Note Saved", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error: " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                    progressDialog!!.dismiss()
                }
            }
        }
    }

    companion object {
        private const val TAG = "AddNoteFragment"
    }


}