package com.example.admincnpm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admincnpm.adapter.CategoryAdapter;
import com.example.admincnpm.databinding.ActivityUpdateBinding;
import com.example.admincnpm.model.Category;
import com.example.admincnpm.model.Food;
import com.example.admincnpm.utils.Constant;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    private ActivityUpdateBinding binding;
    private CategoryAdapter categoryAdapter;
    ArrayAdapter<String> adapter;  
    private Food mFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        String[] codition = {"Còn Đơn", "Hết Đơn"};
         adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, codition);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinerStatusa.setAdapter(adapter);
        getDataIntent();
        initView();
        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateData();
            }
        });

        setContentView(binding.getRoot());
    }

    private void UpdateData() {
        Map<String, Object> map = new HashMap<>();
        String name = binding.updateName.getText().toString();
        map.put("name",name);
        map.put("banner",binding.updateBannerUrl.getText().toString());
        map.put("image",binding.updateImgUrl.getText().toString());
        map.put("price",Long.parseLong(binding.updatePrice.getText().toString()));
        map.put("sale",Long.parseLong(binding.updateSale.getText().toString()));
        map.put("description",binding.updateDes.getText().toString());
        binding.spinerStatusa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                map.put("codition",categoryAdapter.getItem(i).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("food");
        reference.child(mFood.getId()+"").updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(UpdateActivity.this, "update Thành Công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        if(mFood == null) return;
        Picasso.get().load(mFood.getImage()).into(binding.updateImgSrc);
        binding.updateImgUrl.setText(mFood.getImage());
        binding.updateBannerUrl.setText(mFood.getBanner());
        binding.updateName.setText(mFood.getName());
        binding.updatePrice.setText(mFood.getPrice()+"");
        binding.updateSale.setText(mFood.getSale()+"");

        binding.updateDes.setText(mFood.getDescription());
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mFood = (Food) bundle.get(Constant.KEY_INTENT_FOOD_OBJECT);
        }
    }
}