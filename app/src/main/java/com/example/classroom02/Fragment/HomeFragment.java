package com.example.classroom02.Fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classroom02.Adapter.Classroom;
import com.example.classroom02.Adapter.ClassroomAdapter;
import com.example.classroom02.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rcvClassroom = view.findViewById(R.id.rcv_classroom);
        btSelections = view.findViewById(R.id.bt_selections);
        homeMenuView = inflater.inflate(R.layout.home_menu, null);

        setupRecyclerView();
        setupPopupMenu();
        loadClassrooms();

        return view;
    }

    private void setupRecyclerView() {
        rcvClassroom.setLayoutManager(new LinearLayoutManager(getContext()));
        classroomAdapter = new ClassroomAdapter(getContext());
        rcvClassroom.setAdapter(classroomAdapter);
    }

    private void setupPopupMenu() {
        btSelections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow = new PopupWindow(homeMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        TextView joinClassTextView = homeMenuView.findViewById(R.id.join_class);
        joinClassTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                openJoinClassFragment();
            }
        });

        TextView createClassTextView = homeMenuView.findViewById(R.id.create_class);
        createClassTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                openCreateClassFragment();
            }
        });
    }

    private void loadClassrooms() {
        displayJoinedClassrooms();
    }

    private void displayJoinedClassrooms() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        List<String> joinedClassrooms = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getKey().equals("joined_classrooms")) {
                                for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                                    String classId = classSnapshot.getKey();
                                    joinedClassrooms.add(classId);
                                }
                                break;
                            }
                        }
                        if (!joinedClassrooms.isEmpty()) {
                            displayClassrooms(joinedClassrooms);
                        } else {
                            // Nếu người dùng chưa tham gia lớp học nào, để danh sách trống
                            classroomAdapter.setData(new ArrayList<>());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                }
            });
        }
    }

    private void displayClassrooms(List<String> classIds) {
        List<Classroom> classrooms = new ArrayList<>();
        DatabaseReference classRef = FirebaseDatabase.getInstance().getReference().child("Class");

        for (String classId : classIds) {
            classRef.child(classId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String id = dataSnapshot.child("id").getValue(String.class);
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                        String creatorId = dataSnapshot.child("creatorId").getValue(String.class);
                        boolean archived = dataSnapshot.child("archived").getValue(Boolean.class);

                        if (!archived) { // Only add non-archived classes
                            Classroom classroom = new Classroom(id, imageUrl, name, "", "", "", creatorId, archived);
                            classrooms.add(classroom);
                            classroomAdapter.setData(classrooms);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                }
            });
        }
    }


    private void openJoinClassFragment() {
        Fragment newFragment = new JoinclassFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openCreateClassFragment() {
        Fragment newFragment = new CreateclassFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
