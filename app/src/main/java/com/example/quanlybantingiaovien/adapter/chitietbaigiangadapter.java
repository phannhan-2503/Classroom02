package com.example.quanlybantingiaovien.adapter;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybantingiaovien.R;
import com.example.quanlybantingiaovien.model.taptinModel;

import java.util.List;

public class chitietbaigiangadapter extends RecyclerView.Adapter<chitietbaigiangadapter.FileViewHolder> {
    private Context context;
    private List<taptinModel> fileList;


    public chitietbaigiangadapter(Context context, List<taptinModel> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_baigiang, parent, false);
        return new chitietbaigiangadapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        taptinModel fileUri = fileList.get(position);
        holder.fileNameTextView.setText(getFileNameFromUri(fileUri.getUri()));
        holder.fileNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile(fileUri.getUri());
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
    @SuppressLint("Range")
    private String getFileNameFromUri(String uriString) {
        if (uriString == null) {
            return null;
        }

        Uri uri = Uri.parse(uriString);
        if (uri == null) {
            return null;
        }

        String result = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    // Mở tệp khi người dùng nhấn vào một item trong RecyclerView
    private void openFile(String file) {
        Uri fileUri=Uri.parse(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "*/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Cấp quyền đọc cho ứng dụng
        context.startActivity(intent);
    }
}
