package com.example.admincnpm.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admincnpm.R;
import com.example.admincnpm.UpdateActivity;
import com.example.admincnpm.UpdateDonHang;
import com.example.admincnpm.adapter.CategoryAdapter;
import com.example.admincnpm.adapter.DonHangAdapter;
import com.example.admincnpm.databinding.FragmentDonHangBinding;

import com.example.admincnpm.model.Category;
import com.example.admincnpm.model.Food;
import com.example.admincnpm.model.Order;
import com.example.admincnpm.utils.Constant;
import com.example.admincnpm.utils.GlobalFuntion;
import com.example.admincnpm.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DonHangFragment extends Fragment {
    private FragmentDonHangBinding binding;
    private List<Order> mListOrder;
    private DonHangAdapter donHangAdapter;
    private CategoryAdapter categoryAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDonHangBinding.inflate(getLayoutInflater());
        mListOrder = new ArrayList<>();
        displayOder();
        getListOrders();
        categoryAdapter = new CategoryAdapter(getContext(),R.layout.item_selection,getListCategory());
        return binding.getRoot();
    }

    private void getListOrders() {
        String chile = Utils.getDeviceId(getActivity());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("booking/"+chile);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Order order = dataSnapshot.getValue(Order.class);
                            mListOrder.add(0,order);
                        donHangAdapter.notifyDataSetChanged();
                    }
                    displayOder();

                }



                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void displayOder() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        binding.recyclerview.addItemDecoration(itemDecoration);
        donHangAdapter = new DonHangAdapter(mListOrder, new DonHangAdapter.IClickListener() {
            @Override
            public void updateItemFood(Order order, int position) {
                gotoUpdate(order);
            }
        });
        binding.recyclerview.setAdapter(donHangAdapter);
    }

    private void Updatebooking(Order order, int position) {
        String stra = "";
        String chile = Utils.getDeviceId(getActivity());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("booking/" + chile);
        View viewdialog = getLayoutInflater().inflate(R.layout.activity_update_booking,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(viewdialog);
        Map<String, Object> map = new HashMap<>();
        AppCompatEditText edt_name = viewdialog.findViewById(R.id.update_name);
        AppCompatEditText edt_phone = viewdialog.findViewById(R.id.update_phone);
        Spinner spn_status = viewdialog.findViewById(R.id.spiner_status);
        AppCompatEditText edt_adress = viewdialog.findViewById(R.id.update_adress);
        AppCompatEditText edt_des = viewdialog.findViewById(R.id.update_des);
        AppCompatButton bnt = viewdialog.findViewById(R.id.update);
        spn_status.setAdapter(categoryAdapter);
        edt_name.setText(order.getName());
        edt_phone.setText(order.getPhone());
        edt_adress.setText(order.getAddress());
        edt_des.setText(order.getFoods());
        map.put("name",edt_name.getText().toString());
        map.put("phone",edt_phone.getText().toString());
        map.put("address",edt_adress.getText().toString());
        map.put("foods",edt_des.getText().toString());
        spn_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                map.put("statust",categoryAdapter.getItem(i).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        AlertDialog dialog = builder.create();
        bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(order.getId()+"").updateChildren(map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getContext(), "update thành công", Toast.LENGTH_SHORT).show();
                        donHangAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

            }
        });
        dialog.show();


    }
    private List<Category> getListCategory(){
        List<Category> list = new ArrayList<>();
        list.add(new Category("Đang Giao"));
        list.add(new Category("Đã Giao"));
        list.add(new Category("Đã Hủy"));
        list.add(new Category("Chờ Duyệt"));
        return list;
    }
    private void gotoUpdate(@NonNull Order order) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_FOOD_OBJECT, order);
        GlobalFuntion.startActivity(getActivity(), UpdateDonHang.class, bundle);
    }


}