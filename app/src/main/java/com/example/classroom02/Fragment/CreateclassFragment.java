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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class CreateclassFragment extends Fragment {

    private EditText editTextName, editTextPart, editTextRoom, editTextTheme;
    private Button buttonCreate;

    private DatabaseReference databaseReference;

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
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Class");

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

        String[] imageUrls = {
                "https://i.imgur.com/0QPaGB8.jpeg",
                "https://i.imgur.com/8bJqpTz.jpeg",
                // Add more image URLs here as needed
        };
        Random random = new Random();
        int randomIndex = random.nextInt(imageUrls.length);
        String randomImageUrl = imageUrls[randomIndex];

        // Validate input
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Required.");
            return;
        }

        // Create new Class object
        Classroom newClass = new Classroom(randomImageUrl, name, part, room, theme);

        // Push new Class to Firebase Database
        databaseReference.push().setValue(newClass)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Class created successfully!", Toast.LENGTH_SHORT).show();
                        // Clear input fields after successful creation
                        editTextName.setText("");
                        editTextPart.setText("");
                        editTextRoom.setText("");
                        editTextTheme.setText("");
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to create class. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
