package org.nothingugly.uglydeals.jobPort.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.nothingugly.uglydeals.MainActivity;
import org.nothingugly.uglydeals.R;
import org.nothingugly.uglydeals.jobPort.activity.JobPortActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppliedFragment extends Fragment {
    private Toolbar toolbar;
    private ImageView ivBack;

    public AppliedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applied, container, false);
        return view;
    }
}