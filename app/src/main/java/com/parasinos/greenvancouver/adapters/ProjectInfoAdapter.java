package com.parasinos.greenvancouver.adapters;

import com.parasinos.greenvancouver.fragments.DetailsFragment;
import com.parasinos.greenvancouver.fragments.ReviewsFragment;
import com.parasinos.greenvancouver.fragments.VolunteerFragment;

import java.security.InvalidParameterException;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ProjectInfoAdapter extends FragmentStateAdapter {

    private String mapId;
    public ProjectInfoAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String mapId) {
        super(fragmentManager, lifecycle);
        this.mapId = mapId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return DetailsFragment.newInstance(mapId);
            case 1:
                return ReviewsFragment.newInstance();
            case 2:
                return VolunteerFragment.newInstance();
            default:
                throw new InvalidParameterException("Position is not valid");
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
