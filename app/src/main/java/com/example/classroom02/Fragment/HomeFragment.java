package com.example.classroom02.Fragment;

import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.classroom02.Adapter.Classroom;
import com.example.classroom02.Adapter.ClassroomAdapter;
import com.example.classroom02.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private RecyclerView rcvClassroom;
    private ClassroomAdapter classroomAdapter;
    private DatabaseReference databaseReference;
    private ImageView btSelections;
    private View homeMenuView;
    private PopupWindow popupWindow;
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

        // Tìm ImageView trong layout của fragment
        btSelections = view.findViewById(R.id.bt_selections);

        // Tìm View của home_menu layout
        homeMenuView = inflater.inflate(R.layout.home_menu, null);

        // Thiết lập sự kiện nhấn cho ImageView
        btSelections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo PopupWindow
                popupWindow = new PopupWindow(homeMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

                Point screenSize = new Point();
                getActivity().getWindowManager().getDefaultDisplay().getSize(screenSize);

                // Hiển thị PopupWindow ở vị trí cuối màn hình
                popupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        // Tìm TextView trong layout của fragment có id là join_class
        TextView joinClassTextView = homeMenuView.findViewById(R.id.join_class);

        // Thiết lập sự kiện nhấn cho TextView join_class
        joinClassTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                // Khi TextView join_class được nhấn, mở fragment mới ở đây
                openNewFragment();
            }
        });

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

    // Phương thức để mở fragment mới
    private void openNewFragment() {
        // Tạo instance của fragment mới cần mở
        Fragment newFragment = new JoinclassFragment(); // Thay YourNewFragment bằng tên fragment bạn muốn mở

        // Lấy instance của FragmentManager
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        // Bắt đầu transaction để thay thế fragment hiện tại bằng fragment mới
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, newFragment); // Thay R.id.fragment_container bằng id của layout chứa fragment trong activity của bạn
        fragmentTransaction.addToBackStack(null); // (Optional) Cho phép nhấn nút back để quay lại fragment trước đó
        fragmentTransaction.commit();
    }
}
