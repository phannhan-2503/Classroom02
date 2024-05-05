package com.example.quanlybantingiaovien.ui.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
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
import com.example.quanlybantingiaovien.adapter.chitietbaigiangadapter;
import com.example.quanlybantingiaovien.adapter.dsbaigiangadapter;
import com.example.quanlybantingiaovien.adapter.taptinbaigiangadapter;
import com.example.quanlybantingiaovien.adapter.updatebaigiangadapter;
import com.example.quanlybantingiaovien.model.taptinModel;
import com.example.quanlybantingiaovien.model.thongtinbaigiangModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link chitietbaigiangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chitietbaigiangFragment extends Fragment {


    private View mView;
    private TextView tenGiangVien, ngayDangTin, noiDungTin;
    private LinearLayout thongtinbaidang;
    private CircleImageView imageView_chitietbaigiang;
    private RecyclerView recyclerViewDanhSachTapTin;
    private TextView txtXoa_ChinhSua;
    private chitietbaigiangadapter chitietbaigiangadapter;

    public chitietbaigiangFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static chitietbaigiangFragment newInstance(String param1, String param2) {
        chitietbaigiangFragment fragment = new chitietbaigiangFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_chitietbaigiang, container, false);

        imageView_chitietbaigiang=mView.findViewById(R.id.profile_image_chitietbaigiang);
        tenGiangVien=mView.findViewById(R.id.tenGiaoVien_chitietbaigiang);
        ngayDangTin = mView.findViewById(R.id.ngayDangTin_chitietbaigiang);
        noiDungTin= mView.findViewById(R.id.noidung_chitietbaigiang);
        recyclerViewDanhSachTapTin = mView.findViewById(R.id.recycler_filectbaigiang);

        Bundle bundle=getArguments();
        thongtinbaigiangModel ttbgModel= (thongtinbaigiangModel) bundle.getParcelable("key_chitietbaigiang");

        // Sử dụng Glide để tải và hiển thị hình ảnh từ URL
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.custom_bogocbanner) // Hình ảnh tạm thời nếu không tải được
                .error(R.drawable.custom_bogocbanner) // Hình ảnh mặc định khi xảy ra lỗi
                .diskCacheStrategy(DiskCacheStrategy.ALL); // Cache hình ảnh
        Glide.with(getContext())
                .load(ttbgModel.getSrc()) // URL của hình ảnh
                .apply(requestOptions) // Áp dụng các tùy chọn của RequestOptions
                .into(imageView_chitietbaigiang);

        tenGiangVien.setText(ttbgModel.getTenGiangVien());
        ngayDangTin.setText(ttbgModel.getNgayDangTin() );
        noiDungTin.setText(ttbgModel.getNoiDungTin());

        List<taptinModel> files = ttbgModel.getTaptinModel();

        // Tạo adapter với danh sách tập tin
        taptinbaigiangadapter tapTinAdapter = new taptinbaigiangadapter(getContext(), files);

        // Thiết lập layout manager và adapter cho RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerViewDanhSachTapTin.setLayoutManager(layoutManager);
        recyclerViewDanhSachTapTin.setAdapter(tapTinAdapter);

        recyclerViewDanhSachTapTin.setLayoutManager(new LinearLayoutManager(getContext()));
        chitietbaigiangadapter = new chitietbaigiangadapter(getContext(),files);
        recyclerViewDanhSachTapTin.setAdapter(chitietbaigiangadapter);

        ImageView imageView =mView.findViewById(R.id.btnClickBackHome_chitietbaidang);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Quay lại màn hình ban đầu
                Navigation.findNavController(view).popBackStack();
            }
        });

        txtXoa_ChinhSua= mView.findViewById(R.id.txtxoa_chinhsua_chitietbaidang);
        txtXoa_ChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(mView.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.xoa_chinhsua, popupMenu.getMenu());

                // Xử lý sự kiện khi một mục được chọn
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.itemXoa){
                            showConfirmationDialog(
                                    getContext(),
                                    "Xác nhận",
                                    "Bạn có muốn tiếp tục không?",
                                    "Có",
                                    "Không",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            deleteItemFromFirebase(ttbgModel.getKey());
                                            Navigation.findNavController(view).navigate(R.id.navigation_home, bundle);

                                        }
                                    },
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Xử lý khi người dùng chọn "Không"
                                        }
                                    }
                            );


                        }else if(menuItem.getItemId()==R.id.itemChinhSua){
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("key_updatebaigiang", ttbgModel);
                            Navigation.findNavController(view).navigate(R.id.updatefragmentbaigiang, bundle);
                        }
                        return false;
                    }
                });
                // Hiển thị PopupMenu
                popupMenu.show();
            }
        });

        // Inflate the layout for this fragment
        return mView;
    }
    public void showConfirmationDialog(Context context, String title, String message,
                                       String positiveButtonLabel, String negativeButtonLabel,
                                       final DialogInterface.OnClickListener positiveClickListener,
                                       final DialogInterface.OnClickListener negativeClickListener) {
        if(context!=null){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(positiveButtonLabel, positiveClickListener);
            builder.setNegativeButton(negativeButtonLabel, negativeClickListener);
            builder.setCancelable(false); // Ngăn chặn việc hủy bỏ hộp thoại bằng cách nhấn nút back

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
    private void deleteItemFromFirebase(String key) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("BangTin").child("1").child(key);
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Xóa thành công
                Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xóa thất bại
                Toast.makeText(getContext(), "Xóa thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}