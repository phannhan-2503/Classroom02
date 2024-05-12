package com.example.classroom02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameTextView, emailTextView, phoneTextView;
    private DatabaseReference mDatabase;
    private Button editProfile, signoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Khởi tạo Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        nameTextView = findViewById(R.id.profileName);
        emailTextView = findViewById(R.id.profileEmail);
        phoneTextView = findViewById(R.id.profilePhone);
        editProfile = findViewById(R.id.editButton);
        signoutButton = findViewById(R.id.signoutButton);

        // Lấy thông tin người dùng đăng nhập
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Lấy UID của người dùng hiện tại
            String uid = user.getUid();

            // Truy vấn dữ liệu từ Firebase Database dựa trên UID
            DatabaseReference currentUserDb = mDatabase.child(uid);
            currentUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Kiểm tra xem dữ liệu có tồn tại không
                    if (dataSnapshot.exists()) {
                        // Lấy dữ liệu từ dataSnapshot và hiển thị lên các TextView
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String username = dataSnapshot.child("username").getValue(String.class);

                        nameTextView.setText(name);
                        emailTextView.setText(email);
                        phoneTextView.setText(username);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                }
            });
        }
        // Sự kiện khi nhấn nút EditProfile
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển hướng tới trang activity_edit_profile.xml
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });
        // Sự kiện khi nhấn nút Sign Out
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đăng xuất người dùng
                FirebaseAuth.getInstance().signOut();
                // Chuyển hướng về màn hình đăng nhập
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish(); // Kết thúc Activity hiện tại
            }
        });
    }
}