package com.example.classroom02.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.classroom02.model.nhanxetModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classroom02.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class nhanxetadapter extends RecyclerView.Adapter<nhanxetadapter.YourViewHolder>{
    private Context context;
    private List<nhanxetModel> list;

    public nhanxetadapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }
    public void setData(List<nhanxetModel> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public YourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nhanxet,parent,false);
        return new YourViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull YourViewHolder holder, int position) {
        nhanxetModel nhanxet=list.get(position);
        // Sử dụng Glide để tải và hiển thị hình ảnh từ URL
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.custom_bogocbanner) // Hình ảnh tạm thời nếu không tải được
                .error(R.drawable.custom_bogocbanner) // Hình ảnh mặc định khi xảy ra lỗi
                .diskCacheStrategy(DiskCacheStrategy.ALL); // Cache hình ảnh
        Glide.with(context)
                .load(nhanxet.getSrc()) // URL của hình ảnh
                .apply(requestOptions) // Áp dụng các tùy chọn của RequestOptions
                .into(holder.imageView);
        holder.txtTenGiangVien.setText(nhanxet.getTenGiangVien());
        holder.txtNgayNhanXet.setText(nhanxet.getNgayDangTin());
        holder.txtNoiDungNhanXet.setText(nhanxet.getNoiDungTin());
    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }

    public class YourViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        private TextView txtTenGiangVien;
        private TextView txtNgayNhanXet;
        private TextView txtNoiDungNhanXet;
        public YourViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.profile_image_nhanxet);
            txtTenGiangVien=itemView.findViewById(R.id.tenGiaoVien_nhanxet);
            txtNgayNhanXet =itemView.findViewById(R.id.thoigiang_nhanxet);
            txtNoiDungNhanXet=itemView.findViewById(R.id.noidung_nhanxet);
        }
    }
}