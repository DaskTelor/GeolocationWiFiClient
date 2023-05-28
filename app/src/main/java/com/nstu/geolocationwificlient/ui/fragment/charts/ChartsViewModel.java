package com.nstu.geolocationwificlient.ui.fragment.charts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nstu.geolocationwificlient.App;
import com.nstu.geolocationwificlient.DataRepository;
import com.nstu.geolocationwificlient.data.WifiSignals;
import com.nstu.geolocationwificlient.listener.IDataSetsChangeListener;
import com.nstu.geolocationwificlient.listener.IConfigLineDataSetStrategy;
import com.nstu.geolocationwificlient.listener.IWifiSignalsChangeListener;
import com.nstu.geolocationwificlient.listener.IWifiTrackedChangeListener;

import java.util.ArrayList;
import java.util.Map;

public class ChartsViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private ArrayList<ILineDataSet> dataSets;
    private IDataSetsChangeListener IDataSetsChangeListener;
    private IConfigLineDataSetStrategy configLineDataSetStrategy;
    private int colorIndex = 0;

    public ChartsViewModel(@NonNull Application application) {
        super(application);

        dataRepository = DataRepository.getInstance();

    }
    private LineDataSet createLineDataset(String title, WifiSignals data){
        ArrayList<Entry> values = new ArrayList<>();
        setWifiSignals(values, data);
        LineDataSet lineDataSet = new LineDataSet(values, title);

        if(configLineDataSetStrategy != null)
            configLineDataSetStrategy.config(lineDataSet);
        data.setChangeListener(new IWifiSignalsChangeListener() {
            @Override
            public void onAddWifiSignal(int rssi, int timeStep) {
                addWifiSignal(values, rssi, timeStep);

                if (IDataSetsChangeListener == null)
                    return;

                lineDataSet.notifyDataSetChanged();
                IDataSetsChangeListener.onChangeDatasets();
            }
        });


        return lineDataSet;
    }
    private void setWifiSignals(ArrayList<Entry> values, WifiSignals data){
        for (int i = 0; i < data.getRssi().size(); i++) {
            values.add(
                    new Entry(
                            data.getTimeSteps().get(i),
                            data.getRssi().get(i))
            );
        }
    }
    private void initDataSets(){
        dataSets = new ArrayList<>();
        for(Map.Entry<String, WifiSignals> entry : dataRepository.getTrackedWifiSignalsSet().entrySet()){
            LineDataSet lineDataSet = createLineDataset(entry.getKey(), entry.getValue());

            lineDataSet.notifyDataSetChanged();
            dataSets.add(lineDataSet);
        }
        ((App)getApplication()).setWifiTrackedChangeListener(new IWifiTrackedChangeListener() {
            @Override
            public void onAddWifiTracked(Map.Entry<String, WifiSignals> entry) {
                LineDataSet lineDataSet = createLineDataset(entry.getKey(), entry.getValue());

                lineDataSet.notifyDataSetChanged();
                dataSets.add(lineDataSet);

                if (IDataSetsChangeListener != null)
                    IDataSetsChangeListener.onChangeDatasets();
            }
        });
    }
    public ArrayList<ILineDataSet> getDataSets(){
        if(dataSets == null)
            initDataSets();
        return dataSets;
    }
    public void addWifiSignal(ArrayList<Entry> values, int rssi, int timeStep){
        values.add(new Entry(timeStep, rssi));
    }
    public void setDataSetsChangeListener(IDataSetsChangeListener IDataSetsChangeListener){
        this.IDataSetsChangeListener = IDataSetsChangeListener;
    }
    public void setConfigLineDataSetStrategy(IConfigLineDataSetStrategy configLineDataSetStrategy){
        this.configLineDataSetStrategy = configLineDataSetStrategy;
    }
    public int getNextIndex(){
        return (colorIndex++ % ColorTemplate.VORDIPLOM_COLORS.length);
    }
}