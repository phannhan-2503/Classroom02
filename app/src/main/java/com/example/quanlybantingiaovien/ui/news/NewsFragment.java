package com.example.quanlybantingiaovien.ui.news;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybantingiaovien.MainActivity;
import com.example.quanlybantingiaovien.R;
import com.example.quanlybantingiaovien.adapter.dsbaigiangadapter;
import com.example.quanlybantingiaovien.databinding.FragmentNewsBinding;
import com.example.quanlybantingiaovien.model.taptinModel;
import com.example.quanlybantingiaovien.model.thongtinbaigiangModel;
import com.example.quanlybantingiaovien.ui.fragment.chitietbaigiangFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;
    private RecyclerView recyclerView;
    private dsbaigiangadapter adapter;
    private List<thongtinbaigiangModel> dataList;
    private View mView;
    private NewsFragment newsFragment;
    private MainActivity mainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mView=root;
        //
        TextView textView = binding.clickdangbai;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                BottomNavigationView bottomNavigationView=mView.findViewById(R.id.nav_view);
//                bottomNavigationView.setVisibility(View.GONE);
//                Navigation.findNavController(view).navigate(R.id.addfragmentbaigiang);
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_addfragment);

            }
        });

        // Khởi tạo RecyclerView và adapter
       recyclerView = binding.recyclerviewDanhSachBaiDang;
        dataList = new ArrayList<>();
        adapter = new dsbaigiangadapter(getContext(), dataList, new dsbaigiangadapter.IClickItemListener() {
            @Override
            public void OnCLickItemBaiGiang(thongtinbaigiangModel ttbg) {
                chitietbaigiangFragment chitietbaigiangFragment= new chitietbaigiangFragment();
                Bundle bundle=new Bundle();
                bundle.putSerializable("key_chitietbaigiang", (Serializable) ttbg);
                chitietbaigiangFragment.setArguments(bundle);
//                Navigation.findNavController(mView).navigate(R.id.chitietfragmentbaigiang);
                Navigation.findNavController(mView).navigate(R.id.chitietfragmentbaigiang, bundle);
            }
        });

        //Thiết lập layout manager và adapter cho RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

         //Tạo dữ liệu mẫu
        createSampleData();
        // Xử lý sự kiện khi nhấn vào "Thông báo tin gì đó cho lớp"
//        TextView textView = binding.clickdangbai;
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.add(R.id.container, new addbaidangFragment());
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });

        return root;
    }
    private void createSampleData() {
        // Tạo một danh sách các tập tin cho mỗi bài đăng
        List<taptinModel> files1 = new ArrayList<>();
        files1.add(new taptinModel(Uri.parse("@drawable/custom_title")));
        files1.add(new taptinModel(Uri.parse("@drawable/custom_title")));
        Date date=new Date();

        // Tạo đối tượng thongtinbaidangModel và thêm vào danh sách
        dataList.add(new thongtinbaigiangModel(R.drawable.custom_bogocbanner ,"Huynh Phan Quoc Huy", date,"Nội dung 1 Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1Nội dung 1",files1));

        List<taptinModel> files2 = new ArrayList<>();
        files2.add(new taptinModel(Uri.parse("path_to_file3")));
        files2.add(new taptinModel(Uri.parse("@drawable/custom_title")));
        dataList.add(new thongtinbaigiangModel( R.drawable.custom_bogocbanner,"Huynh Phan Quoc Huy", date,"Nội dung 2",files2));

        // Thêm các bài đăng khác nếu cần
        // dataList.add(...);

        // Cập nhật dữ liệu trong adapter
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}