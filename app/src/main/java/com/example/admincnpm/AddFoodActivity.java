package com.example.admincnpm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.admincnpm.databinding.ActivityAddFoodBinding;
import com.example.admincnpm.model.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFoodActivity extends AppCompatActivity {
    private ActivityAddFoodBinding binding;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        String[] codition = {"Còn Đơn", "Hết Đơn"};
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, codition);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinerStatusa.setAdapter(adapter);
        addProductFood();
        setContentView(binding.getRoot());
    }

    private void addProductFood() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("food");
        String id = binding.addfoodId.getText().toString();
        String image = binding.addImgUrl.getText().toString();
        String banner = binding.addBannerUrl.getText().toString();
        String name = binding.addName.getText().toString();
        String price = binding.addPrice.getText().toString();
        String Sale = binding.addfoodSale.getText().toString();
        String listImg = binding.addfoodListImg.getText().toString();
        String desciption = binding.addfoodDes.getText().toString();
        binding.spinerStatusa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            Food food;
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 food = new Food(9,name,image,banner,desciption,170,2,"Còn Đơn",true);
                    binding.update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            reference.child(9+"").setValue(food).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

    }
}