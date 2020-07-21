package com.smile.atozdelivery.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.smile.atozdelivery.R;
import com.smile.atozdelivery.controller.Control;
import com.smile.atozdelivery.controller.Move_Show;
import com.smile.atozdelivery.fragment.Home;
import com.smile.atozdelivery.fragment.Myorder;
import com.smile.atozdelivery.fragment.Order;
import com.smile.atozdelivery.retrofit.ApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginMain extends AppCompatActivity {

    Toolbar myactionbar;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    ConstraintLayout screen;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.bot_nav_home:
                    loadfragment(new Home());
                    getSupportActionBar().show();
                    return true;
                case R.id.bot_nav_orders:
                    loadfragment(new Order());
                    getSupportActionBar().hide();
                    return true;
                case R.id.bot_nav_myorders:
                    loadfragment(new Myorder());
                    getSupportActionBar().hide();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        myactionbar = findViewById(R.id.toolbar);
        setSupportActionBar(myactionbar);
        screen = findViewById(R.id.main_screen);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if(!checkLocationPermission()){
            checkLocationPermission();
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM ID ", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("FCM ID ", token);
                        addtoken(token);
                    }
                });
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        loadfragment(new Home());

    }

    void addtoken(String token) {
        Call<String> call = ApiUtil.getServiceClass().updateid(new Control(LoginMain.this).getuid() , token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    if(response.body().equals("1")){
                        Toast.makeText(LoginMain.this, "Update successfull", Toast.LENGTH_SHORT).show();
                    }else if(response.body().equals("10")){
                        Toast.makeText(LoginMain.this, "Update error", Toast.LENGTH_SHORT).show();
                    }else if(response.body().equals("2")){
                        Toast.makeText(LoginMain.this, "insert successfull", Toast.LENGTH_SHORT).show();
                    }else if(response.body().equals("20")){
                        Toast.makeText(LoginMain.this, "insert error", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginMain.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginMain.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadfragment(Fragment frag){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout , frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.loginmenu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.log_menu_refres:
                return true;
            case R.id.log_menu_logout:
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this);
                builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
                builder.setIcon(R.drawable.sparrowiconsmall);
                builder.setCornerRadius(20);
                builder.setTitle("Hey , There !");
                builder.setMessage("Are You sure, want to close this app ?");
                builder.addButton("EXIT", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new Control(LoginMain.this).clearmemory();
                        new Move_Show(LoginMain.this , Login.class);
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                });
                builder.addButton("NOT NOW", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onBackPressed() {
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this);
        builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        builder.setIcon(R.drawable.sparrowiconsmall);
        builder.setCornerRadius(20);
        builder.setTitle("Hey , There !");
        builder.setMessage("Are You sure, want to close this app ?");
        builder.addButton("EXIT", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finishAffinity();
            }
        });
        builder.addButton("NOT NOW", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
