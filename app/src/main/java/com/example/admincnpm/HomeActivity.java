package com.example.admincnpm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admincnpm.databinding.LayoutHeaderHomeBinding;
import com.example.admincnpm.fragment.DonHangFragment;
import com.example.admincnpm.fragment.QuanLyFragment;
import com.example.admincnpm.fragment.TaiKhoanFragment;
import com.example.admincnpm.fragment.ThongKeFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final int FRAGMENT_QL = 0;
    private static final int FRAGMENT_DH = 1;
    private static final int FRAGMENT_TK = 2;
    private static final int FRAGMENT_AD = 3;
    private int current = FRAGMENT_QL;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AppBarConfiguration appBarConfiguration;
    private   ImageView img;
    private TextView txt_name;
    private TextView txt_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new QuanLyFragment());
        Intent t = getIntent();
        String str = t.getStringExtra("key_email");
        innitView();
//        setData();
//        setView(str);
    }

    private void setView(String str) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("admin");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getValue(UserAdmin.class).getEmail().equals(str)){
                        UserAdmin user = child.getValue(UserAdmin.class);
                        txt_name.setText(user.getFullname()+"");
                        txt_email.setText(user.getEmail()+"");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void innitView() {
        img = findViewById(R.id.header_img);
        txt_name = findViewById(R.id.header_name);
        txt_email = findViewById(R.id.header_name);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_quanly){
            if(current != FRAGMENT_QL){
                replaceFragment(new QuanLyFragment());
                current = FRAGMENT_QL;
            }
        } else if(id == R.id.menu_donhang)
        {
            if(current != FRAGMENT_DH){
                replaceFragment(new DonHangFragment());
                current = FRAGMENT_DH;
            }
        }
        else if(id == R.id.menu_thongke)
        {
            if(current != FRAGMENT_TK){
                replaceFragment(new ThongKeFragment());
                current = FRAGMENT_TK;
            }
        }
        else if(id == R.id.menu_caidat)
        {
            if(current != FRAGMENT_AD){
                replaceFragment(new TaiKhoanFragment());
                current = FRAGMENT_AD;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout,fragment);
        transaction.commit();
    }
}