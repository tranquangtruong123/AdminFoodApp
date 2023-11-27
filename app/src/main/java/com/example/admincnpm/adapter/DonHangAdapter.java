package com.example.admincnpm.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.admincnpm.R;
import com.example.admincnpm.databinding.ItemDonhangLayoutBinding;
import com.example.admincnpm.model.Food;
import com.example.admincnpm.model.Order;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.DonHangViewHodel> {
    private  List<Order> oderList;
    private final IClickListener iClickListener;

    public DonHangAdapter(List<Order> oderList, IClickListener iClickListener) {
        this.oderList = oderList;
        this.iClickListener = iClickListener;
    }

    public interface IClickListener {
        void updateItemFood(Order order, int position);
    }

    @NonNull
    @Override
    public DonHangViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDonhangLayoutBinding binding = ItemDonhangLayoutBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new DonHangViewHodel(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangViewHodel holder, int position) {
        Order order = oderList.get(position);

        if(order.getStatust().equals("Đang Giao")){
            holder.binding.itemTringtrang.setText("Đang Giao");
            holder.binding.itemTringtrang.setTextColor(Color.BLUE);
        }
        if(order.getStatust().equals("Đã Giao")){
            holder.binding.itemTringtrang.setText("Đã Giao");
            holder.binding.itemTringtrang.setTextColor(Color.GREEN);
        }
        if(order.getStatust().equals("Đã Hủy")){
            holder.binding.itemTringtrang.setText("Đã Hủy");
            holder.binding.itemTringtrang.setTextColor(Color.RED);
        }
        if(order.getStatust().equals("Chờ Duyệt")){
            holder.binding.itemTringtrang.setText("Chờ Duyệt");
            holder.binding.itemTringtrang.setTextColor(Color.BLACK);
        }
        holder.binding.itemMadonhang.setText(order.getId()+"");
        holder.binding.itemTendonhang.setText(order.getName());
        holder.binding.itemGia.setText(order.getFoods());
        holder.binding.bntUpdate.setOnClickListener(v -> iClickListener.updateItemFood(order,position));

    }

    @Override
    public int getItemCount() {
        if (oderList != null) {
            return oderList.size();
        }
        return 0;
    }

    public class DonHangViewHodel extends RecyclerView.ViewHolder{
        private ItemDonhangLayoutBinding binding;
        public DonHangViewHodel(ItemDonhangLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
