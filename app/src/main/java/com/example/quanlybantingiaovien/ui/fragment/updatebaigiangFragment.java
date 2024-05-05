package com.example.quanlybantingiaovien.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link updatebaigiangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class updatebaigiangFragment extends Fragment {

    private static final int MY_REQUEST_CODE = 2808;
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

            thongtinbaigiangModel ttbgModel = bundle.getParcelable("key_updatebaigiang");
            if (ttbgModel != null) {
                noiDungTin.setText(ttbgModel.getNoiDungTin());
                selectedFiles = ttbgModel.getTaptinModel();

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
        Button btnLuuNoiDungChinhSua=mView.findViewById(R.id.btnLuuNoiDungChinhSua);
        btnLuuNoiDungChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ed_updatendthongbao=mView.findViewById(R.id.ed_updatendthongbao);
                updateDataOnFirebase(ed_updatendthongbao.getText().toString(),ttbgModel.getKey());
                Navigation.findNavController(view).navigate(R.id.navigation_home, bundle);
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
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Chọn tập tin"));


    }
    private void updateDataOnFirebase(String newContent, String key) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("BangTin").child("1").child(key); // Thay "1" bằng key thực tế của mục dữ liệu
        Map<String, Object> updateMap = new HashMap<>();
        // Cập nhập nội dung mới
        updateMap.put("content", newContent);
        // Cập nhập thời gian
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
        String date = sdf.format(currentDate);
        updateMap.put("date", date);

        // Xóa tất cả các tập tin hiện có
        updateMap.put("file", null);

        // Kiểm tra xem có file nào được chọn không
        if (selectedFiles != null && selectedFiles.size() > 0) {
            Map<String, Object> fileData = new HashMap<>();

            for (taptinModel model : selectedFiles) {
                String fileKey = databaseReference.push().getKey();
                String fileUrl = model.getUri();

                // Thêm tập tin mới vào dữ liệu
                fileData.put(fileKey, fileUrl);

                // Tạo tham chiếu đến Storage và tải tập tin lên
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child("Bangtin").child(fileKey);
                storageRef.putFile(Uri.parse(fileUrl))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Lấy đường dẫn URL của tập tin sau khi tải lên thành công
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Thêm đường dẫn URL vào cơ sở dữ liệu Firebase Realtime
                                        String downloadUrl = uri.toString();
                                        fileData.put(fileKey, downloadUrl);

                                        // Kiểm tra xem đã thêm tất cả các đường dẫn URL vào cơ sở dữ liệu chưa
                                        if (fileData.size() == selectedFiles.size()) {
                                            // Thêm tập tin mới vào updateMap
                                            updateMap.put("file", fileData);

                                            // Thực hiện cập nhật dữ liệu trong Firebase
                                            databaseReference.updateChildren(updateMap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Xử lý khi cập nhật thành công
                                                            Toast.makeText(getContext(), "Dữ liệu đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Xử lý khi cập nhật thất bại
                                                            Toast.makeText(getContext(), "Cập nhật dữ liệu thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xử lý khi tải lên thất bại
                                Log.e("Firebase", "Failed to upload file: " + e.getMessage());
                            }
                        });
            }
        }
    }


    @SuppressLint("Range")
    private String getFileNameFromUri( String result) {
        Uri uri=Uri.parse(result);
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


}