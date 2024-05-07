package com.example.codesteembullyguard.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.codesteembullyguard.activities.SliderActivity;
import com.example.codesteembullyguard.adapters.ParentFragmentAdapter;
import com.example.codesteembullyguard.R;
import com.google.android.material.tabs.TabLayout;


public class SecondFragment extends Fragment {

    View view;
    private TabLayout tabLayout1;
    private ViewPager2 viewPager3;
    private ParentFragmentAdapter adapter1;
    ImageView child_filter;


    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_second, container, false);


        tabLayout1 = view.findViewById(R.id.tabLayout1);
        viewPager3 = view.findViewById(R.id.viewPager3);
        child_filter = view.findViewById(R.id.child_filter);

        child_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SliderActivity.class);
                startActivity(i);
            }
        });

        tabLayout1.addTab(tabLayout1.newTab().setText("Message Alert"));
        tabLayout1.addTab(tabLayout1.newTab().setText("Web Alert"));

        FragmentManager fragmentManager = getFragmentManager();
//        adapter1 = new ParentFragmentAdapter(fragmentManager , getLifecycle(), childrenModel);
//        viewPager3.setAdapter(adapter1);

        tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager3.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager3.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout1.selectTab(tabLayout1.getTabAt(position));
            }
        });



        return view;
    }
}