package com.example.classroom02.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.classroom02.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinclassFragment extends Fragment {
    private EditText editTextJoin;
    private Button buttonJoin;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joinclass, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextJoin = view.findViewById(R.id.editText_join);
        buttonJoin = view.findViewById(R.id.button_join);

        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy mã lớp học từ EditText
                final String classroomCode = editTextJoin.getText().toString().trim();

                if (!classroomCode.isEmpty()) {
                    // Lấy thông tin người dùng hiện tại
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        final String userId = currentUser.getUid();

                        // Kiểm tra xem lớp học có tồn tại không
                        mDatabase.child("Class").child(classroomCode).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Nếu lớp học tồn tại, tham gia vào lớp
                                    joinClassroom(userId, classroomCode);
                                } else {
                                    // Nếu mã lớp không hợp lệ
                                    Toast.makeText(getContext(), "Mã lớp không hợp lệ", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Xử lý lỗi
                                Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    // Nếu người dùng không nhập mã lớp
                    Toast.makeText(getContext(), "Vui lòng nhập mã lớp", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    // Phương thức để tham gia vào lớp học
    private void joinClassroom(String userId, final String classroomCode) {
        // Thêm người dùng vào danh sách tham gia lớp học
        mDatabase.child("Class").child(classroomCode).child("members").child(userId).setValue(true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Thêm lớp học vào danh sách lớp đã tham gia của người dùng
                            mDatabase.child("users").child(userId).child("joined_classrooms").child(classroomCode).setValue(true)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Thành công
                                                Toast.makeText(getContext(), "Tham gia lớp học thành công", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Xử lý lỗi
                                                Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Xử lý lỗi
                            Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
