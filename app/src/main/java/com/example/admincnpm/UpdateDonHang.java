package com.example.admincnpm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.admincnpm.adapter.CategoryAdapter;
import com.example.admincnpm.adapter.DonHangAdapter;
import com.example.admincnpm.databinding.ActivityUpdateDonHangBinding;
import com.example.admincnpm.model.Category;
import com.example.admincnpm.model.Order;
import com.example.admincnpm.utils.Constant;
import com.example.admincnpm.utils.Utils;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateDonHang extends AppCompatActivity {

    private CategoryAdapter categoryAdapter;
    private DonHangAdapter donHangAdapter;
    private Order morder;
    private ActivityUpdateDonHangBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateDonHangBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getDataIntent();
        categoryAdapter = new CategoryAdapter(this,R.layout.item_selection,getListCategory());
        innitData();
        updateData();

    }

    private void innitData() {
        if(morder == null) return;
        binding.updateName.setText(morder.getName());
        binding.updatePhone.setText(morder.getPhone());
        binding.updateAdress.setText(morder.getAddress());
        binding.updateDes.setText(morder.getFoods());

    }

    private void updateData() {
        String stra = "";
        String chile = Utils.getDeviceId(this);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("booking/" + chile);
        Map<String, Object> map = new HashMap<>();
        String name = binding.updateName.getText().toString();
        String phone = binding.updatePhone.getText().toString();
        String adress = binding.updateAdress.getText().toString();
        String description = binding.updateDes.getText().toString();
        binding.spinerStatus.setAdapter(categoryAdapter);
        map.put("name",name);
        map.put("phone",phone);
        map.put("address",adress);
        map.put("foods",description);
        binding.spinerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                map.put("statust",categoryAdapter.getItem(i).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(morder.getId()+"").updateChildren(map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getApplication(), "update thành công", Toast.LENGTH_SHORT).show();
                        String s = (String) map.get("statust");
                        clickme(s);
                        finish();
                    }
                });
            }
        });
    }
    private void clickme(String s) {
        if(s.equals("Chờ Duyệt")){
            s = "Đang Chờ Duyệt";
        }
        else if(s.equals("Đang Giao")){
            s = "Đang Được Giao";
        }
        else if(s.equals("Đã Giao")){
            s = "Đã Được Giao";
        } else if (s.equals("Đã Hủy")) {
            s = "Đã Bị Hủy";
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logonew2);
        Notification notification = new NotificationCompat.Builder(this,MyAplycation.CHANNEL_ID)
                .setContentTitle("TQT Food Thông Báo")
                .setContentText("Đơn Hàng của bạn " + s)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setLargeIcon(bitmap)
                .setColor(getResources().getColor(R.color.purple_500))
                .build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(manager != null){
            manager.notify(getdate(),notification);
        }
    }

    private List<Category> getListCategory(){
        List<Category> list = new ArrayList<>();
        list.add(new Category("Đang Giao"));
        list.add(new Category("Đã Giao"));
        list.add(new Category("Đã Hủy"));
        list.add(new Category("Chờ Duyệt"));
        return list;
    }
    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            morder = (Order) bundle.get(Constant.KEY_INTENT_FOOD_OBJECT);
        }
    }
    private int getdate() {
        return (int) new Date().getTime();
    }


}