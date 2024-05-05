package com.example.quanlybantingiaovien.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.quanlybantingiaovien.model.nhanxetModel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybantingiaovien.R;

import java.text.SimpleDateFormat;
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
        holder.imageView.setImageResource(Integer.parseInt(nhanxet.getSrc()));
        holder.txtTenGiangVien.setText(nhanxet.getTenGiangVien());
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
//        String strDate = sdf.format(nhanxet.getNgayDangTin());
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