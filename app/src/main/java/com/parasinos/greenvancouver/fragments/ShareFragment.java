package com.parasinos.greenvancouver.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parasinos.greenvancouver.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ShareFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_share, container, false);
    }
}
