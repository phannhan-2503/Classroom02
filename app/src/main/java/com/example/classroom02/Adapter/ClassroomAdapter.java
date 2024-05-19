package com.example.classroom02.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classroom02.MainActivity;
import com.example.classroom02.Quanlybantingiaovien;
import com.example.classroom02.R;
import com.example.classroom02.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null && currentUser.getUid().equals(classroom.getCreatorId())) {
                    // Nếu là người tạo ra lớp học, hiển thị "Lưu trữ"
                    // Thực hiện các hành động liên quan đến việc lưu trữ lớp học ở đây
                    tvCancel.setText("Lưu trữ");
                }
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
        holder.imgClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy ID của lớp học
                String classId = classroom.getId();
                Intent intent=new Intent(mContext, Quanlybantingiaovien.class);
                intent.putExtra("key_class", classId);
                // Lấy UID của người dùng hiện tại
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(mContext, "User not logged in.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String uid = currentUser.getUid();
                intent.putExtra("key_user", uid);
                mContext.startActivity(intent);

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
        DatabaseReference classRef = FirebaseDatabase.getInstance().getReference().child("Class").child(classId);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("joined_classrooms");

        classRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String creatorId = snapshot.child("creatorId").getValue(String.class);
                    if (creatorId != null && creatorId.equals(uid)) {
                        // Người dùng là người tạo lớp học, lưu trữ lớp học
                        classRef.child("archived").setValue(true)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(mContext, "Class archived successfully!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(mContext, "Failed to archive class. Please try again.", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        // Người dùng không phải là người tạo lớp học, cho phép hủy đăng ký
                        classRef.child("members").child(uid).removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    userRef.child(classId).removeValue()
                                            .addOnSuccessListener(aVoid1 -> {
                                                Toast.makeText(mContext, "Cancelled registration successfully!", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(mContext, "Failed to cancel registration. Please try again.", Toast.LENGTH_SHORT).show();
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(mContext, "Failed to cancel registration. Please try again.", Toast.LENGTH_SHORT).show();
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, "Failed to check class creator. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
