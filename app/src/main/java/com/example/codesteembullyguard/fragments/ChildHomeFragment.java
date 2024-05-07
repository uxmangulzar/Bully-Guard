package com.example.codesteembullyguard.fragments;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.codesteembullyguard.activities.LoginChildrenActivity;
import com.example.codesteembullyguard.adapters.AppsAdapter;
import com.example.codesteembullyguard.helpers.Helper;
import com.example.codesteembullyguard.helpers.HelperKeys;
import com.example.codesteembullyguard.helpers.SessionManager;
import com.example.codesteembullyguard.helpers.Util;
import com.example.codesteembullyguard.models.AppsModel;
import com.example.codesteembullyguard.models.LoginResponseModel;
import com.example.codesteembullyguard.models.MainResponseModel;
import com.example.codesteembullyguard.models.ThirdItemModel;
import com.example.codesteembullyguard.adapters.ThirdRecyclerAdapter;
import com.example.codesteembullyguard.R;
import com.example.codesteembullyguard.network.ApiClient;
import com.example.codesteembullyguard.network.ApiInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChildHomeFragment extends Fragment {

    View view;
    Button need_talk;
    final static String TAG ="Child home";

    public ChildHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mood, container, false);

        ImageView profile = view.findViewById(R.id.profile);
        need_talk = view.findViewById(R.id.need_talk);
        ImageView navigation = view.findViewById(R.id.navigation);

        // Iterate through top5AppsUsage and access data for each app


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
//        uploadFile(0);
        if(!checkAccessibilityPermissions()) {
            boolean isServiceEnabled = isAccessibilityServiceEnabled(getActivity(), "com.example.codesteembullyguard/com.example.codesteembullyguard.helpers.AccessibilityService");
            if (!isServiceEnabled) {
                // The Accessibility Service is enabled
                setAccessibilityPermissions();
            }
        }
        return view;

    }
    public boolean isAccessibilityServiceEnabled(Context context, String accessibilityServiceName) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        String enabledServices = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        );
        Log.d("AccessibilityService", "Enabled Services: " + enabledServices);

        if (enabledServices != null) {
            String[] enabledServiceList = enabledServices.split(":");
            for (String enabledService : enabledServiceList) {
                ComponentName enabledServiceName = ComponentName.unflattenFromString(enabledService);
                if (enabledServiceName != null && enabledServiceName.flattenToString().equals(accessibilityServiceName)) {
                    return true;
                }
            }
        }

        return false;
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

    public boolean checkAccessibilityPermissions() {

        Log.d(TAG, "checkAccessibilityPermissions");
        AccessibilityManager accessibilityManager = (AccessibilityManager) getActivity().getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.DEFAULT);

        for (int i = 0; i < list.size(); i++) {
            AccessibilityServiceInfo info = list.get(i);

            if (info.getResolveInfo().serviceInfo.packageName.equals(getActivity().getApplication().getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public void setAccessibilityPermissions() {

        Log.d(TAG, "setAccessibilityPermissions");
        AlertDialog.Builder gsDialog = new AlertDialog.Builder(getActivity());
        gsDialog.setTitle("Your Safety");
        gsDialog.setMessage("For keeping your child safe we need to turn on Accessibility");
        gsDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 설정화면으로 보내는 부분
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                return;
            }
        }).create().show();
    }


}
