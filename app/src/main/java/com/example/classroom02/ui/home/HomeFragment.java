package com.example.classroom02.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.classroom02.Adapter.ClassroomAdapter;
import com.example.classroom02.R;
import com.example.classroom02.Adapter.dsbaigiangadapter;
import com.example.classroom02.databinding.FragmentHomeBantinBinding;
import com.example.classroom02.model.taptinModel;
import com.example.classroom02.model.thongtinbaigiangModel;
import com.example.classroom02.Fragment.chitietbaigiangFragment;
import com.example.classroom02.Fragment.updatebaigiangFragment;
import com.example.classroom02.Fragment.nhanxetFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private FragmentHomeBantinBinding binding;
    private RecyclerView recyclerView;
    private dsbaigiangadapter adapter;
    private List<thongtinbaigiangModel> dataList;
    private View mView;
    private Intent intent;
    private boolean check=false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBantinBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mView=root;
        //
        TextView textView = binding.clickdangbai;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_addfragment);

            }
        });

        // Khởi tạo RecyclerView và adapter
        recyclerView = binding.recyclerviewDanhSachBaiDang;
        dataList = new ArrayList<>();
        adapter = new dsbaigiangadapter(getContext(), dataList, new dsbaigiangadapter.IClickItemListener() {
            @Override
            public void OnCLickItemBaiGiang(thongtinbaigiangModel ttbg) {
                chitietbaigiangFragment chitietbaigiangFragment= new chitietbaigiangFragment();
                Bundle bundle=new Bundle();
                bundle.putParcelable("key_chitietbaigiang",  ttbg);
                chitietbaigiangFragment.setArguments(bundle);
                Navigation.findNavController(mView).navigate(R.id.chitietfragmentbaigiang, bundle);

            }
            @Override
            public void OnCLickItemChinhSua(thongtinbaigiangModel ttbg) {
                updatebaigiangFragment updatebaigiangfragment= new updatebaigiangFragment();
                Bundle bundle=new Bundle();
                bundle.putParcelable("key_updatebaigiang",  ttbg);
                updatebaigiangfragment.setArguments(bundle);
                Navigation.findNavController(mView).navigate(R.id.updatefragmentbaigiang, bundle);
            }

            @Override
            public void OnCLickItemNhanxet(thongtinbaigiangModel ttbg) {
                nhanxetFragment nhanxetfragment= new nhanxetFragment();
                Bundle bundle=new Bundle();
                bundle.putParcelable("key_nhanxetbaigiang",  ttbg);
                nhanxetfragment.setArguments(bundle);
                Navigation.findNavController(mView).navigate(R.id.nhanxetfragmentbaigiang, bundle);
            }


        });

        //Thiết lập layout manager và adapter cho RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //       layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        //Tạo dữ liệu mẫu

        try {
            createSampleData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return root;
    }
    private void createSampleData() {

        // Nhận Intent đã gửi
        intent = requireActivity().getIntent();
        // Lấy dữ liệu từ Intent
        String value = intent.getStringExtra("key_class");
        String key_class = value;

        FirebaseDatabase databaseclass = FirebaseDatabase.getInstance();
        DatabaseReference myRefclass = databaseclass.getReference("Class");
        myRefclass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    //Kiểm tra mã môn thuộc môn học nào

                    if (key.equals(key_class)) {
                        String name = snapshot.child("name").getValue(String.class);
                        String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                        ImageView imageView = mView.findViewById(R.id.img_classroom_bantin);
                        TextView view = mView.findViewById(R.id.tv_tittle_bantin);
                        //Sử dụng Glide để tải và hiển thị hình ảnh từ URL
                        RequestOptions requestOptions = new RequestOptions()
                                .placeholder(R.drawable.custom_bogocbanner) // Hình ảnh tạm thời nếu không tải được
                                .error(R.drawable.custom_bogocbanner) // Hình ảnh mặc định khi xảy ra lỗi
                                .diskCacheStrategy(DiskCacheStrategy.ALL); // Cache hình ảnh
                        Glide.with(getContext())
                                .load(imageUrl) // URL của hình ảnh
                                .apply(requestOptions) // Áp dụng các tùy chọn của RequestOptions
                                .into(imageView);
                        view.setText(name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Khởi tạo Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("BangTin");

        // Lắng nghe sự kiện thay đổi dữ liệu trên Firebase
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                // Xóa dữ liệu cũ trước khi cập nhật mới
                dataList.clear();
                // Duyệt qua các dữ liệu trên Firebase và thêm vào danh sách dataList
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    //Kiểm tra mã môn thuộc môn học nào

                    if (key.equals(key_class)) {
                        check = true;
                        for (DataSnapshot snapshotvalue : snapshot.getChildren()) {

                            // Đọc dữ liệu từ snapshot
                            String src = snapshotvalue.child("imageUrl").getValue(String.class);
                            String tenGiangVien = snapshotvalue.child("name").getValue(String.class);
                            String noiDungTin = snapshotvalue.child("content").getValue(String.class);
                            String ngayDangTin = snapshotvalue.child("date").getValue(String.class);
                            // Tạo danh sách các tập tin
                            List<taptinModel> files = new ArrayList<>();
                            for (DataSnapshot fileSnapshot : snapshotvalue.child("file").getChildren()) {
                                String fileSrc = fileSnapshot.getValue(String.class);
                                // Thêm các thuộc tính khác của tập tin nếu có
                                files.add(new taptinModel(fileSrc));
                            }

//                            CircleImageView profile_image=mView.findViewById(R.id.profile_image);
//                            //Sử dụng Glide để tải và hiển thị hình ảnh từ URL
//                            RequestOptions requestOptions = new RequestOptions()
//                                    .placeholder(R.drawable.custom_bogocbanner) // Hình ảnh tạm thời nếu không tải được
//                                    .error(R.drawable.custom_bogocbanner) // Hình ảnh mặc định khi xảy ra lỗi
//                                    .diskCacheStrategy(DiskCacheStrategy.ALL); // Cache hình ảnh
//                            Glide.with(getContext())
//                                    .load(src) // URL của hình ảnh
//                                    .apply(requestOptions) // Áp dụng các tùy chọn của RequestOptions
//                                    .into(profile_image);
                            // Tạo đối tượng thongtinbaigiangModel
                            thongtinbaigiangModel item = new thongtinbaigiangModel(src, tenGiangVien, ngayDangTin, noiDungTin, files);
                            item.setKey(snapshotvalue.getKey());
                            dataList.add(item);
                        }
                    }


                }
                if (check == false) {
                    // Tham chiếu đến nút "BangTin" trong Firebase
                    DatabaseReference bangTinRef = FirebaseDatabase.getInstance().getReference("BangTin");
                    bangTinRef.child(key_class).setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }


                // Cập nhật RecyclerView
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý trường hợp lỗi khi đọc dữ liệu từ Firebase
            }
        });
        // Lấy UID của người dùng hiện tại
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();


        FirebaseDatabase databaseuser = FirebaseDatabase.getInstance();
        DatabaseReference myRefuser = databaseuser.getReference("users");
        myRefuser.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    String key = snapshot.getKey();
                                                    //Kiểm tra mã môn thuộc môn học nào

                                                    if (key.equals(uid)) {
                                                        String profile = snapshot.child("profile").getValue(String.class);
                                                        CircleImageView profile_image = mView.findViewById(R.id.profile_image);
                                                        //Sử dụng Glide để tải và hiển thị hình ảnh từ URL
                                                        RequestOptions requestOptions = new RequestOptions()
                                                                .placeholder(R.drawable.custom_bogocbanner) // Hình ảnh tạm thời nếu không tải được
                                                                .error(R.drawable.custom_bogocbanner) // Hình ảnh mặc định khi xảy ra lỗi
                                                                .diskCacheStrategy(DiskCacheStrategy.ALL); // Cache hình ảnh
                                                        Glide.with(getContext())
                                                                .load(profile) // URL của hình ảnh
                                                                .apply(requestOptions) // Áp dụng các tùy chọn của RequestOptions
                                                                .into(profile_image);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
