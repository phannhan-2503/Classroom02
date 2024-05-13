package com.example.classroom02.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.classroom02.R;
import com.example.classroom02.model.thongtinbaigiangModel;
import com.example.classroom02.model.taptinModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class dsbaigiangadapter extends RecyclerView.Adapter<dsbaigiangadapter.YourViewHolder> {

    private List<thongtinbaigiangModel> dataList;
    private Context context;

    public  IClickItemListener iClickItemListener;
    public interface IClickItemListener{
        void OnCLickItemBaiGiang(thongtinbaigiangModel ttbg);
        void OnCLickItemChinhSua(thongtinbaigiangModel ttbg);
        void OnCLickItemNhanxet(thongtinbaigiangModel ttbg);
    }


    public dsbaigiangadapter(Context context, List<thongtinbaigiangModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    public dsbaigiangadapter(Context context, List<thongtinbaigiangModel> dataList,IClickItemListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.iClickItemListener=listener;
    }


    @NonNull
    @Override
    public YourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baigiang, parent, false);
        return new YourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YourViewHolder holder, int position) {
        thongtinbaigiangModel data = dataList.get(position);
        // Sử dụng Glide để tải và hiển thị hình ảnh từ URL
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.custom_bogocbanner) // Hình ảnh tạm thời nếu không tải được
                .error(R.drawable.custom_bogocbanner) // Hình ảnh mặc định khi xảy ra lỗi
                .diskCacheStrategy(DiskCacheStrategy.ALL); // Cache hình ảnh
        Glide.with(context)
                .load(data.getSrc()) // URL của hình ảnh
                .apply(requestOptions) // Áp dụng các tùy chọn của RequestOptions
                .into(holder.imageView);
        holder.tenGiangVien.setText(data.getTenGiangVien());
        holder.ngayDangTin.setText(data.getNgayDangTin());
        holder.noiDungTin.setText(data.getNoiDungTin());
        holder.txtXoa_ChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.xoa_chinhsua, popupMenu.getMenu());

                // Xử lý sự kiện khi một mục được chọn
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId()==R.id.itemXoa){
                            showConfirmationDialog(
                                    context,
                                    "Xác nhận",
                                    "Bạn có muốn tiếp tục không?",
                                    "Có",
                                    "Không",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Xử lý khi người dùng chọn "Có"
                                            int position = holder.getBindingAdapterPosition();
                                            // Xóa item từ danh sách dữ liệu
                                            dataList.remove(position);
                                            deleteItemFromFirebase(data.getKey());

                                            // Cập nhật giao diện người dùng bằng cách thông báo cho Adapter
                                            notifyItemRemoved(position);
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
                            iClickItemListener.OnCLickItemChinhSua(data);
//                                Navigation.findNavController(view).navigate(R.id.updatefragmentbaigiang);
                        }
                        return false;
                    }
                });
                // Hiển thị PopupMenu
                popupMenu.show();
            }
        });
        // Gán danh sách tập tin từ thongtinbaidangModel vào file
        List<taptinModel> file = data.getTaptinModel();

        // Tạo adapter với danh sách tập tin
        taptinbaigiangadapter tapTinAdapter = new taptinbaigiangadapter(context, file);

        // Thiết lập layout manager và adapter cho RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        holder.recyclerViewDanhSachTapTin.setLayoutManager(layoutManager);
        holder.recyclerViewDanhSachTapTin.setAdapter(tapTinAdapter);
        holder.thongtinbaidang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListener.OnCLickItemBaiGiang(data);
            }
        });
        holder.nhanxetlophoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemListener.OnCLickItemNhanxet(data);
                //Navigation.findNavController(view).navigate(R.id.nhanxetfragmentbaigiang);
            }
        });



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


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class YourViewHolder extends RecyclerView.ViewHolder {
        TextView txtXoa_ChinhSua;
        TextView tenGiangVien, ngayDangTin, noiDungTin,nhanxetlophoc,fileNameTextView;
        LinearLayout thongtinbaidang;
        CircleImageView imageView;
        RecyclerView recyclerViewDanhSachTapTin;

        public YourViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.profile_image_baigiang);
            tenGiangVien=itemView.findViewById(R.id.tenGiangVien);
            ngayDangTin = itemView.findViewById(R.id.ngayDangTin);
            noiDungTin = itemView.findViewById(R.id.noiDungTin);
            recyclerViewDanhSachTapTin = itemView.findViewById(R.id.recyclerview_DanhSachTapTin);
            nhanxetlophoc =itemView.findViewById(R.id.txt_nxLopHoc);
            thongtinbaidang=itemView.findViewById(R.id.thongtinbaidang);
            txtXoa_ChinhSua=itemView.findViewById(R.id.txtxoa_chinhsua);
            fileNameTextView = itemView.findViewById(R.id.txt_item_dangbai);


        }
    }
    // Phương thức để xóa một item từ Firebase Realtime Database
    private void deleteItemFromFirebase(String key) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("BangTin").child("1").child(key);
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Xóa thành công
                Toast.makeText(context, "Xóa thành công!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xóa thất bại
                Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

