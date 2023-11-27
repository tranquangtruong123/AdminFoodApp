package com.example.admincnpm.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admincnpm.adapter.StatistAdapter;
import com.example.admincnpm.model.Food;
import com.example.admincnpm.model.Order;
import com.example.admincnpm.model.Statist;
import com.example.admincnpm.utils.Constant;
import com.example.admincnpm.utils.DateTimeUtils;
import com.example.admincnpm.utils.Utils;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.example.admincnpm.R;
import com.example.admincnpm.databinding.FragmentThongKeBinding;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ThongKeFragment extends Fragment {

    private FragmentThongKeBinding binding;
    List<PieEntry> pieEntries;
    List<BarEntry> barEntries;
    private List<Statist> statistList;
    private StatistAdapter  statistAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentThongKeBinding.inflate(getLayoutInflater());
        pieEntries = new ArrayList<>();
        setUpPieChart();
        getStatist();
        getTimeDate();
        getDataFireBase();
        setStatist();

        return binding.getRoot();
    }

    private void getStatist() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("food");
        reference.addValueEventListener(new ValueEventListener() {
            int foods = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    foods++;

                }
                statistList.add(new Statist("Số Lượng Món:",foods+" Món"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setStatist() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.recyclerview.setLayoutManager(layoutManager);
        statistList = new ArrayList<>();
        statistAdapter = new StatistAdapter(statistList);
        binding.recyclerview.setAdapter(statistAdapter);

    }



    private void getTimeDate() {
        Calendar calendar = Calendar.getInstance();
        List<Date> dates = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(dates.get(0));
        String chile = Utils.getDeviceId(getActivity());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("booking/"+chile);
        reference.addValueEventListener(new ValueEventListener() {
            int booking = 0;
            int bookingtoday = 0;
            int summoney = 0;
            int sumone = 0;
            int sumtwo = 0;
            int sumthr = 0;
            int sumfor = 0;
            int sumfiv = 0;
            int sumsix = 0;
            int sumsev = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    summoney += order.getAmount();
                    booking +=1;
                    if(DateTimeUtils.convertTimeStampToDate(order.getId()).equals(dateFormat.format(dates.get(0)))){
                        sumone += order.getAmount();
                        bookingtoday += 1;
                    }
                    if(DateTimeUtils.convertTimeStampToDate(order.getId()).equals(dateFormat.format(dates.get(1)))){
                        sumtwo += order.getAmount();
                    }
                    if(DateTimeUtils.convertTimeStampToDate(order.getId()).equals(dateFormat.format(dates.get(2)))){
                        sumthr += order.getAmount();
                    }
                    if(DateTimeUtils.convertTimeStampToDate(order.getId()).equals(dateFormat.format(dates.get(3)))){
                        sumfor += order.getAmount();
                    }
                    if(DateTimeUtils.convertTimeStampToDate(order.getId()).equals(dateFormat.format(dates.get(4)))){
                        sumfiv += order.getAmount();
                    }
                    if(DateTimeUtils.convertTimeStampToDate(order.getId()).equals(dateFormat.format(dates.get(5)))){
                        sumsix += order.getAmount();
                    }
                    if(DateTimeUtils.convertTimeStampToDate(order.getId()).equals(dateFormat.format(dates.get(6)))){
                        sumsev += order.getAmount();
                    }
                }
                statistList.add(new Statist("Tổng Doanh Thu:",summoney+Constant.CURRENCY));
                statistList.add(new Statist("Doanh Thu Hôm Nay:",sumone+Constant.CURRENCY));
                statistList.add(new Statist("Đơn Hàng Đã Đặt:",+booking+Constant.ORDER));
                statistList.add(new Statist("Đơn Hàng Hôm Nay:",bookingtoday+Constant.ORDER));
                statistAdapter.notifyDataSetChanged();
                setBarChart(sumone,sumtwo,sumthr,sumfor,sumfiv,sumsix,sumsev);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void setBarChart(int sumone, int sumtwo, int sumthr, int sumfor, int sumfiv, int sumsix, int sumsev) {
        Calendar calendar = Calendar.getInstance();
        List<Date> dates = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(dates.get(0));
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1,sumsev));
        barEntries.add(new BarEntry(2,sumsix));
        barEntries.add(new BarEntry(3,sumfiv));
        barEntries.add(new BarEntry(4,sumfor));
        barEntries.add(new BarEntry(5,sumthr));
        barEntries.add(new BarEntry(6,sumtwo));
        barEntries.add(new BarEntry(7,sumone));
        BarDataSet barDataSet = new BarDataSet(barEntries, Constant.CURRENCY);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(15f);
        BarData barData = new BarData(barDataSet);

        binding.barchart.setFitBars(true);
        binding.barchart.setData(barData);
        binding.barchart.getDescription().setEnabled(false);
        binding.barchart.animateY(1000);
        binding.barchart.invalidate();




    }

    private void getDataFireBase() {
        String chile = Utils.getDeviceId(getActivity());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("booking/"+chile);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int total = (int) snapshot.getChildrenCount();
                int danggiao = 0;
                int dagiao = 0;
                int dahuy = 0;
                int choduet = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                        if(order.getStatust().equals("Đã Giao")) {
                            dagiao +=1;
                        }
                        if(order.getStatust().equals("Đang Giao")) {
                            danggiao +=1;
                        }
                        if(order.getStatust().equals("Đã Hủy")) {
                            dahuy +=1;
                        }
                        if(order.getStatust().equals("Chờ Duyệt")) {
                            choduet +=1;
                        }
                }
                setValuePieChart(total, choduet, danggiao, dagiao, dahuy);
                setUpPieChart();

            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setUpPieChart() {
        PieDataSet pieChart1 = new PieDataSet(pieEntries,"");
        PieData pieData = new PieData(pieChart1);
        pieChart1.setColors(new int[] {ContextCompat.getColor(getContext(), R.color.nau), ContextCompat.getColor(getContext(), R.color.redpink), ContextCompat.getColor(getContext(), R.color.xanh), ContextCompat.getColor(getContext(), R.color.pink)});
        pieChart1.setValueFormatter(new PercentFormatter(binding.pieChart));
        pieChart1.setValueTextColor(Color.WHITE);
        pieData.setValueTextSize(12f);
        binding.pieChart.setData(pieData);
        binding.pieChart.setDescription(null);
        Legend legend = binding.pieChart.getLegend();
        legend.setEnabled(false);
        binding.pieChart.invalidate();
    }
    private void setValuePieChart(int total, int choduet, int danggiao, int dagiao, int dahuy) {
        pieEntries.clear();
        pieEntries.add(new PieEntry((float) dagiao/total*100,"%"));
        pieEntries.add(new PieEntry((float) danggiao/total*100,"%"));
        pieEntries.add(new PieEntry((float) dahuy/total*100,"%"));
        pieEntries.add(new PieEntry((float) choduet/total*100,"% "));
    }
}

