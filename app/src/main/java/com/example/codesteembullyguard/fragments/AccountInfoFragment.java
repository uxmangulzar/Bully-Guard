package com.example.codesteembullyguard.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.codesteembullyguard.helpers.Helper;
import com.example.codesteembullyguard.helpers.HelperKeys;
import com.example.codesteembullyguard.helpers.SessionManager;
import com.example.codesteembullyguard.helpers.Util;
import com.example.codesteembullyguard.models.ThirdItemModel;
import com.example.codesteembullyguard.adapters.ThirdRecyclerAdapter;
import com.example.codesteembullyguard.R;
import com.example.codesteembullyguard.adapters.ThirdRecyclerAdapter;
import com.example.codesteembullyguard.models.ThirdItemModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kotlin.Pair;


public class AccountInfoFragment extends Fragment {

    View view;




    public AccountInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account_info, container, false);


        TextView tvGender = view.findViewById(R.id.tvGender);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvAge = view.findViewById(R.id.tvAge);
        TextView tvPhone = view.findViewById(R.id.tvPhone);
        TextView tvAddress = view.findViewById(R.id.tvAddress);
        TextView tvSchool = view.findViewById(R.id.tvSchool);
        tvName.setText(SessionManager.getStringPref(HelperKeys.USER_FIRST,getContext())+" "+SessionManager.getStringPref(HelperKeys.USER_LAST,getContext()));

        tvPhone.setText(formatPhoneNumber(SessionManager.getStringPref(HelperKeys.PHONE,getContext())));
        tvSchool.setText(SessionManager.getStringPref(HelperKeys.SCHOOL,getContext()));
        tvGender.setText(SessionManager.getStringPref(HelperKeys.GENDER,getContext()));
        tvAddress.setText(SessionManager.getStringPref(HelperKeys.ADDRESS,getContext()));
        tvAge.setText(calculateAge(SessionManager.getStringPref(HelperKeys.DOB,getContext()))+" Years | "+SessionManager.getStringPref(HelperKeys.DOB,getContext()));


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        List<ThirdItemModel> items = new ArrayList<>();
        Context ApplicationContext = requireContext().getApplicationContext();

        List<kotlin.Pair<String, Long>> top5AppsUsage = Helper.INSTANCE.getTop5AppsOnDate(getContext(), Util.getCurrentDate());
        for (Pair<String, Long> appUsage : top5AppsUsage) {
            String packageName = appUsage.getFirst();
            long usageTimeMinutes = appUsage.getSecond();

            // Now you can work with packageName and usageTimeMinutes
            // For example, print the package name and usage time:
            System.out.println("Package Name: " + packageName);
            System.out.println("Usage Time (Minutes): " + usageTimeMinutes);

            Drawable appIcon = getAppIcon(getContext(), packageName);
            items.add(new ThirdItemModel(appIcon));
        }

        LinearLayoutManager HorizontalLayout = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(new ThirdRecyclerAdapter(ApplicationContext, items));

        return view;
    }
    public static int calculateAge(String dob) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        try {
            Date dateOfBirth = dateFormat.parse(dob);
            Calendar dobCalendar = Calendar.getInstance();
            dobCalendar.setTime(dateOfBirth);

            Calendar currentCalendar = Calendar.getInstance();

            int age = currentCalendar.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);

            // Adjust age if the birthday hasn't occurred yet this year
            if (currentCalendar.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            return age;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Handle parsing errors
        }
    }

    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return "";
        }

        // Remove all non-numeric characters from the phone number
        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");

        // Check if the phone number has at least 7 digits (e.g., 1234567)
        if (phoneNumber.length() >= 7) {
            // Format the phone number with dashes (e.g., 123-456-7890)
            return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
        } else {
            // Return the original phone number if it's too short to format
            return phoneNumber;
        }
    }
    public static Drawable getAppIcon(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent launchIntent = packageManager.getLaunchIntentForPackage(packageName);

            if (launchIntent != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                return applicationInfo.loadIcon(packageManager);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null; // Return null if the app icon could not be retrieved.
    }

}