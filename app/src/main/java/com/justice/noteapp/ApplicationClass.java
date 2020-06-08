package com.justice.noteapp;

import android.app.Application;

import com.google.firebase.firestore.DocumentSnapshot;

public class ApplicationClass extends Application {
    public static  DocumentSnapshot documentSnapshot;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
