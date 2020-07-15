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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozdelivery.R;
import com.smile.atozdelivery.controller.AppUtill;
import com.smile.atozdelivery.controller.Control;
import com.smile.atozdelivery.controller.OrderParametrs;
import com.smile.atozdelivery.model.Order_Hold;

public class Order extends Fragment {

    View v;
    RecyclerView list;
    Query q;
    ProgressDialog pd;
    ConstraintLayout norder;

    private static String TAG = "order";

    public Order() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_order, container, false);

        list = v.findViewById(R.id.order_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        norder = v.findViewById(R.id.order_norder);

        pd = new ProgressDialog(getContext());
        pd.setTitle("Loading");
        pd.setMessage("fetching Please wait.....");

        if (new Control(getContext()).getwork() != null) {
            if (new Control(getContext()).getwork().equals("login")) {
                q = AppUtill.ORDERURl.orderByChild("sts").equalTo("taken");
                viewdetails();
                getdetails(q);
            } else {
                norder.setVisibility(View.VISIBLE);
            }
        }

        return v;
    }

    void viewdetails() {
        FirebaseRecyclerAdapter<OrderParametrs, Order_Hold> frecycle = new FirebaseRecyclerAdapter<OrderParametrs, Order_Hold>(
                OrderParametrs.class, R.layout.row_order, Order_Hold.class, q
        ) {
            @Override
            protected void populateViewHolder(Order_Hold order_hold, OrderParametrs op, int i) {
                order_hold.setdetails(getContext(), op.getUid(), op.getId(), op.getBam(), op.getAddres(), op.getSts());
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
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }

}
