package com.example.firebase;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerView extends RecyclerView.ViewHolder implements View.OnClickListener {


    TextView txttitle,txtcontent,teksIpk,teksSmt;


    public void setItemClicked(ItemClicked itemClicked) {
        this.itemClicked = itemClicked;
    }

    ItemClicked itemClicked;
    public MyRecyclerView(@NonNull View itemView) {
        super(itemView);

        txtcontent = itemView.findViewById(R.id.txtcontent);
        txttitle= itemView.findViewById(R.id.txttitle);
        teksIpk = itemView.findViewById(R.id.teksIpk);
        teksSmt = itemView.findViewById(R.id.teksSmt);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        itemClicked.onClick(view, getAdapterPosition());

    }
}
