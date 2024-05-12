package com.example.classroom02.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classroom02.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ClassroomViewHolder> {
    private Context mContext;
    private List<Classroom> listClassroom;

    public ClassroomAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Classroom> list) {
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

                        // Lấy ID của lớp học
                        String classId = classroom.getId();

                        // Kiểm tra xem lớp học có ID không
                        if (classId == null || classId.isEmpty()) {
                            Toast.makeText(mContext, "Class ID is invalid.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Lấy UID của người dùng hiện tại
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser == null) {
                            Toast.makeText(mContext, "User not logged in.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String uid = currentUser.getUid();

                        // Cập nhật dữ liệu trên Firebase Realtime Database
                        cancelRegistration(classId, uid);
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
        if (listClassroom != null) {
            return listClassroom.size();
        }
        return 0;
    }

    public class ClassroomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgClassroom;
        private TextView tvName;
        private ImageView imgSelection;

        public ClassroomViewHolder(@NonNull View itemView) {
            super(itemView);

            imgClassroom = itemView.findViewById(R.id.img_classroom);
            tvName = itemView.findViewById(R.id.tv_tittle);
            imgSelection = itemView.findViewById(R.id.img_selection);
        }
    }

    private void cancelRegistration(String classId, String uid) {
        // Lấy reference đến Firebase Realtime Database
        DatabaseReference classRef = FirebaseDatabase.getInstance().getReference().child("Class").child(classId);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("joined_classrooms");

        // Xóa user khỏi danh sách thành viên của lớp học
        classRef.child("members").child(uid).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Xóa lớp học khỏi danh sách lớp học đã tham gia của user
                    userRef.child(classId).removeValue()
                            .addOnSuccessListener(aVoid1 -> {
                                // Thông báo hủy đăng ký thành công
                                Toast.makeText(mContext, "Cancelled registration successfully!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Thông báo hủy đăng ký không thành công
                                Toast.makeText(mContext, "Failed to cancel registration. Please try again.", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    // Thông báo hủy đăng ký không thành công
                    Toast.makeText(mContext, "Failed to cancel registration. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }
}
