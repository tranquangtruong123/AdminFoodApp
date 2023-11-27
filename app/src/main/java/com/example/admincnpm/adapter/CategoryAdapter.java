package com.example.admincnpm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincnpm.R;
import com.example.admincnpm.model.Category;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selection,parent,false);
        TextView textView = convertView.findViewById(R.id.text_string);
        Category category = this.getItem(position);
        if(category != null){
            textView.setText(category.getName());
        }
        return convertView;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        TextView textView = convertView.findViewById(R.id.tv_categoty);
        Category category = this.getItem(position);
        if(category != null){
            textView.setText(category.getName());
        }
        return convertView;
    }

    public CategoryAdapter(@NonNull Context context, int resource, @NonNull List<Category> objects) {

        super(context, resource, objects);
    }
}
