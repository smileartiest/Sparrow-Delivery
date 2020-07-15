package com.smile.atozdelivery.firebasedb;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class FirebaseData {

    public Context context;
    public DatabaseReference dfbase ;
    public Query qbase;
    public String sts = "";

    public FirebaseData(Context context) {
        this.context = context;
    }

    public void checkuser(final String phno1 ,final String pass1){

    }
}
