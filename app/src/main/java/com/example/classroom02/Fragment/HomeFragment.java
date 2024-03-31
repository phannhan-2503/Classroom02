package com.example.classroom02.Fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.example.classroom02.Adapter.Classroom;
import com.example.classroom02.Adapter.ClassroomAdapter;
import com.example.classroom02.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private RecyclerView rcvClassroom;
    private ClassroomAdapter classroomAdapter;
    private DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Tìm RecyclerView trong layout của fragment
        rcvClassroom = view.findViewById(R.id.rcv_classroom);

        // Khởi tạo adapter và thiết lập dữ liệu
        classroomAdapter = new ClassroomAdapter(getContext());

        rcvClassroom.setAdapter(classroomAdapter);
        // Thiết lập LinearLayoutManager cho RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvClassroom.setLayoutManager(linearLayoutManager);

        // Đặt adapter cho RecyclerView
        rcvClassroom.setAdapter(classroomAdapter);

        // Kết nối với Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Class");

        // Đồng bộ sự thay đổi trong cơ sở dữ liệu
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Classroom> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("Name").getValue(String.class);
                    String imageUrl = snapshot.child("Url").getValue(String.class);

                    // Trong trường hợp bạn có thêm trường ảnh, bạn có thể lấy ảnh tương tự
                    list.add(new Classroom(imageUrl, name));
                }
                // Cập nhật RecyclerView với danh sách mới
                classroomAdapter.setData(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });

        return view;

    }

}