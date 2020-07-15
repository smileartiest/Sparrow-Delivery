package com.smile.atozdelivery;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozdelivery.controller.Addresparameters;
import com.smile.atozdelivery.controller.AppUtill;
import com.smile.atozdelivery.controller.OrderParametrs;
import com.smile.atozdelivery.controller.TimeDate;
import com.smile.atozdelivery.retrofit.ApiUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView namelist, nodelist, pricelist, bamount, pmode, csname;
    FloatingActionButton callbtn;
    String phno;
    Button completebtn;
    private static final int MY_PERMISSIONS_REQUEST_CALL = 1;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    StringBuilder servlist1;
    StringBuilder qntlist1;
    StringBuilder amlist1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        namelist = findViewById(R.id.map_itemnamelist);
        nodelist = findViewById(R.id.map_itemnodelist);
        pricelist = findViewById(R.id.map_itempricelist);
        bamount = findViewById(R.id.map_itemamount);
        pmode = findViewById(R.id.map_itempmode);
        csname = findViewById(R.id.map_cname);
        callbtn = findViewById(R.id.map_callbtn);
        completebtn = findViewById(R.id.map_completebtn);
        completebtn.setVisibility(View.GONE);

        if (getIntent().getStringExtra("k").equals("myorder")) {
            completebtn.setVisibility(View.VISIBLE);
        }

        AppUtill.ORDERURl.child(getIntent().getStringExtra("id")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    OrderParametrs o = dataSnapshot.getValue(OrderParametrs.class);

                    String name1 = o.getName();
                    String qnt1 = o.getQnt();
                    String size1 = o.getSize();
                    String am1 = o.getAm();

                    String[] tempnamlist = name1.split(",");
                    String[] tempqntlist = qnt1.split(",");
                    String[] tempamlist = am1.split(",");
                    String[] tempsizelist = size1.split(",");

                    servlist1 = new StringBuilder();
                    qntlist1 = new StringBuilder();
                    amlist1 = new StringBuilder();

                    for (int i = 0; i < tempnamlist.length; i++) {
                        servlist1.append(tempnamlist[i] + "\n\n");
                        qntlist1.append(tempqntlist[i]+"\n\n");
                        amlist1.append((Integer.parseInt(tempqntlist[i]) * Integer.parseInt(tempamlist[i])) + "\n\n");
                    }

                    namelist.setText(servlist1.toString());
                    nodelist.setText(qntlist1.toString());
                    pricelist.setText(amlist1.toString());

                    bamount.setText("Rs . " + o.getBam());
                    pmode.setText(o.getPmode());
                    if (o.getSts().equals("complete")) {
                        completebtn.setVisibility(View.GONE);
                        callbtn.setVisibility(View.GONE);
                    }
                    getMapdetails(o.getUid(), o.getAddres());
                    getcustomerdetails(o.getUid());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    void getcustomerdetails(String id) {
        AppUtill.REGURL.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    csname.setText(dataSnapshot.child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void getMapdetails(String uid, String id) {

        AppUtill.ADDRESURL.child(uid).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Addresparameters a = dataSnapshot.getValue(Addresparameters.class);
                    phno = a.getCno();
                    LatLng userpoint = new LatLng(Double.parseDouble(a.getLat()), Double.parseDouble(a.getLang()));
                    mMap.addMarker(new MarkerOptions().position(userpoint).title("USER").icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.usloc_icon)));
                    mMap.setIndoorEnabled(true);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userpoint, 15f));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        completebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(MapsActivity.this);
                d.setContentView(R.layout.dialog_complete);
                d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageView img = d.findViewById(R.id.complete_dialog_image);
                Button complete = d.findViewById(R.id.complete_dialog_cbtn);
                ImageView close = d.findViewById(R.id.complete_dialog_close);

                close.setImageResource(R.drawable.close_icon);
                img.setImageResource(R.drawable.collectcash);

                complete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                        sendpushnotify(getApplicationContext() , phno , "Delivered your order. Thank your for your valuable order. Please give rating.");
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.show();

            }
        });

        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phno.length() != 0 && phno.length() == 10) {
                    if (checkcallPermission()) {
                        Intent in = new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + phno));
                        startActivity(in);
                    }else{
                        checkcallPermission();
                    }
                }else{
                    opendialog("Oops !","Don't make a call");
                }
            }
        });

    }

    public void opendialog(String tit ,String messg){
        final Dialog d = new Dialog(MapsActivity.this);
        d.setContentView(R.layout.dialog_oops);
        TextView message = d.findViewById(R.id.dopps_message);
        TextView title = d.findViewById(R.id.dopps_title);
        TextView okbtn = d.findViewById(R.id.dopps_okbtn);
        title.setText(tit);
        message.setText(messg);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
            }
        });
        d.setCanceledOnTouchOutside(false);
        d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.show();
    }

    public boolean checkcallPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL);
            }
            return false;
        } else {
            return true;
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    void sendpushnotify(final Context c1 ,String uid1 , String message){
        Call<String> call = ApiUtil.getServiceClass().sendpush(uid1,"Hai "+uid1,message);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Toast.makeText(c1, "Success", Toast.LENGTH_SHORT).show();
                    AppUtill.ORDERURl.child(getIntent().getStringExtra("id")).child("sts").setValue("complete");
                    AppUtill.ORDERURl.child(getIntent().getStringExtra("id")).child("ddate").setValue(new TimeDate(MapsActivity.this).getdate());
                    finish();
                }else{
                    Toast.makeText(c1, "Success", Toast.LENGTH_SHORT).show();
                    AppUtill.ORDERURl.child(getIntent().getStringExtra("id")).child("sts").setValue("complete");
                    AppUtill.ORDERURl.child(getIntent().getStringExtra("id")).child("ddate").setValue(new TimeDate(MapsActivity.this).getdate());
                    finish();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("error",t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
