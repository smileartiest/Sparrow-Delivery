package com.smile.atozdelivery.controller;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppUtill {

    public static DatabaseReference ORDERURl = FirebaseDatabase.getInstance().getReference("order");
    public static DatabaseReference EMPURL = FirebaseDatabase.getInstance().getReference("employe");
    public static DatabaseReference ADDRESURL = FirebaseDatabase.getInstance().getReference("address");
    public static DatabaseReference REGURL = FirebaseDatabase.getInstance().getReference("usregister");

}
