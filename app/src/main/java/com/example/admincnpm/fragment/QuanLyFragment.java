package com.example.admincnpm.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;


import com.example.admincnpm.AddFoodActivity;
import com.example.admincnpm.HomeActivity;
import com.example.admincnpm.MainActivity;
import com.example.admincnpm.R;
import com.example.admincnpm.UpdateActivity;
import com.example.admincnpm.adapter.CategoryAdapter;
import com.example.admincnpm.adapter.QuanLyAdapter;
import com.example.admincnpm.databinding.FragmentQuanLyBinding;
import com.example.admincnpm.model.Food;
import com.example.admincnpm.utils.Constant;
import com.example.admincnpm.utils.GlobalFuntion;
import com.example.admincnpm.utils.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class QuanLyFragment extends Fragment {
    private FragmentQuanLyBinding binding;
    private QuanLyAdapter quanLyAdapter;
    private List<Food> mListFood;
    private HomeActivity homeActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuanLyBinding.inflate(getLayoutInflater());
        homeActivity = (HomeActivity) getActivity();
        getListFoodFromFirebase("");
        initListener();
        displayListFoodSuggest();
        binding.addButtomimg.setOnClickListener(v ->{
            startActivity(new Intent(homeActivity, AddFoodActivity.class));
        });
        return binding.getRoot();

    }




    private void displayListFoodSuggest() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerview.setLayoutManager(gridLayoutManager);

        quanLyAdapter = new QuanLyAdapter(mListFood, new QuanLyAdapter.IClickListener() {
            @Override
            public void clickDeteteFood(Food food, int position) {
               deleteFoodFromCart(food,position+1);
            }

            @Override
            public void updateItemFood(Food food, int position) {
                gotoUpdate(food);
            }
        });
        binding.recyclerview.setAdapter(quanLyAdapter);

    }



    private void initListener() {
        binding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    if (mListFood != null) mListFood.clear();
                    getListFoodFromFirebase("");
                }
            }
        });

        binding.imgSearch.setOnClickListener(view -> searchFood());

        binding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFood();
                return true;
            }
            return false;
        });
    }
    private void getListFoodFromFirebase(String key) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("food");
            reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListFood = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    if (food == null) {
                        return;
                    }

                    if (StringUtil.isEmpty(key)) {
                        mListFood.add(0,food);
                    }
                    else {
                        if (GlobalFuntion.getTextSearch(food.getName()).toLowerCase().trim()
                                .contains(GlobalFuntion.getTextSearch(key).toLowerCase().trim())) {
                            mListFood.add(0, food);
                        }
                    }
                    quanLyAdapter.notifyDataSetChanged();
                }
                displayListFoodSuggest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchFood() {
        String strKey = binding.edtSearchName.getText().toString().trim();
        if (mListFood != null) mListFood.clear();
        getListFoodFromFirebase(strKey);
        GlobalFuntion.hideSoftKeyboard(getActivity());
    }
    private void deleteFoodFromCart(Food food, int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc muốn xóa sản phẩm?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("food");
                        reference.child(position+"").removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(getContext(), "xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("cancle",null)
                .show();
    }
    private void gotoUpdate(@NonNull Food food) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_FOOD_OBJECT, food);
        GlobalFuntion.startActivity(getActivity(), UpdateActivity.class, bundle);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}