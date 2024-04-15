package com.example.quanlybantingiaovien.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybantingiaovien.MainActivity;
import com.example.quanlybantingiaovien.R;
import com.example.quanlybantingiaovien.adapter.addbaigiangadapter;
import com.example.quanlybantingiaovien.model.taptinModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addbaigiangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addbaigiangFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mView;
    private MainActivity mainActivity;

    private static final int PICK_FILES_REQUEST_CODE = 1;

    private List<taptinModel> selectedFiles = new ArrayList<>();
    private RecyclerView recyclerView;
    private addbaigiangadapter addbaidangadapter;

    public addbaigiangFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dangbaiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addbaigiangFragment newInstance(String param1, String param2) {
        addbaigiangFragment fragment = new addbaigiangFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_addbbaigiang, container, false);
        mainActivity= (MainActivity) getActivity();
        recyclerView = mView.findViewById(R.id.recycler_addfile);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        addbaidangadapter = new addbaigiangadapter(mainActivity,selectedFiles);
        recyclerView.setAdapter(addbaidangadapter);

        TextView uploadButton = mView.findViewById(R.id.btn_add_choosefile);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        ImageView imageView =mView.findViewById(R.id.btnClickBackHome_Add);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Quay lại màn hình ban đầu
                Navigation.findNavController(view).popBackStack();
            }
        });
//        Toolbar toolbar=mView.findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//
//        }


        return mView;
    }
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Chọn tập tin"), PICK_FILES_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILES_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    taptinModel tapTin = new taptinModel(uri); // Tạo đối tượng taptinModel từ Uri
                    selectedFiles.add(tapTin); // Thêm đối tượng vào danh sách
                }
            } else if (data.getData() != null) {
                Uri uri = data.getData();
                taptinModel tapTin = new taptinModel(uri); // Tạo đối tượng taptinModel từ Uri
                selectedFiles.add(tapTin); // Thêm đối tượng vào danh sách
            }
            addbaidangadapter.notifyDataSetChanged();
        }
    }
}