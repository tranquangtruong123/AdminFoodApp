package com.example.admincnpm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.example.admincnpm.databinding.ItemQlLayoutBinding;
import com.example.admincnpm.model.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class QuanLyAdapter extends RecyclerView.Adapter<QuanLyAdapter.QuanLyviewhodel> {



    private  List<Food> mListFoods;
    private final IClickListener iClickListener;

    public QuanLyAdapter(List<Food> listfood, IClickListener iClickListener) {
        this.mListFoods = listfood;
        this.iClickListener = iClickListener;
    }


    public interface IClickListener {
        void clickDeteteFood(Food food, int position);

        void updateItemFood(Food food, int position);
    }


    @NonNull
    @Override
    public QuanLyviewhodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQlLayoutBinding binding =ItemQlLayoutBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new QuanLyviewhodel(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuanLyviewhodel holder, int position) {
        Food food = mListFoods.get(position);
        String strSale = "Giảm " + food.getSale() + "%";
        String strPrice = food.getPrice() +"000 VNĐ";
        Picasso.get().load(food.getImage()).into(holder.binding.itemQlImg);
        if(food.getSale() <= 0){
            holder.binding.itemQlSale.setVisibility(View.GONE);
        }
        else {
            holder.binding.itemQlSale.setText(strSale);
        }
        holder.binding.itemQlName.setText(food.getName());
        holder.binding.itemQlPrice.setText(strPrice);
        holder.binding.itemBntEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickListener.updateItemFood(food,holder.getAdapterPosition());
            }
        });
        holder.binding.itemBntDele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickListener.clickDeteteFood(food,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == mListFoods ? 0 : mListFoods.size();
    }

    public static  class QuanLyviewhodel extends  RecyclerView.ViewHolder{
        private ItemQlLayoutBinding binding;
        public QuanLyviewhodel(ItemQlLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
