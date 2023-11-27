package com.example.admincnpm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincnpm.databinding.ItemStatistBinding;
import com.example.admincnpm.model.Statist;

import java.util.List;

public class StatistAdapter extends RecyclerView.Adapter<StatistAdapter.StatistViewHodel> {
    private List<Statist> list;

    public StatistAdapter(List<Statist> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public StatistViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStatistBinding binding = ItemStatistBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new StatistViewHodel(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StatistViewHodel holder, int position) {
        Statist statist = list.get(position);
        holder.binding.statistName.setText(statist.getName());
        holder.binding.statistSl.setText(statist.getSl());

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public class StatistViewHodel extends RecyclerView.ViewHolder{
        private ItemStatistBinding binding;

        public StatistViewHodel(ItemStatistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
