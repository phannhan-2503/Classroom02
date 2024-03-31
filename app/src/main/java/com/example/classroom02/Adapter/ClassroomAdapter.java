package com.example.classroom02.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        if(classroom == null){
            return;
        }

        Picasso.get().load(classroom.getImageUrl()).into(holder.imgClassroom);
        holder.tvName.setText(classroom.getName());
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
        public ClassroomViewHolder(@NonNull View itemView){
            super(itemView);

            imgClassroom = itemView.findViewById(R.id.img_classroom);
            tvName = itemView.findViewById(R.id.tv_tittle);
        }
    }
}
