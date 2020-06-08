package com.justice.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Fragment mediumFragment = new MediumFragment();
    private Fragment highFragment = new HighFragment();
    private Fragment lowFragment = new LowFragment();

    public static final List<Fragment> fragments = new ArrayList<>();
    public static final List<String> fragmentsName = new ArrayList<>();
    private int position;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        setUpViewPager();
        setOnClickListeners();
        setBadgeUpdateListener();

    }

    private void setBadgeUpdateListener() {
        firebaseFirestore.collection("Notes").document(FirebaseAuth.getInstance().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                tabLayout.getTabAt(0).getOrCreateBadge().setNumber(documentSnapshot.getReference().collection("medium").get().getResult().size());
                tabLayout.getTabAt(1).getOrCreateBadge().setNumber(documentSnapshot.getReference().collection("high").get().getResult().size());
                tabLayout.getTabAt(2).getOrCreateBadge().setNumber(documentSnapshot.getReference().collection("low").get().getResult().size());
            }
        });


    }

    private void setOnClickListeners() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainActivity.this.position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpViewPager() {
        fragments.add(mediumFragment);
        fragments.add(highFragment);
        fragments.add(lowFragment);

        fragmentsName.add("medium");
        fragmentsName.add("high");
        fragmentsName.add("low");


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addMenu:
                ApplicationClass.documentSnapshot=null;
                startActivity(new Intent(this, AddNoteActivity.class));
                break;
            case R.id.deleteMenu:

                deleteAllNotes();

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNotes() {

        switch (position) {
            case 0:
                deleteMediumNotes();
                break;
            case 1:
                deleteHighNotes();
                break;
            case 2:
                deleteLowNotes();
                break;

        }
    }

    private void deleteMediumNotes() {
        firebaseFirestore.collection("Notes").document(FirebaseAuth.getInstance().getUid()).collection("medium").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        documentSnapshot.getReference().delete();

                    }

                } else {
                    Toast.makeText(MainActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteLowNotes() {
        firebaseFirestore.collection("Notes").document(FirebaseAuth.getInstance().getUid()).collection("low").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        documentSnapshot.getReference().delete();

                    }

                } else {
                    Toast.makeText(MainActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteHighNotes() {
        firebaseFirestore.collection("Notes").document(FirebaseAuth.getInstance().getUid()).collection("high").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        documentSnapshot.getReference().delete();

                    }

                } else {
                    Toast.makeText(MainActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initWidgets() {
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }
}
