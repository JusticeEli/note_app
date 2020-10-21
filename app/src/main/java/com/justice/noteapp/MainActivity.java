package com.justice.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String COLLECTION_HIGH = "high";
    public static final String COLLECTION_MEDIUM = "medium";
    public static final String COLLECTION_LOW = "low";
    public static final String COLLECTION_NOTES = "notes";
    public static final int RC_SIGN_IN = 3;

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
        checkIfWeAreLoginIn();
        initWidgets();
        setUpViewPager();
        setOnClickListeners();
        //    setBadgeUpdateListener();

    }

    private void checkIfWeAreLoginIn() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            //we are not logged in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                                    new AuthUI.IdpConfig.AnonymousBuilder().build()))
                            .setTosAndPrivacyPolicyUrls("https://www.linkedin.com/in/JusticeEli",
                                    "https://github.com/JusticeEli/").build(),
                    RC_SIGN_IN);
            finish();
        } else {
            setBadgeUpdateListener();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showToast("sign in cancelled");
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showToast("no internet connection");

                }
                showToast("unknown error");
                Log.e(TAG, "Sign-in error: ", response.getError());

            }
        }
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void setBadgeUpdateListener() {


        firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().getUid()).collection(COLLECTION_MEDIUM).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "onEvent: Error" + e.getMessage());
                    Toast.makeText(MainActivity.this, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Log.d(TAG, "onSuccess: loaded medium");
                tabLayout.getTabAt(0).getOrCreateBadge().setNumber(queryDocumentSnapshots.size());


            }
        });
        firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().getUid()).collection(COLLECTION_HIGH).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "onEvent: Error" + e.getMessage());
                    Toast.makeText(MainActivity.this, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                Log.d(TAG, "onSuccess: loaded high");
                tabLayout.getTabAt(1).getOrCreateBadge().setNumber(queryDocumentSnapshots.size());


            }
        });
        firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().getUid()).collection(COLLECTION_LOW).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "onEvent: Error" + e.getMessage());
                    Toast.makeText(MainActivity.this, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d(TAG, "onSuccess: loaded low");
                tabLayout.getTabAt(2).getOrCreateBadge().setNumber(queryDocumentSnapshots.size());


            }
        });

//////////////////////////////////////////////////////

/*
                tabLayout.getTabAt(0).getOrCreateBadge().setNumber(documentSnapshot.getReference().collection(COLLECTION_MEDIUM).get().getResult().size());
                tabLayout.getTabAt(1).getOrCreateBadge().setNumber(documentSnapshot.getReference().collection(COLLECTION_HIGH).get().getResult().size());
                tabLayout.getTabAt(2).getOrCreateBadge().setNumber(documentSnapshot.getReference().collection(COLLECTION_LOW).get().getResult().size());
    */
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
                ApplicationClass.documentSnapshot = null;
                startActivity(new Intent(this, AddNoteActivity.class));
                break;
            case R.id.deleteMenu:

                deleteAllNotes();

                break;
            case R.id.logoutMenu:
                logout();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        finish();
                    }
                });
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
        firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().getUid()).collection(COLLECTION_MEDIUM).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
        firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().getUid()).collection(COLLECTION_LOW).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
        firebaseFirestore.collection(COLLECTION_NOTES).document(FirebaseAuth.getInstance().getUid()).collection(COLLECTION_HIGH).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
