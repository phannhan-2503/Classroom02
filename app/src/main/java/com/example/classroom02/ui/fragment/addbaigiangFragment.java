package com.example.classroom02.ui.fragment;

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

import com.example.classroom02.MainActivity_BangTin;
import com.example.classroom02.R;
import com.example.classroom02.adapter.addbaigiangadapter;
import com.example.classroom02.model.taptinModel;
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
 * Use the {@link addbaigiangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addbaigiangFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final int MY_REQUEST_CODE = 28082002;
    private View mView;
    private MainActivity_BangTin mainActivityBangTin;
    private List<taptinModel> selectedFiles = new ArrayList<>();
    private RecyclerView recyclerView;
    private addbaigiangadapter addbaidangadapter;
    private Button btnAddBangTin;
    private EditText ed_addndthongbao;
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
        mainActivityBangTin = (MainActivity_BangTin) getActivity();
        recyclerView = mView.findViewById(R.id.recycler_addfile);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivityBangTin));
        addbaidangadapter = new addbaigiangadapter(mainActivityBangTin,selectedFiles);
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
        btnAddBangTin=mView.findViewById(R.id.btnAddBangTin);
        btnAddBangTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_addndthongbao=mView.findViewById(R.id.ed_addndthongbao);
                String content=ed_addndthongbao.getText().toString();
                if(content.equals("")){
                    Toast.makeText(getContext(), "Thiếu nội dung!", Toast.LENGTH_SHORT).show();
                    ed_addndthongbao.requestFocus();
                    return;
                }
                String name="Huy";
                Date Date=new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
                String date = sdf.format(Date);
                String imageUrl="imageUrl";
                //lấy id của class hiện tại so sánh với id bangtin đang xét
                String id_class="1";

                // Tham chiếu đến nút "BangTin" trong Firebase
                DatabaseReference bangTinRef = FirebaseDatabase.getInstance().getReference("BangTin").child(id_class);
                // Tạo một ID ngẫu nhiên cho bài đăng mới
                String newPostId = bangTinRef.push().getKey();

                // Tạo một đối tượng dữ liệu mới
                Map<String, Object> bangTinData = new HashMap<>();
                bangTinData.put("content",content );
                bangTinData.put("date", date);
                bangTinData.put("imageUrl",  imageUrl);
                bangTinData.put("name", name);
                bangTinData.put("comment", "");



                // Thêm các tập tin vào dưới dạng đối tượng con với key ngẫu nhiên
                Map<String, Object> fileData = new HashMap<>();

                // Khởi tạo CountDownLatch với số lượng tập tin cần tải lên
                CountDownLatch latch = new CountDownLatch(selectedFiles.size());

                // Kiểm tra nếu danh sách các file không rỗng
                if (!selectedFiles.isEmpty()) {
                    for (taptinModel model : selectedFiles) {
                        String fileKey = bangTinRef.push().getKey();
                        String fileUrl = model.getUri();

                        // Khởi tạo FirebaseStorage và tham chiếu đến thư mục bạn muốn lưu trữ tập tin
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference().child("Bangtin");

                        // Tham chiếu đến tập tin bạn muốn tải lên
                        StorageReference fileRef = storageRef.child(getFileNameFromUri(fileUrl));

                        // Tải tập tin lên
                        UploadTask uploadTask = fileRef.putFile(Uri.parse(fileUrl));

                        // Thêm lắng nghe để xử lý kết quả
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Tải tập tin lên thành công, lấy URL của tập tin và đặt vào map
                                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String fileUrlStorage = uri.toString();
                                        fileData.put(fileKey, fileUrlStorage);
                                        // Giảm giá trị của latch khi một tập tin đã được tải lên thành công
                                        latch.countDown();
                                        // Kiểm tra nếu tất cả các tập tin đã được tải lên thành công
                                        if (latch.getCount() == 0) {
                                            // Đặt fileData vào trong bangTinData
                                            bangTinData.put("file", fileData);
                                            // Đặt dữ liệu vào Firebase với key ngẫu nhiên
                                            bangTinRef.child(newPostId).setValue(bangTinData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Nếu thành công, hiển thị thông báo
                                                            Toast.makeText(getContext(), "Đăng bài thành công!", Toast.LENGTH_SHORT).show();
                                                            // Gọi phương thức sendNotification từ MyFirebaseMessagingService
                                                            Navigation.findNavController(view).popBackStack();

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
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Xử lý lỗi khi tải tập tin lên thất bại
                                Toast.makeText(getContext(), "Lỗi khi tải tập tin lên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                // Giảm giá trị của latch khi một tập tin gặp lỗi
                                latch.countDown();
                            }
                        });
                    }
                } else {
                    // Nếu không có tập tin được chọn, chỉ đăng bài với nội dung và thông tin người đăng
                    bangTinRef.child(newPostId).setValue(bangTinData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Nếu thành công, hiển thị thông báo
                                    Toast.makeText(getContext(), "Đăng bài thành công!", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(view).popBackStack();
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
            ActivityCompat.requestPermissions(mainActivityBangTin, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Chọn tập tin"));
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