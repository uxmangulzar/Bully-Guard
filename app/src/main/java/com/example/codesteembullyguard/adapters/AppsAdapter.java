package com.example.codesteembullyguard.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.codesteembullyguard.helpers.Util;
import com.example.codesteembullyguard.models.AppsModel;
import com.example.codesteembullyguard.models.MainResponseModel;
import com.example.codesteembullyguard.network.ApiClient;
import com.example.codesteembullyguard.network.ApiInterface;
import com.example.codesteembullyguard.viewholders.FourthMyViewHolder;
import com.example.codesteembullyguard.R;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppsAdapter extends RecyclerView.Adapter<FourthMyViewHolder> {


    Activity context;
    List<AppsModel> items;
    String fromTime="";
    String toTime="";
    public AppsAdapter(Activity context, List<AppsModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public FourthMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FourthMyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view4,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FourthMyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.nameView.setText(items.get(position).getApp_name());
        if (items.get(position).getSet_time().equals("0")){
            holder.timeView.setText("Set Time");
        }else{
            if (!items.get(position).getFrom_time().equals("0")){
                holder.timeView.setText(
                        items.get(position).getFrom_time()+"-"+
                        items.get(position).getTo_time()+"\n"+
                        items.get(position).getSet_time()+" mins");

            }else{
                holder.timeView.setText(items.get(position).getSet_time()+" mins");

            }

        }
        Glide.with(context)
                .load(ApiClient.BASE_URL+"/files/"+items.get(position).getApp_image())
                .placeholder(R.drawable.j1) // Optional placeholder image
                .error(R.drawable.j1) // Optional error image
                .into(holder.imageView);

        holder.tvSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeRangePickerDialog(context,items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public static String convertMinutesToHHMM(int minutes) {
        long milliseconds = TimeUnit.MINUTES.toMillis(minutes);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date resultDate = new Date(milliseconds);
        return sdf.format(resultDate);
    }

    private void updateAppTime(String appId, String setTime, String fromTime, String toTime) {
        AlertDialog dialog = Util.progressDialog(context);
        dialog.show();
        ApiClient apiClient = new ApiClient();
        ApiInterface apiInterface = apiClient.getClient().create(ApiInterface.class);

        Call<MainResponseModel> responseModelCall = apiInterface.update_app_time(
                appId,setTime,fromTime,toTime
        );

        responseModelCall.enqueue(new Callback<MainResponseModel>() {
            @Override
            public void onResponse(Call<MainResponseModel> call, Response<MainResponseModel> response) {
                dialog.dismiss();
                MainResponseModel data11 = response.body();
                if (data11 != null && data11.getStatus()==200) {
                    Toast.makeText(context, "Time set Successfully", Toast.LENGTH_SHORT).show();
                    context.finish();

                } else {
                    Toast.makeText(context, "Not able to set", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponseModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, "Not able to set", Toast.LENGTH_SHORT).show();
            }
        });
    }

public void showTimeRangePickerDialog(Context context, AppsModel appsModel) {

    // Create a TimePickerDialog with no initial view
    TimePickerDialog timeRangePickerDialog = new TimePickerDialog(
            context,
            R.style.TimeRangePickerDialog, // Define a custom style for the dialog
            null,
            0, // Initial hour value (ignored)
            0, // Initial minute value (ignored),
            false // 24-hour format
    );

    // Inflate the custom layout for the dialog
    View dialogView = View.inflate(context, R.layout.custom_time_range_picker_dialog, null);
    timeRangePickerDialog.setView(dialogView);

    // Get references to the EditText for usage limit and the "Select Time Range" button
    ImageView return_back = dialogView.findViewById(R.id.return_back);
    EditText usageLimitInput = dialogView.findViewById(R.id.usageLimitInput);
    TextView selectButton = dialogView.findViewById(R.id.selectButton);
    Button bt_submit = dialogView.findViewById(R.id.bt_submit);
    bt_submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            timeRangePickerDialog.dismiss();
            if (!selectButton.getText().toString().equals("")){
                updateAppTime(
                        appsModel.getApp_id().toString(),
                        String.valueOf(Integer.parseInt(usageLimitInput.getText().toString().trim())*60),
                        fromTime,
                        toTime

                );
            }


        }
    });
    return_back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            timeRangePickerDialog.dismiss();
        }
    });
    // Set a listener for the "Select Time Range" button
    selectButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Retrieve the entered usage limit
            String usageLimitStr = usageLimitInput.getText().toString().trim();
            int usageLimit = 0;

            // Check if the usage limit is valid
            if (!usageLimitStr.isEmpty()) {
                try {
                    usageLimit = Integer.parseInt(usageLimitStr);
                    if (usageLimit < 1 || usageLimit > 24) {
                        // Invalid usage limit, show an error message
                        Toast.makeText(context, "Invalid usage limit. Enter a number between 1 and 24.", Toast.LENGTH_SHORT).show();
                        return; // Do not proceed further
                    }
                } catch (NumberFormatException e) {
                    // Invalid input, show an error message
                    Toast.makeText(context, "Invalid usage limit. Enter a number between 1 and 24.", Toast.LENGTH_SHORT).show();
                    return; // Do not proceed further
                }
            } else {
                // Usage limit field is empty, show an error message
                Toast.makeText(context, "Usage limit cannot be empty.", Toast.LENGTH_SHORT).show();
                return; // Do not proceed further
            }

            // Once the usage limit is entered, show the TimePicker for selecting the time range
            showTimeRangePicker1(context, appsModel, usageLimit,selectButton);
        }
    });

    // Show the time range picker dialog
    timeRangePickerDialog.show();
}

    public void showTimeRangePicker1(Context context, AppsModel appsModel, int usageLimit, TextView selectButton) {
        // Get the current time
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        // Create a TimePickerDialog for selecting the start time
        TimePickerDialog startTimePickerDialog = new TimePickerDialog(
                context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        // Handle the selected start time
                        String startTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        fromTime=startTime;
                        // Store the selected start time and then show the end time picker
                        showEndTimePickerDialog(context, appsModel, startTime,usageLimit,selectButton);
                    }
                },
                hour, // Initial hour value
                minute, // Initial minute value
                false // 24-hour format (true for 24-hour format, false for AM/PM)
        );

        // Show the time picker dialog for selecting the start time
        startTimePickerDialog.show();
    }

    public void showEndTimePickerDialog(Context context, AppsModel appsModel, String startTime, int usageLimit, TextView selectButton) {
        // Get the current time
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        // Create a TimePickerDialog for selecting the end time
        TimePickerDialog endTimePickerDialog = new TimePickerDialog(
                context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                        // Handle the selected end time
                        String endTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        // Now, you have both start and end times, you can use them as needed
                        String timeRange = startTime + " - " + endTime;
                        toTime=endTime;
//                        Toast.makeText(context, "Selected time range: " + timeRange, Toast.LENGTH_SHORT).show();
                        // You can perform further actions with the time range, like saving it to your model
                        selectButton.setText(timeRange);
                    }
                },
                hour, // Initial hour value
                minute, // Initial minute value
                false // 24-hour format (true for 24-hour format, false for AM/PM)
        );

        // Show the time picker dialog for selecting the end time
        endTimePickerDialog.show();
    }




    public int timeToMinutes(int hours, int minutes) {
        return (hours * 60) + minutes;
    }

}
