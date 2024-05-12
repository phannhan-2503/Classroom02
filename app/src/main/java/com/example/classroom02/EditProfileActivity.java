package com.example.classroom02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {
    private EditText nameEditText, phoneEditText;
    private Button saveButton;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        nameEditText = findViewById(R.id.editName);
        phoneEditText = findViewById(R.id.editPhone);
        saveButton = findViewById(R.id.saveButton);

        // Lấy thông tin người dùng hiện tại
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Lấy UID của người dùng
            final String uid = user.getUid();

            // Lấy thông tin người dùng từ Firebase Database và hiển thị lên EditText
            mDatabase.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        if (dataSnapshot.exists()) {
                            String name = dataSnapshot.child("name").getValue(String.class);
                            String email = dataSnapshot.child("email").getValue(String.class);
                            String username = dataSnapshot.child("username").getValue(String.class);

                            nameEditText.setText(name);
                            phoneEditText.setText(username);
                        }
                    }
                }
            });
        }

        if (user != null) {
            final String uid = user.getUid();

            // Sự kiện khi nhấn nút Save
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lấy dữ liệu từ EditText
                    final String name = nameEditText.getText().toString().trim();
                    final String username = phoneEditText.getText().toString().trim();

                    // Lưu thông tin người dùng vào Firebase Database
                    mDatabase.child(uid).child("name").setValue(name);
                    mDatabase.child(uid).child("username").setValue(username);

                    // Hiển thị thông báo
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                    // Đóng Activity và quay trở lại ProfileActivity
                    finish();
                }
            });
        }
    }
}