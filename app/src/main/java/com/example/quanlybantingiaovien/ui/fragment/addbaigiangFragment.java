package com.example.quanlybantingiaovien.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybantingiaovien.MainActivity;
import com.example.quanlybantingiaovien.R;
import com.example.quanlybantingiaovien.adapter.addbaigiangadapter;
import com.example.quanlybantingiaovien.model.taptinModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addbaigiangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addbaigiangFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final int MY_REQUEST_CODE = 28082002;
    private View mView;
    private MainActivity mainActivity;



    private List<taptinModel> selectedFiles = new ArrayList<>();
    private RecyclerView recyclerView;
    private addbaigiangadapter addbaidangadapter;
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();
                        if(data==null){
                            return;
                        }
                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri uri = data.getClipData().getItemAt(i).getUri();
                                taptinModel tapTin = new taptinModel(uri.toString()); // Tạo đối tượng taptinModel từ Uri
                                selectedFiles.add(tapTin); // Thêm đối tượng vào danh sách
                            }
                        } else if (data.getData() != null) {
                            Uri uri = data.getData();
                            taptinModel tapTin = new taptinModel(uri.toString()); // Tạo đối tượng taptinModel từ Uri
                            selectedFiles.add(tapTin); // Thêm đối tượng vào danh sách
                        }
                        addbaidangadapter.notifyDataSetChanged();
                    }
                }
            });


    public addbaigiangFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static addbaigiangFragment newInstance(String param1, String param2) {
        addbaigiangFragment fragment = new addbaigiangFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_addbbaigiang, container, false);
        mainActivity= (MainActivity) getActivity();
        recyclerView = mView.findViewById(R.id.recycler_addfile);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        addbaidangadapter = new addbaigiangadapter(mainActivity,selectedFiles);
        recyclerView.setAdapter(addbaidangadapter);

        TextView uploadButton = mView.findViewById(R.id.btn_add_choosefile);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCLickRequestPermission(getContext());
            }
        });
        ImageView imageView =mView.findViewById(R.id.btnClickBackHome_Add);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Quay lại màn hình ban đầu
                Navigation.findNavController(view).popBackStack();
            }
        });


        return mView;
    }
    public void onCLickRequestPermission(Context context){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            openFileChooser();
            return;
        }
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            openFileChooser();
        }
        else{
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Chọn tập tin"));


    }


}