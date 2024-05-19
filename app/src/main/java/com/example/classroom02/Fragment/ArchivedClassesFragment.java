package com.example.classroom02.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ArchivedClassesFragment extends Fragment {
    private RecyclerView rcvClassroom;
    private ClassroomAdapter classroomAdapter;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archived_classes, container, false);

        rcvClassroom = view.findViewById(R.id.archived_classroom);
        setupRecyclerView();
        loadArchivedClassrooms();

        return view;
    }

    private void setupRecyclerView() {
        rcvClassroom.setLayoutManager(new LinearLayoutManager(getContext()));
        classroomAdapter = new ClassroomAdapter(getContext());
        rcvClassroom.setAdapter(classroomAdapter);
    }

    private void loadArchivedClassrooms() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("joined_classrooms");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        List<String> archivedClassrooms = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String classId = snapshot.getKey();
                            DatabaseReference classRef = FirebaseDatabase.getInstance().getReference().child("Class").child(classId);
                            classRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot classSnapshot) {
                                    boolean archived = classSnapshot.child("archived").getValue(Boolean.class);
                                    if (archived) {
                                        archivedClassrooms.add(classId);
                                        displayArchivedClassrooms(archivedClassrooms);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Xử lý lỗi nếu có
                                }
                            });
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


    private void displayArchivedClassrooms(List<String> classIds) {
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

                        if (archived) { // Only add archived classes
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
}
