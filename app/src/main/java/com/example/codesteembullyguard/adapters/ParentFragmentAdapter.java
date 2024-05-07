package com.example.codesteembullyguard.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.codesteembullyguard.fragments.MessageAlertFragment;
import com.example.codesteembullyguard.fragments.WebAlertFragment;
import com.example.codesteembullyguard.models.ChildrenModel;

public class ParentFragmentAdapter extends FragmentStateAdapter {
    ChildrenModel childrenModel;
    public ParentFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ChildrenModel childrenModel) {
        super(fragmentManager, lifecycle);
        this.childrenModel=childrenModel;
    }

    public Fragment createFragment(int position) {

        if (position == 1) {
            WebAlertFragment webAlertFragment = new WebAlertFragment();
            Bundle args = new Bundle();
            args.putSerializable("model", childrenModel);
            webAlertFragment.setArguments(args);
            return webAlertFragment;
        } else {
            MessageAlertFragment messageAlertFragment = new MessageAlertFragment();
            Bundle args = new Bundle();
            args.putSerializable("model", childrenModel);
            messageAlertFragment.setArguments(args);
            return messageAlertFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
