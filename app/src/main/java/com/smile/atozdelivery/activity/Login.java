package com.smile.atozdelivery.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozdelivery.R;
import com.smile.atozdelivery.controller.Control;
import com.smile.atozdelivery.controller.Move_Show;

public class Login extends AppCompatActivity {

    TextInputLayout phno,pass;
    Button loginbtn;
    Query qbase;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        phno = findViewById(R.id.login_phno);
        pass = findViewById(R.id.login_pass);

        loginbtn = findViewById(R.id.login_loginbtn);

        pd = new ProgressDialog(this);
        pd.setTitle("Still Wait..!");
        pd.setMessage("Loading Please wait...");

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(new Control(Login.this).getsts().equals("login")){
            startActivity(new Intent(Login.this , LoginMain.class));
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                final String phno1 = phno.getEditText().getText().toString();
                final String pass1 = pass.getEditText().getText().toString();
                if(phno1.length()!=0 && phno1.length()==10){
                    if(pass1.length()!=0){
                        qbase = FirebaseDatabase.getInstance().getReference("employe").orderByChild("eusname").equalTo(phno1);
                        qbase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue()!=null){
                                    if(dataSnapshot.getChildrenCount()==1){
                                        for(DataSnapshot data : dataSnapshot.getChildren()){
                                            if(pass1.equals(data.child("epass").getValue().toString())){
                                                Control c = new Control(Login.this);
                                                c.adduid(data.child("id").getValue().toString());
                                                c.addcno(phno1);
                                                c.addsts("login");
                                                pd.dismiss();
                                                startActivity(new Intent(Login.this , LoginMain.class));
                                                finish();
                                            }else {
                                                pd.dismiss();
                                                Move_Show.showToast(Login.this , "Incorrect User or Password");
                                            }
                                        }
                                    }
                                    else {
                                        pd.dismiss();
                                        Move_Show.showToast(Login.this , "Multiple Account's Found");
                                    }
                                }else {
                                    pd.dismiss();
                                    Move_Show.showToast(Login.this , "No Account Found This Number");
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            pd.dismiss();
                                Log.d("login error",databaseError.getMessage());
                            }
                        });
                    }else {
                        pd.dismiss();
                        pass.getEditText().setError("Please fill elements");}
                }else {
                    pd.dismiss();
                    phno.getEditText().setError("Please fill elements"); }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
