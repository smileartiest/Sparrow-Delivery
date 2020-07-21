package com.smile.atozdelivery.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozdelivery.R;
import com.smile.atozdelivery.controller.Control;
import com.smile.atozdelivery.controller.OrderParametrs;
import com.smile.atozdelivery.model.MyOrderHold;

public class Myorder extends Fragment {

    View v;
    RecyclerView list;
    ProgressDialog pd;
    ConstraintLayout norder;
    Query q;

    private static String TAG = "myorder";

    public Myorder() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_myorder , container , false);
        list = v.findViewById(R.id.myorder_list);
        norder = v.findViewById(R.id.myorder_norder);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setHasFixedSize(true);

        pd = new ProgressDialog(getContext());
        pd.setTitle("Loading");
        pd.setMessage("fetching Please wait.....");
        pd.show();

        q = FirebaseDatabase.getInstance().getReference("order").orderByChild("did").equalTo(new Control(getContext()).getuid());

        getdetails(q);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<OrderParametrs, MyOrderHold> frecycle = new FirebaseRecyclerAdapter<OrderParametrs, MyOrderHold>(
                OrderParametrs.class , R.layout.row_order , MyOrderHold.class , q
        ) {
            @Override
            protected void populateViewHolder(MyOrderHold order_hold, OrderParametrs op, int i) {
                order_hold.setdetails(getContext() ,op.getUid() ,op.getId() , op.getAddres() , op.getSts());
            }
        };
        pd.dismiss();
        list.setAdapter(frecycle);

    }

    void getdetails(Query q1) {
        q1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    norder.setVisibility(View.GONE);
                } else {
                    norder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG , databaseError.getMessage());
            }
        });
    }

}

