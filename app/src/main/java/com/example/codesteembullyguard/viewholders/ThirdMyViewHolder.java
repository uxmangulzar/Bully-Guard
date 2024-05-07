package com.example.codesteembullyguard.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codesteembullyguard.R;

public class ThirdMyViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;

    public ThirdMyViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageview);

    }
}
