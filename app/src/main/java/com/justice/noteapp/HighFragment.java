package com.justice.noteapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static com.justice.noteapp.MainActivity.COLLECTION_HIGH;
import static com.justice.noteapp.MainActivity.COLLECTION_NOTES;


/**
 * A simple {@link Fragment} subclass.
 */
public class HighFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;

    public HighFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_high, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        setUpFirestore();

    }

    private void setUpFirestore() {
        Query query = firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().getUid()).collection(COLLECTION_HIGH);


        FirestoreRecyclerOptions<Note> firestoreRecyclerOptions=new FirestoreRecyclerOptions.Builder<Note>().setQuery(query, new SnapshotParser<Note>() {
            @NonNull
            @Override
            public Note parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                Note note = snapshot.toObject(Note.class);
                note.setId(snapshot.getId());
                return note;
            }
        }).setLifecycleOwner(HighFragment.this).build();

        noteAdapter = new NoteAdapter(getActivity(), firestoreRecyclerOptions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(noteAdapter);

    }
}
