package com.example.quanlybantingiaovien.adapter;

// FileAdapter.java
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybantingiaovien.R;
import com.example.quanlybantingiaovien.thongtinbaidang.taptinModel;

import java.util.List;

public class addbaigiangadapter extends RecyclerView.Adapter<addbaigiangadapter.FileViewHolder> {

    private Context context;
    private List<taptinModel> fileList;

    public addbaigiangadapter(Context context, List<taptinModel> fileList) {
        this.context = context;
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addvaupdatebaigiang, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        taptinModel fileUri = fileList.get(position);
        holder.fileNameTextView.setText(getFileNameFromUri(fileUri.getUri()));
        // Sự kiện click vào một item trong RecyclerView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
        TextView txtclickxoa_fragmentchinhsua;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(com.example.quanlybantingiaovien.R.id.txt_item_dangbai);
            txtclickxoa_fragmentchinhsua = itemView.findViewById(R.id.txtxoa_fragmentaddvaupdate);
            txtclickxoa_fragmentchinhsua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(itemView.getContext(), view);
                    popupMenu.getMenuInflater().inflate(R.menu.xoa, popupMenu.getMenu());
                    // Xử lý sự kiện khi một mục được chọn
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.itemXoa_fragmentaddvaupdate) {
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
                                                fileList.remove(position);
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
            if (context != null) {
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
    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
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
    private void openFile(Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "*/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Cấp quyền đọc cho ứng dụng
        context.startActivity(intent);
    }
}
