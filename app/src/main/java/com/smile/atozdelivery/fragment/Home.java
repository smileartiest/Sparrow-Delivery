package com.smile.atozdelivery.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozdelivery.R;
import com.smile.atozdelivery.controller.AppUtill;
import com.smile.atozdelivery.controller.BillingParameters;
import com.smile.atozdelivery.controller.Control;
import com.smile.atozdelivery.controller.LocationClass;
import com.smile.atozdelivery.controller.MyAccountParameters;
import com.smile.atozdelivery.controller.TimeDate;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends Fragment {

    View v;
    CircleImageView pic;
    TextView name, uid, stst, login, logout;
    TextView totalcount, totalearntxt, totlapendingtxt, todaycount, todayearntxt;
    ArrayList<String> amountlistpending = new ArrayList<>();
    ArrayList<String> amountlistearn = new ArrayList<>();
    ArrayList<String> todayamountlistearn = new ArrayList<>();

    Control c;

    private final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationClass locationClass;

    public Home() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_home, container, false);
        totalcount = v.findViewById(R.id.home_totalorders);
        totalearntxt = v.findViewById(R.id.home_totalamount);
        totlapendingtxt = v.findViewById(R.id.home_pendingamount);
        todaycount = v.findViewById(R.id.home_todayorders);
        todayearntxt = v.findViewById(R.id.home_todayamount);

        pic = v.findViewById(R.id.macount_pic);
        name = v.findViewById(R.id.macount_name);
        uid = v.findViewById(R.id.macount_uid);
        stst = v.findViewById(R.id.macount_sts);
        login = v.findViewById(R.id.macount_login);
        logout = v.findViewById(R.id.macount_logout);

        c = new Control(getContext());
        locationClass = new LocationClass(getContext());

        if (c.getwork() != null) {
            if (c.getwork().equals("login")) {
                login.setVisibility(View.GONE);
                logout.setVisibility(View.VISIBLE);
            } else {
                login.setVisibility(View.VISIBLE);
                logout.setVisibility(View.GONE);
            }
        } else {
            login.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
        }

        AppUtill.EMPURL.child(c.getuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    MyAccountParameters m = dataSnapshot.getValue(MyAccountParameters.class);
                    Glide.with(getContext()).load(m.getEpic()).into(pic);
                    name.setText(m.getEname());
                    uid.setText(m.getEusname());
                    stst.setText(m.getEsts());
                    c.addName(m.getEname());
                    if (m.getEsts().equals("login")) {
                        login.setVisibility(View.GONE);
                        logout.setVisibility(View.VISIBLE);
                    } else {
                        login.setVisibility(View.VISIBLE);
                        logout.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        gettotalcount();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkLocationPermission()){
                    checkLocationPermission();
                }else {
                    AppUtill.EMPURL.child(c.getuid()).child("ests").setValue("login");
                    AppUtill.EMPURL.child(c.getuid()).child("lat").setValue(String.valueOf(locationClass.getLat()));
                    AppUtill.EMPURL.child(c.getuid()).child("lang").setValue(String.valueOf(locationClass.getlong()));
                    c.addwork();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkLocationPermission()){
                    checkLocationPermission();
                }else {
                    AppUtill.EMPURL.child(c.getuid()).child("ests").setValue("logout");
                    AppUtill.EMPURL.child(c.getuid()).child("lat").setValue(String.valueOf(locationClass.getLat()));
                    AppUtill.EMPURL.child(c.getuid()).child("lang").setValue(String.valueOf(locationClass.getlong()));
                    c.logoutwork();
                }
            }
        });

    }

    public void gettotalcount() {
        amountlistpending.clear();
        amountlistearn.clear();
        todayamountlistearn.clear();
        Query q = AppUtill.BILLINGURl.orderByChild("did").equalTo(new Control(getContext()).getuid());
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    int i = 0;
                    totalcount.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        BillingParameters b = data.getValue(BillingParameters.class);
                        if (b.getDate() != null) {
                            if (b.getDate().equals(new TimeDate(getContext()).getdate())) {
                                i += 1;
                                todayamountlistearn.add(b.getTotal_amount());
                            }
                        }
                        if (b.getSts().equals("pending")) {
                            amountlistpending.add(b.getTotal_amount());
                        } else if (b.getSts().equals("complete")) {
                            amountlistearn.add(b.getTotal_amount());
                        }
                    }
                    todaycount.setText(String.valueOf(i));
                    gettotalamount();
                } else {
                    totalcount.setText("0");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void gettotalamount() {

        float totalearn = (float) 0.0, totalpending = (float) 0.0, todayearn = (float) 0.0;

        for (int i = 0; i < amountlistpending.size(); i++) {
            float temp1 = Float.parseFloat(amountlistpending.get(i));
            totalpending += temp1;
        }

        for (int i = 0; i < amountlistearn.size(); i++) {
            float temp2 = Float.parseFloat(amountlistearn.get(i));
            totalearn += temp2;
        }
        for (int i = 0; i < todayamountlistearn.size(); i++) {
            float temp3 = Float.parseFloat(todayamountlistearn.get(i));
            todayearn += temp3;
        }

        todayearntxt.setText(String.valueOf(todayearn));
        totalearntxt.setText(String.valueOf(totalearn));
        totlapendingtxt.setText(String.valueOf(totalpending));
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

}
