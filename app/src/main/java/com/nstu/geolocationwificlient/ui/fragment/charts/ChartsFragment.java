package com.nstu.geolocationwificlient.ui.fragment.charts;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.nstu.geolocationwificlient.databinding.FragmentChartsBinding;


public class ChartsFragment extends Fragment {

    private FragmentChartsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChartsViewModel chartsViewModel =
                new ViewModelProvider(this).get(ChartsViewModel.class);

        binding = FragmentChartsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}