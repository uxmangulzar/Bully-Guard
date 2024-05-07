package com.example.codesteembullyguard.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codesteembullyguard.activities.ParentHomeActivity;
import com.example.codesteembullyguard.helpers.HelperKeys;
import com.example.codesteembullyguard.helpers.SessionManager;
import com.example.codesteembullyguard.helpers.Util;
import com.example.codesteembullyguard.models.ChildrenModel;
import com.example.codesteembullyguard.models.MainResponseModel;
import com.example.codesteembullyguard.models.NotificationsModel;
import com.example.codesteembullyguard.adapters.NotificationsAdapter;
import com.example.codesteembullyguard.R;
import com.example.codesteembullyguard.network.ApiClient;
import com.example.codesteembullyguard.network.ApiInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    View view;
    private TabLayout tabLayout1;
    private ImageView ivBack;
    private Button btn_load_more;
    private TextView tv_alert_history;
    private RecyclerView recyclerViewNotifications;
    private ChildrenModel childrenModel;
    private ArrayList<NotificationsModel> items;
    private int pageNo=0;
    private String selectedTab="message";
    private NotificationsAdapter notificationsAdapter;

    public NotificationsFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     view =inflater.inflate(R.layout.fragment_third, container, false);
        Bundle args = getArguments();
        if (args != null) {
            childrenModel = (ChildrenModel) args.getSerializable("model");

        }
        tabLayout1 = view.findViewById(R.id.tabLayout2);
        tv_alert_history = view.findViewById(R.id.tv_alert_history);
        btn_load_more = view.findViewById(R.id.btn_load_more);
        ivBack = view.findViewById(R.id.ivBack);
        TabLayout.Tab tab1 = tabLayout1.newTab().setText("Message Alert").setTag("tab1");

        // Add the tab to the TabLayout
        tabLayout1.addTab(tab1);
        TabLayout.Tab tab2 = tabLayout1.newTab().setText("Web Alert").setTag("tab2");
        tabLayout1.addTab(tab2);

        TooltipCompat.setTooltipText(tab1.view, "Message alert in your child protection app notifies parents or guardians of any message");
        TooltipCompat.setTooltipText(tab2.view, "Web alert alerts parents to potentially harmful websites or content accessed by child");
        items = new ArrayList<>();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), ParentHomeActivity.class));
                getActivity().finishAffinity();
            }
        });

        btn_load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getNotificationsOnParent(selectedTab, pageNo);
            }
        });

        tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Get the tag or identifier of the selected tab
                String selectedTabTag = (String) tab.getTag();

                // Now you have the selected tab's tag, you can use it as needed
                if ("tab1".equals(selectedTabTag)) {
                    // Handle tab 1 selection
                    selectedTab="message";
                    pageNo=0;
                    getNotificationsOnParent("message", 1);

                } else if ("tab2".equals(selectedTabTag)) {
                    // Handle tab 2 selection
                    selectedTab="web";
                    pageNo=0;

                    getNotificationsOnParent("web", 1);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

            // Other overridden methods
        });




        recyclerViewNotifications = view.findViewById(R.id.recyclerView);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getActivity()));
        notificationsAdapter=new NotificationsAdapter(getActivity(), items);
        recyclerViewNotifications.setAdapter(notificationsAdapter);
        getNotificationsOnParent("message",1);


     return view;
    }
    private void getNotificationsOnParent(String type, int pageNumber) {
        AlertDialog dialog = Util.progressDialog(getActivity());
        dialog.show();
        ApiClient apiClient = new ApiClient();
        ApiInterface apiInterface = apiClient.getClient().create(ApiInterface.class);
        Call<MainResponseModel> responseModelCall;
        if (childrenModel != null) {
            // You now have access to the ChildrenModel object
            // Use it as needed
            responseModelCall = apiInterface.get_all_notification_by_type(
                    childrenModel.getId().toString(),type,pageNumber,5
            );
        }else{
           responseModelCall = apiInterface.get_all_notification_by_type(
                    SessionManager.getStringPref(HelperKeys.USER_ID,getActivity()),type,pageNumber,5
            );
        }


        responseModelCall.enqueue(new Callback<MainResponseModel>() {
            @Override
            public void onResponse(Call<MainResponseModel> call, Response<MainResponseModel> response) {
                dialog.dismiss();
                MainResponseModel data11 = response.body();
                if (data11 != null && data11.getStatus()==200) {
                    List<NotificationsModel> listOne=new ArrayList<>();
                    listOne=data11.getAllNotifications();
                    if (listOne.size()!=0){
                        items.addAll(listOne);
                    }else {
                        items.clear();
                    }
                    notificationsAdapter.notifyDataSetChanged();

                    Integer totalPages = data11.getTotalPages();

                    if (pageNumber<totalPages){
                        btn_load_more.setVisibility(View.VISIBLE);
                        pageNo=pageNumber+1;
                    }else {
                        btn_load_more.setVisibility(View.GONE);
                        pageNo=0;

                    }
                } else {
                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponseModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
            }
        });
    }

}