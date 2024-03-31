package com.example.classroom02.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.classroom02.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArchivedClassesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArchivedClassesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_archived_classes, container, false);
    }
}