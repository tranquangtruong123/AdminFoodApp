package com.example.admincnpm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.admincnpm.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    DatabaseReference database;
    long member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("admin");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    member = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.registerbtn.setOnClickListener(v -> {
            Toast.makeText(RegisterActivity.this, "Ok", Toast.LENGTH_SHORT).show();
            CreateAuthAdmin();
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
        });
    }

    private void CreateAuthAdmin() {
        binding.progress.setVisibility(View.VISIBLE);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("admin");
        String memail = binding.registerEmail.getText().toString();
        String mfullname = binding.registerFullname.getText().toString();
        String mposition = binding.registerPosition.getText().toString();
        String mphone = binding.registerPhone.getText().toString();
        String mpassword = binding.registerPassword.getText().toString();
        UserAdmin userAdmin = new UserAdmin(memail,mfullname,mposition,mphone,mpassword,"");
        firebaseAuth.createUserWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    binding.progress.setVisibility(View.GONE);
                    reference.child((member+1)+"").setValue(userAdmin)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(RegisterActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "No", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    binding.progress.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "Kiem Tra Thong Tin", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}