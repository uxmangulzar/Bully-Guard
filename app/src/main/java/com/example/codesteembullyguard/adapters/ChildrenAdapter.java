package com.example.codesteembullyguard.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.codesteembullyguard.activities.SingleChildActivity;
import com.example.codesteembullyguard.models.ChildrenModel;
import com.example.codesteembullyguard.network.ApiClient;
import com.example.codesteembullyguard.viewholders.FifthMyViewHolder;
import com.example.codesteembullyguard.R;

import java.util.List;

public class ChildrenAdapter extends RecyclerView.Adapter<FifthMyViewHolder> {


    Activity context;
    int frameLayout;
    List<ChildrenModel> items;


    public ChildrenAdapter(Activity context, List<ChildrenModel> items, int frameLayout) {
        this.context = context;
        this.items = items;
        this.frameLayout = frameLayout;
    }

    @NonNull
    @Override
    public FifthMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FifthMyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view5,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  FifthMyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Glide.with(context)
                .load(ApiClient.BASE_URL+"/files/"+items.get(position).getUserImage())
                .placeholder(R.drawable.c3) // Optional placeholder image
                .error(R.drawable.c3) // Optional error image
                .into(holder.imageView);

        holder.tv_child_name.setText(items.get(position).getFirstName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(context,SingleChildActivity.class);
                intent1.putExtra("model",items.get(position));
                context.startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


}
