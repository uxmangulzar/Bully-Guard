package com.example.codesteembullyguard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codesteembullyguard.models.ThirdItemModel;
import com.example.codesteembullyguard.viewholders.ThirdMyViewHolder;
import com.example.codesteembullyguard.R;
import com.example.codesteembullyguard.models.ThirdItemModel;
import com.example.codesteembullyguard.viewholders.ThirdMyViewHolder;

import java.util.List;

public class ThirdRecyclerAdapter extends RecyclerView.Adapter<ThirdMyViewHolder> {


    Context context;
    List<ThirdItemModel> items;


    public ThirdRecyclerAdapter(Context context, List<ThirdItemModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ThirdMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ThirdMyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view3,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  ThirdMyViewHolder holder, int position) {

        holder.imageView.setImageDrawable(items.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
