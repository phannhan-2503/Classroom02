package com.example.quanlybantingiaovien.adapter;

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
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.quanlybantingiaovien.R;
import com.example.quanlybantingiaovien.thongtinbaidang.thongtinbaigiangModel;
import com.example.quanlybantingiaovien.thongtinbaidang.taptinModel;

import java.util.List;


public class dsbaigiangadapter extends RecyclerView.Adapter<dsbaigiangadapter.YourViewHolder> {

    private List<thongtinbaigiangModel> dataList;
    private Context context;
    private TextView txtXoa_ChinhSua;

    public dsbaigiangadapter(Context context, List<thongtinbaigiangModel> dataList) {
        this.context = context;
        this.dataList = dataList;
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
        holder.tenGiangVien.setText(data.getTenGiangVien());
        holder.ngayDangTin.setText(data.getNgayDangTin().toString());
        holder.noiDungTin.setText(data.getNoiDungTin());
        // Gán danh sách tập tin từ thongtinbaidangModel vào file
        List<taptinModel> file = data.getTaptinModel();

        // Tạo adapter với danh sách tập tin
        taptinbaigiangadapter tapTinAdapter = new taptinbaigiangadapter(context, file);

        // Thiết lập layout manager và adapter cho RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        holder.recyclerViewDanhSachTapTin.setLayoutManager(layoutManager);
        holder.recyclerViewDanhSachTapTin.setAdapter(tapTinAdapter);


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class YourViewHolder extends RecyclerView.ViewHolder {

        TextView tenGiangVien, ngayDangTin, noiDungTin,nhanxetlophoc;
        LinearLayout thongtinbaidang;
        RecyclerView recyclerViewDanhSachTapTin;

        public YourViewHolder(@NonNull View itemView) {
            super(itemView);
            tenGiangVien=itemView.findViewById(R.id.tenGiangVien);
            ngayDangTin = itemView.findViewById(R.id.ngayDangTin);
            noiDungTin = itemView.findViewById(R.id.noiDungTin);
            recyclerViewDanhSachTapTin = itemView.findViewById(R.id.recyclerview_DanhSachTapTin);
            nhanxetlophoc =itemView.findViewById(R.id.txt_nxLopHoc);
            thongtinbaidang=itemView.findViewById(R.id.thongtinbaidang);
            thongtinbaidang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Navigation.findNavController(view).navigate(R.id.chitietfragmentbaigiang);
                }
            });
            nhanxetlophoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Navigation.findNavController(view).navigate(R.id.nhanxetfragmentbaigiang);
                }
            });
            txtXoa_ChinhSua= itemView.findViewById(R.id.txtxoa_chinhsua);
            txtXoa_ChinhSua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popupMenu = new PopupMenu(itemView.getContext(), view);
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
                                                int position = getAdapterPosition();
                                                // Xóa item từ danh sách dữ liệu
                                                dataList.remove(position);
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
                                Navigation.findNavController(view).navigate(R.id.updatefragmentbaigiang);
                            }
                            return false;
                        }
                    });
                    // Hiển thị PopupMenu
                    popupMenu.show();
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
        }

    }

