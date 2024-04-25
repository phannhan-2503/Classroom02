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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybantingiaovien.MainActivity;
import com.example.quanlybantingiaovien.R;
import com.example.quanlybantingiaovien.adapter.addbaigiangadapter;
import com.example.quanlybantingiaovien.adapter.taptinbaigiangadapter;
import com.example.quanlybantingiaovien.adapter.updatebaigiangadapter;
import com.example.quanlybantingiaovien.model.taptinModel;
import com.example.quanlybantingiaovien.model.thongtinbaigiangModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link updatebaigiangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class updatebaigiangFragment extends Fragment {

    private static final int MY_REQUEST_CODE = 28082002;
    private String mParam1;
    private String mParam2;
    private View mView;
    private MainActivity mainActivity;
    private List<taptinModel> selectedFiles = new ArrayList<>();
    private RecyclerView recyclerView;
    private updatebaigiangadapter updatebaidangadapter;
    private EditText noiDungTin;
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
                        updatebaidangadapter.notifyDataSetChanged();
                    }
                }
            });

    public updatebaigiangFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chinhSuaBaiDang.
     */
    // TODO: Rename and change types and number of parameters
    public static updatebaigiangFragment newInstance(String param1, String param2) {
        updatebaigiangFragment fragment = new updatebaigiangFragment();
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
        mView= inflater.inflate(R.layout.fragment_updatebaigiang, container, false);
        mainActivity= (MainActivity) getActivity();
        recyclerView = mView.findViewById(R.id.recycler_updatefile);
        noiDungTin=mView.findViewById(R.id.ed_updatendthongbao);
        //truyền dữ liệu giữa fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            thongtinbaigiangModel ttbgModel = bundle.getParcelable("key_updatebaigiang");
            if (ttbgModel != null) {
                noiDungTin.setText(ttbgModel.getNoiDungTin());
                selectedFiles = ttbgModel.getTaptinModel();
            }
        }
        taptinbaigiangadapter tapTinAdapter = new taptinbaigiangadapter(getContext(), selectedFiles);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(tapTinAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        updatebaidangadapter = new updatebaigiangadapter(mainActivity,selectedFiles);
        recyclerView.setAdapter(updatebaidangadapter);


        TextView uploadButton = mView.findViewById(R.id.btn_update_choosefile);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCLickRequestPermission(getContext());
            }
        });

        ImageView imageView =mView.findViewById(R.id.btnClickBackHome_Update);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Quay lại màn hình ban đầu
                Navigation.findNavController(view).popBackStack();
            }
        });

        // Inflate the layout for this fragment
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