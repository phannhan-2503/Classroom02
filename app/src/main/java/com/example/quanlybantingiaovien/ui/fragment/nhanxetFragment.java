package com.example.quanlybantingiaovien.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.quanlybantingiaovien.R;
import com.example.quanlybantingiaovien.adapter.nhanxetadapter;
import com.example.quanlybantingiaovien.model.nhanxetModel;
import com.example.quanlybantingiaovien.model.taptinModel;
import com.example.quanlybantingiaovien.model.thongtinbaigiangModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link nhanxetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class nhanxetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mView;
    private nhanxetadapter adapter;
    private List<nhanxetModel> dataList;
    private EditText ednoidung;
    private ImageView imageView_send;
    private RecyclerView recyclerView;


    public nhanxetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment nhanxetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static nhanxetFragment newInstance(String param1, String param2) {
        nhanxetFragment fragment = new nhanxetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_nhanxet, container, false);

        ImageView imageView =mView.findViewById(R.id.btnClickBackHome_nhanxet);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Quay lại màn hình ban đầu
                Navigation.findNavController(view).popBackStack();
            }
        });
        unitUI();
        adapter=new nhanxetadapter(getContext());
        dataList=new ArrayList<>();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setData(dataList);
        //truyền dữ liệu giữa fragment
        Bundle bundle = getArguments();
        thongtinbaigiangModel model = bundle.getParcelable("key_nhanxetbaigiang");

        createSampleData(model.getKey());
        imageView_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content=ednoidung.getText().toString();
                if(content.equals("")){
                    ednoidung.requestFocus();
                    return;
                }

                AddNhanXet(model.getKey(),content);

            }
        });

        // Inflate the layout for this fragment
        return mView ;
    }
    private void AddNhanXet(String key_itembangtin,String content) {

        String id_user="Huy";
        String name="Huy";
        Date Date=new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
        String date = sdf.format(Date);
        String imageUrl="imageUrl";
        //lấy id của class hiện tại so sánh với id bangtin đang xét
        String id_class="1";

        // Tham chiếu đến nút "comment" trong Firebase
        DatabaseReference bangTinRef = FirebaseDatabase.getInstance().getReference("BangTin").child(id_class).child(key_itembangtin).child("comment");
        // Tạo một ID ngẫu nhiên cho bài đăng mới
        String newPostId = bangTinRef.push().getKey();

        // Tạo một đối tượng dữ liệu mới
        Map<String, Object> bangTinData = new HashMap<>();
        bangTinData.put("content",content );
        bangTinData.put("date", date);
        bangTinData.put("imageUrl",  imageUrl);
        bangTinData.put("name", name);
        bangTinData.put("id_user",id_user );
        bangTinRef.child(newPostId).setValue(bangTinData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.notifyDataSetChanged();
                        ednoidung.setText("");
                        // Nếu thành công, hiển thị thông báo
                        //Navigation.findNavController(mView).popBackStack();
                        // Hoặc có thể thực hiện các hành động khác sau khi đăng bài thành công
                        // Ví dụ: chuyển đến màn hình khác, làm mới giao diện, vv.
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Nếu thất bại, hiển thị thông báo lỗi
                        // Hoặc có thể xử lý lỗi theo ý của bạn
                    }
                });
    }
    public void unitUI(){
        ednoidung=mView.findViewById(R.id.edtndnhanxet);
        imageView_send=mView.findViewById(R.id.send_nhanxet);
        recyclerView=mView.findViewById(R.id.recycler_ndnhanxet);
    }
    private void createSampleData(String key_itembangtin)   {

        // Khởi tạo Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key_class="1";
        DatabaseReference myRef = database.getReference("BangTin").child(key_class).child(key_itembangtin).child("comment");

        // Lắng nghe sự kiện thay đổi dữ liệu trên Firebase
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Xóa dữ liệu cũ trước khi cập nhật mới
                dataList.clear();
                // Duyệt qua các dữ liệu trên Firebase và thêm vào danh sách dataList
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Đọc dữ liệu từ snapshot
                            String src = snapshot.child("imageUrl").getValue(String.class);
                            String tenGiangVien = snapshot.child("name").getValue(String.class);
                            String noiDungTin = snapshot.child("content").getValue(String.class);
                            String ngayDangTin = snapshot.child("date").getValue(String.class);
                            // Sử dụng Glide để tải và hiển thị hình ảnh từ URL
                            RequestOptions requestOptions = new RequestOptions()
                                    .placeholder(R.drawable.custom_bogocbanner) // Hình ảnh tạm thời nếu không tải được
                                    .error(R.drawable.custom_bogocbanner) // Hình ảnh mặc định khi xảy ra lỗi
                                    .diskCacheStrategy(DiskCacheStrategy.ALL); // Cache hình ảnh
                            Glide.with(getContext())
                                    .load(src) // URL của hình ảnh
                                    .apply(requestOptions) // Áp dụng các tùy chọn của RequestOptions
                                    ;
                            // Tạo đối tượng nhanxetModel
                            nhanxetModel nhanxetmodel = new nhanxetModel(src, tenGiangVien, ngayDangTin, noiDungTin);
                            //nhanxetmodel.setKey(snapshotvalue.getKey());
                            dataList.add(nhanxetmodel);
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
}