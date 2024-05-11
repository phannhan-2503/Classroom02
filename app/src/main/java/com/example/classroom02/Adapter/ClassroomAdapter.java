package com.example.classroom02.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classroom02.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ClassroomViewHolder>{
    private Context mContext;
    private List<Classroom> listClassroom;

    public ClassroomAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Classroom> list){
        this.listClassroom = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ClassroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemclassroom, parent, false);
        return new ClassroomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassroomViewHolder holder, int position) {
        Classroom classroom = listClassroom.get(position);
        holder.tvName.setText(classroom.getName());
        Picasso.get().load(classroom.getImageUrl()).into(holder.imgClassroom);
        // Thiết lập sự kiện onClick cho ImageView img_selection
        // Thiết lập sự kiện onClick cho ImageView img_selection
        holder.imgSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo PopupWindow
                View popupView = LayoutInflater.from(mContext).inflate(R.layout.cancel_menu, null);
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                // Thiết lập sự kiện onClick cho TextView trong layout cancel_menu.xml
                TextView tvCancel = popupView.findViewById(R.id.cancel_registration);
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Xử lý khi TextView được nhấn
                        popupWindow.dismiss(); // Đóng PopupWindow sau khi thực hiện xong
                        // Ở đây có thể gọi phương thức hủy đăng ký hoặc thực hiện hành động tương ứng
                    }
                });

                // Xử lý sự kiện nhấn ra ngoài để đóng PopupWindow
                popupWindow.setOutsideTouchable(true);

                // Hiển thị PopupWindow
                popupWindow.showAsDropDown(holder.imgSelection);

                // Đặt sự kiện cho RecyclerView để đóng PopupWindow khi nhấn ra ngoài
                RecyclerView recyclerView = (RecyclerView) holder.itemView.getParent();
                recyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        if(listClassroom != null){
            return listClassroom.size();
        }
        return 0;
    }

    public class ClassroomViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgClassroom;
        private TextView tvName;
        ImageView imgSelection;
        public ClassroomViewHolder(@NonNull View itemView){
            super(itemView);

            imgClassroom = itemView.findViewById(R.id.img_classroom);
            tvName = itemView.findViewById(R.id.tv_tittle);
            imgSelection = itemView.findViewById(R.id.img_selection);
        }
    }
}
