package com.example.codesteembullyguard.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.codesteembullyguard.fragments.AccountInfoFragment;
import com.example.codesteembullyguard.fragments.ChildHomeFragment;

public class MyFragmentAdapter extends FragmentStateAdapter {
    public MyFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position==1)
        {
            return new AccountInfoFragment();
        }
        return new ChildHomeFragment();


    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
