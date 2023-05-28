package com.nstu.geolocationwificlient.ui.fragment.charts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nstu.geolocationwificlient.databinding.FragmentChartsBinding;
import com.nstu.geolocationwificlient.listener.IConfigLineDataSetStrategy;
import com.nstu.geolocationwificlient.listener.IDataSetsChangeListener;


public class ChartsFragment extends Fragment {

    private FragmentChartsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        ChartsViewModel viewModel =
                new ViewModelProvider(this).get(ChartsViewModel.class);

        binding = FragmentChartsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LineChart chart = binding.chartWifiLevels;
        chart.setDrawGridBackground(true);
        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(true);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);

        // enable touch gestures
        //chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        Legend l = chart.getLegend();


        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        viewModel.setConfigLineDataSetStrategy(new IConfigLineDataSetStrategy() {
            @Override
            public void config(LineDataSet lineDataSet) {
                {
                    int color = viewModel.getNextIndex();

                    lineDataSet.setColor(ColorTemplate.VORDIPLOM_COLORS[color]);
                    lineDataSet.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[color]);

                    lineDataSet.setLineWidth(3.5f);
                    lineDataSet.setCircleRadius(6f);

                    lineDataSet.setDrawValues(false);
                }
            }
        });
        viewModel.setDataSetsChangeListener(new IDataSetsChangeListener() {
            @Override
            public void onChangeDatasets() {
                chart.setData(new LineData(viewModel.getDataSets()));
                chart.notifyDataSetChanged();
                chart.invalidate();
            }
        });

        chart.setData(new LineData(viewModel.getDataSets()));
        chart.invalidate();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}