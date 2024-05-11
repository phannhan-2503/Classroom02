package com.example.classroom02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, usernameEditText, passwordEditText;
    private Button signupButton, loginButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Realtime Database
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase = FirebaseDatabase.getInstance();

        nameEditText = findViewById(R.id.signup_name);
        emailEditText = findViewById(R.id.signup_email);
        usernameEditText = findViewById(R.id.signup_username);
        passwordEditText = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginButton = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các EditText
                final String name = nameEditText.getText().toString().trim();
                final String email = emailEditText.getText().toString().trim();
                final String username = usernameEditText.getText().toString().trim();
                final String password = passwordEditText.getText().toString().trim();

                // Tạo tài khoản trên Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        // Lấy UID của người dùng
                                        String userId = user.getUid();

                                        // Lưu thông tin User vào Realtime Database dưới node "users" theo UID của người dùng
                                        DatabaseReference usersRef = mDatabase.getReference("users");
                                        DatabaseReference userUIDRef = usersRef.child(userId);
                                        userUIDRef.child("name").setValue(name);
                                        userUIDRef.child("email").setValue(email);
                                        userUIDRef.child("username").setValue(username);

                                        // Hiển thị thông báo và chuyển đến màn hình chính hoặc màn hình đăng nhập
                                        Toast.makeText(SignupActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                        // Chuyển đến màn hình chính hoặc màn hình đăng nhập
                                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    }
                                } else {
                                    // Hiển thị thông báo nếu đăng ký không thành công
                                    Toast.makeText(SignupActivity.this, "Đăng ký thất bại! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        // Chuyển đến trang đăng ký khi nhấn vào nút "Đăng ký"
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }
}
