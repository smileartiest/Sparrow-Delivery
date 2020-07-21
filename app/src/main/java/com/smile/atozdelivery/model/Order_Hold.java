package com.smile.atozdelivery.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozdelivery.MapsActivity;
import com.smile.atozdelivery.R;
import com.smile.atozdelivery.controller.Addresparameters;
import com.smile.atozdelivery.controller.AppUtill;
import com.smile.atozdelivery.controller.Control;
import com.smile.atozdelivery.controller.DeliveryParameteres;
import com.smile.atozdelivery.controller.TimeDate;
import com.smile.atozdelivery.retrofit.ApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_Hold extends RecyclerView.ViewHolder {

    public Order_Hold(@NonNull View itemView) {
        super(itemView);
    }

    public void setdetails(final Context c1 , final String uid1 , final String id1 , String addid1 , String sts1 ){

        TextView oid = itemView.findViewById(R.id.rorder_oid);
        TextView amount = itemView.findViewById(R.id.rorder_amount);
        final TextView address = itemView.findViewById(R.id.rorder_address);
        final TextView cno = itemView.findViewById(R.id.rorder_cno);
        TextView conform = itemView.findViewById(R.id.rorder_conformorder);
        TextView viewmore = itemView.findViewById(R.id.rorder_viewmore);

        oid.setText(id1);
        //amount.setText(am1);

        DatabaseReference df = FirebaseDatabase.getInstance().getReference("address").child(uid1).child(addid1);
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    Addresparameters a = dataSnapshot.getValue(Addresparameters.class);
                    address.setText(a.getAddress());
                    cno.setText(a.getCno());
                }else {
                    address.setText("Address not found");
                    cno.setText("Contact not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendpushnotify(c1 , uid1 ,"I am tacking your order. deliver with in 15 minits.click to trak live");
                AppUtill.ORDERURl.child(id1).child("sts").setValue("pending");
                AppUtill.ORDERURl.child(id1).child("did").setValue(new Control(c1).getuid());
                AppUtill.BILLINGURl.child(id1).child("did").setValue(new Control(c1).getuid());
                AppUtill.BILLINGURl.child(id1).child("sts").setValue("pending");
                DatabaseReference DELIVERY = AppUtill.DELIVERYURl.child(id1);
                DeliveryParameteres d = new DeliveryParameteres(id1,id1,new Control(c1).getcno(),new Control(c1).getname(),"pending",new TimeDate(c1).getdate(),"none");
                DELIVERY.setValue(d);
            }
        });

        viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1.startActivity(new Intent(c1 , MapsActivity.class).putExtra("id", id1).putExtra("k","order").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

    }

    void sendpushnotify(final Context c1 ,String uid1 , String message){
        Call<String> call = ApiUtil.getServiceClass().sendpush(uid1,"Hai "+uid1,message);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Toast.makeText(c1, "Success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(c1, "Success", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("error",t.getMessage());
            }
        });
    }

}
