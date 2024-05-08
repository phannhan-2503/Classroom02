package com.example.quanlybantingiaovien.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.quanlybantingiaovien.MainActivity;
import com.example.quanlybantingiaovien.R;
import com.example.quanlybantingiaovien.adapter.dsbaigiangadapter;
import com.example.quanlybantingiaovien.databinding.FragmentNewsBinding;
import com.example.quanlybantingiaovien.model.taptinModel;
import com.example.quanlybantingiaovien.model.thongtinbaigiangModel;
import com.example.quanlybantingiaovien.ui.fragment.chitietbaigiangFragment;
import com.example.quanlybantingiaovien.ui.fragment.updatebaigiangFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;
    private RecyclerView recyclerView;
    private dsbaigiangadapter adapter;
    private List<thongtinbaigiangModel> dataList;
    private View mView;
    private NewsFragment newsFragment;
    private MainActivity mainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
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
            public void OnCLickItemChihSua(thongtinbaigiangModel ttbg) {
                updatebaigiangFragment updatebaigiangfragment= new updatebaigiangFragment();
                Bundle bundle1=new Bundle();
                bundle1.putParcelable("key_updatebaigiang",  ttbg);
                updatebaigiangfragment.setArguments(bundle1);
                Navigation.findNavController(mView).navigate(R.id.updatefragmentbaigiang, bundle1);
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
    private void createSampleData() throws Exception   {

        Date dateformat = new Date();
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
                    if(key.equals("1")){
                        for (DataSnapshot snapshotvalue : snapshot.getChildren()) {

                            // Đọc dữ liệu từ snapshot
                            String src = snapshotvalue.child("imageUrl").getValue(String.class);
                            String tenGiangVien = snapshotvalue.child("name").getValue(String.class);
                            String noiDungTin = snapshotvalue.child("content").getValue(String.class);
                            // Đọc ngày từ Firebase (đây có thể là string hoặc timestamp, phụ thuộc vào cách bạn lưu trữ)
                            // Ví dụ nếu là timestamp
                            String ngayDangTin = snapshotvalue.child("date").getValue(String.class);
                            // Tạo danh sách các tập tin
                            List<taptinModel> files = new ArrayList<>();
                            for (DataSnapshot fileSnapshot : snapshotvalue.child("file").getChildren()) {
                                String fileSrc = fileSnapshot.getValue(String.class);
                                // Thêm các thuộc tính khác của tập tin nếu có
                                files.add(new taptinModel(fileSrc));
                            }
                            CircleImageView profile_image=mView.findViewById(R.id.profile_image);
                            // Sử dụng Glide để tải và hiển thị hình ảnh từ URL
                            RequestOptions requestOptions = new RequestOptions()
                                    .placeholder(R.drawable.custom_bogocbanner) // Hình ảnh tạm thời nếu không tải được
                                    .error(R.drawable.custom_bogocbanner) // Hình ảnh mặc định khi xảy ra lỗi
                                    .diskCacheStrategy(DiskCacheStrategy.ALL); // Cache hình ảnh
                            Glide.with(getContext())
                                    .load(src) // URL của hình ảnh
                                    .apply(requestOptions) // Áp dụng các tùy chọn của RequestOptions
                                    .into(profile_image);
                            // Tạo đối tượng thongtinbaigiangModel
                            thongtinbaigiangModel item = new thongtinbaigiangModel(src, tenGiangVien, ngayDangTin, noiDungTin, files);
                            item.setKey(snapshotvalue.getKey());
                            dataList.add(item);
                        }
                        }

                }

                // Cập nhật RecyclerView
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý trường hợp lỗi khi đọc dữ liệu từ Firebase
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