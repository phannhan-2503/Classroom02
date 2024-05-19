package com.example.classroom02.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.classroom02.Adapter.Classroom;
import com.example.classroom02.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateclassFragment extends Fragment {

    private EditText editTextName, editTextPart, editTextRoom, editTextTheme;
    private Button buttonCreate;

    private DatabaseReference classDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_createclass, container, false);

        // Initialize views
        editTextName = view.findViewById(R.id.editText_name);
        editTextPart = view.findViewById(R.id.editText_part);
        editTextRoom = view.findViewById(R.id.editText_room);
        editTextTheme = view.findViewById(R.id.editText_theme);
        buttonCreate = view.findViewById(R.id.button_create);

        // Initialize Firebase
        classDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Class");
        mAuth = FirebaseAuth.getInstance();

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClass();
            }
        });

        return view;
    }

    private void createClass() {
        // Get input values
        String name = editTextName.getText().toString().trim();
        String part = editTextPart.getText().toString().trim();
        String room = editTextRoom.getText().toString().trim();
        String theme = editTextTheme.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Required.");
            return;
        }

        // Get the current user's ID
        String userId = mAuth.getCurrentUser().getUid();

        String[] imageUrls = {
                "https://i.imgur.com/0QPaGB8.jpeg",
                "https://i.imgur.com/8bJqpTz.jpeg",
                // Add more image URLs here as needed
        };
        Random random = new Random();
        int randomIndex = random.nextInt(imageUrls.length);
        String randomImageUrl = imageUrls[randomIndex];

        // Generate a unique ID for the class
        String classId = classDatabaseReference.push().getKey();

        // Create a new Classroom object
        Classroom newClass = new Classroom(classId, randomImageUrl, name, part, room, theme, userId, false);

        // Push the new class to Firebase Database
        classDatabaseReference.child(classId).setValue(newClass)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Class created successfully!", Toast.LENGTH_SHORT).show();
                        // Clear input fields after successful creation
                        editTextName.setText("");
                        editTextPart.setText("");
                        editTextRoom.setText("");
                        editTextTheme.setText("");

                        // Add the current user to the members of this class
                        addCurrentUserToMembers(classId, userId);
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to create class. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void addCurrentUserToMembers(String classId, String userId) {
        // Add the user to members of this class
        DatabaseReference classMembersRef = classDatabaseReference.child(classId).child("members").child(userId);
        classMembersRef.setValue(true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // User added to members successfully
                        // Add the class ID to the joined_classrooms of the user
                        addClassToJoinedClassrooms(classId, userId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add user to members
                    }
                });
    }

    private void addClassToJoinedClassrooms(final String classId, String userId) {
        // Get reference to user's joined_classrooms
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("joined_classrooms");
        userDatabaseReference.child(classId).setValue(true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Class ID added to joined_classrooms successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add class ID to joined_classrooms
                    }
                });
    }
}
