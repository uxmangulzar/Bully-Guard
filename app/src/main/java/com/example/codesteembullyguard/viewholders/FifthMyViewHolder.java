package com.example.codesteembullyguard.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codesteembullyguard.R;

public class FifthMyViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView tv_child_name;

    public FifthMyViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageview);
        tv_child_name = itemView.findViewById(R.id.tv_child_name);

    }
}
