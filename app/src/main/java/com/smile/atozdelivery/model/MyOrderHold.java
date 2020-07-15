package com.smile.atozdelivery.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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

public class MyOrderHold extends RecyclerView.ViewHolder {

    public MyOrderHold(@NonNull View itemView) {
        super(itemView);
    }

    public void setdetails(final Context c1 , String uid1 ,final String id1 , String am1 , String addid1 , String sts1 ){

        TextView oid = itemView.findViewById(R.id.rorder_oid);
        TextView amount = itemView.findViewById(R.id.rorder_amount);
        final TextView address = itemView.findViewById(R.id.rorder_address);
        final TextView cno = itemView.findViewById(R.id.rorder_cno);
        TextView conform = itemView.findViewById(R.id.rorder_conformorder);
        TextView viewmore = itemView.findViewById(R.id.rorder_viewmore);
        ImageView img = itemView.findViewById(R.id.rorder_img);
        ConstraintLayout bgscreen = itemView.findViewById(R.id.rorder_stsbg);

        img.setImageResource(R.drawable.order_icon_green);

        oid.setText(id1);
        amount.setText(am1);

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

        conform.setText("CANCEL");

        if(sts1.equals("complete")){
            bgscreen.setBackgroundResource(R.color.green);
            img.setImageResource(R.drawable.complete_icon);
            conform.setVisibility(View.INVISIBLE);
        }else if(sts1.equals("pending")) {
            bgscreen.setBackgroundResource(R.color.yellow);
        }

        conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtill.ORDERURl.child(id1).child("did").removeValue();
                AppUtill.ORDERURl.child(id1).child("sts").setValue("taken");
            }
        });

        viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1.startActivity(new Intent(c1 , MapsActivity.class).putExtra("id", id1).putExtra("k","myorder").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

    }

}
