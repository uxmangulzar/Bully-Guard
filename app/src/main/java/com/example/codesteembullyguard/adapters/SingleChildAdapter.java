package com.example.codesteembullyguard.adapters;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.codesteembullyguard.models.NotificationsModel;
import com.example.codesteembullyguard.models.SecondItemModel;
import com.example.codesteembullyguard.network.ApiClient;
import com.example.codesteembullyguard.viewholders.SecondMyViewHolder;
import com.example.codesteembullyguard.R;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SingleChildAdapter extends RecyclerView.Adapter<SecondMyViewHolder> {


    Context context;
    List<NotificationsModel> items;

    public SingleChildAdapter(Context context, List<NotificationsModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public SecondMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SecondMyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view2,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  SecondMyViewHolder holder, int position) {


        holder.nameView.setText(items.get(position).getTitle());
        if (items.get(position).getNotificationDateTime()!=null){

            holder.timeView.setText(getTimeAgo(items.get(position).getNotificationDateTime()));
        }


//        app image
        Glide.with(context)
                .load(ApiClient.BASE_URL+"/files/"+items.get(position).getAppIcon())
                .placeholder(R.drawable.j1) // Optional placeholder image
                .error(R.drawable.j1) // Optional error image
                .into(holder.imageView);
        holder.deviceView.setText(items.get(position).getDeviceType());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public static String getTimeAgo(String dateString) {
        try {
            // Define the format of the input date string
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);

            // Parse the input string to a Date object
            Date inputDate = sdf.parse(dateString);

            // Get the current date and time
            Date currentDate = new Date();

            // Calculate the time difference in milliseconds
            long diffMillis = currentDate.getTime() - inputDate.getTime();

            // Calculate years, months, weeks, days, hours, and minutes
            long years = diffMillis / (1000L * 60 * 60 * 24 * 365);
            long months = diffMillis / (1000L * 60 * 60 * 24 * 30);
            long weeks = diffMillis / (1000L * 60 * 60 * 24 * 7);
            long days = diffMillis / (1000L * 60 * 60 * 24);
            long hours = diffMillis / (1000L * 60 * 60);
            long minutes = diffMillis / (1000L * 60);

            // Construct the time ago string
            StringBuilder timeAgo = new StringBuilder();
            if (years > 0) {
                timeAgo.append(years).append(" year").append(years > 1 ? "s" : "").append(" ago");
            } else if (months > 0) {
                timeAgo.append(months).append(" month").append(months > 1 ? "s" : "").append(" ago");
            } else if (weeks > 0) {
                timeAgo.append(weeks).append(" week").append(weeks > 1 ? "s" : "").append(" ago");
            } else if (days > 0) {
                timeAgo.append(days).append(" day").append(days > 1 ? "s" : "").append(" ago");
            } else if (hours > 0) {
                timeAgo.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(" ago");
            } else {
                timeAgo.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(" ago");
            }

            return timeAgo.toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

}
