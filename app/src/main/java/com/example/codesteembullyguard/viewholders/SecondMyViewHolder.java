package com.example.codesteembullyguard.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codesteembullyguard.R;

public class SecondMyViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView nameView,timeView,deviceView;

    public SecondMyViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.imageview);
        nameView = itemView.findViewById(R.id.name);
        timeView = itemView.findViewById(R.id.time);
        deviceView = itemView.findViewById(R.id.device);

    }
}
