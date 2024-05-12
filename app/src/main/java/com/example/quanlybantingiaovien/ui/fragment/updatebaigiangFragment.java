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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybantingiaovien.MainActivity;
import com.example.quanlybantingiaovien.R;
import com.example.quanlybantingiaovien.adapter.taptinbaigiangadapter;
import com.example.quanlybantingiaovien.adapter.updatebaigiangadapter;
import com.example.quanlybantingiaovien.model.taptinModel;
import com.example.quanlybantingiaovien.model.thongtinbaigiangModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link updatebaigiangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class updatebaigiangFragment extends Fragment {

    private static final int MY_REQUEST_CODE = 2808;
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
        mainActivity = (MainActivity) getActivity();
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
                String content=noiDungTin.getText().toString();
                if(content.equals("")){
                    Toast.makeText(getContext(), "Thiếu nội dung!", Toast.LENGTH_SHORT).show();
                    noiDungTin.requestFocus();
                    return;
                }
                updateDataOnFirebase(content,ttbgModel.getKey());

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
        String date = sdf.format(currentDate);
        updateMap.put("date", date);

        // Xóa tất cả các tập tin hiện có

        // Tạo tham chiếu đến Storage và tải tập tin lên

        Map<String, Object> fileData = new HashMap<>();
        // Khởi tạo CountDownLatch với số lượng tập tin cần tải lên
        CountDownLatch latch = new CountDownLatch(selectedFiles.size());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("Bangtin");



        // Kiểm tra xem có file nào được chọn không
        if (!selectedFiles.isEmpty()) {
            for (taptinModel model : selectedFiles) {
                String fileKey = databaseReference.push().getKey();
                String fileUrl = model.getUri();
                //URL url = new URL(fileUrl);
                // Tham chiếu đến tập tin bạn muốn tải lên
                StorageReference fileRef = storageRef.child(getFileNameFromUri(fileUrl));
                // Tải tập tin lên
                UploadTask uploadTask = fileRef.putFile(Uri.parse(fileUrl));


                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Lấy đường dẫn URL của tập tin sau khi tải lên thành công


                                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        // Thêm đường dẫn URL vào cơ sở dữ liệu Firebase Realtime
                                        String downloadUrl = uri.toString();
                                        fileData.put(fileKey, downloadUrl);
                                        // Giảm giá trị của latch khi một tập tin đã được tải lên thành công
                                        latch.countDown();
                                        // Kiểm tra nếu tất cả các tập tin đã được tải lên thành công
                                        if (latch.getCount() == 0) {
                                            updateMap.put("file", fileData);
                                            // Thực hiện cập nhật dữ liệu trong Firebase
                                            databaseReference.updateChildren(updateMap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Xử lý khi cập nhật thành công
                                                            Toast.makeText(getContext(), "Cập nhật dữ liệu thành công!", Toast.LENGTH_SHORT).show();
                                                            Navigation.findNavController(mView).navigate(R.id.navigation_new);
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
                                latch.countDown();
                                fileData.put(fileKey, fileUrl);
                                if (latch.getCount() == 0) {
                                    updateMap.put("file", fileData);
                                    // Thực hiện cập nhật dữ liệu trong Firebase
                                    databaseReference.updateChildren(updateMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Xử lý khi cập nhật thành công
                                                    Toast.makeText(getContext(), "Cập nhật dữ liệu thành công!", Toast.LENGTH_SHORT).show();
                                                    Navigation.findNavController(mView).navigate(R.id.navigation_new);
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
        }
        else {
            // Nếu không có tập tin được chọn, chỉ đăng bài với nội dung và thông tin người đăng
            updateMap.put("file","");
            databaseReference.updateChildren(updateMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Nếu thành công, hiển thị thông báo
                            Toast.makeText(getContext(), "Cập nhật dữ liệu thành công!", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(mView).navigate(R.id.navigation_new);
                            // Hoặc có thể thực hiện các hành động khác sau khi đăng bài thành công
                            // Ví dụ: chuyển đến màn hình khác, làm mới giao diện, vv.
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Nếu thất bại, hiển thị thông báo lỗi
                            Toast.makeText(getContext(), "Đăng bài thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            // Hoặc có thể xử lý lỗi theo ý của bạn
                        }
                    });

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