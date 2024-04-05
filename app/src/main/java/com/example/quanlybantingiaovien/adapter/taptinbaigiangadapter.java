package com.example.quanlybantingiaovien.adapter;

// FileAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybantingiaovien.R;
import com.example.quanlybantingiaovien.thongtinbaidang.taptinModel;

import java.util.List;

public class taptinbaigiangadapter extends RecyclerView.Adapter<taptinbaigiangadapter.FileViewHolder> {

    private Context context;
    private List<taptinModel> fileList;

    public taptinbaigiangadapter(Context context, List<taptinModel> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_baigiang, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        taptinModel fileUri = fileList.get(position);
        holder.fileNameTextView.setText(fileUri.getUri().toString());
        // Sự kiện click vào một item trong RecyclerView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.txt_item_dangbai);
        }
    }



}
