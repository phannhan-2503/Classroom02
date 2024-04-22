package com.example.quanlybantingiaovien.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybantingiaovien.R;
import com.example.quanlybantingiaovien.adapter.taptinbaigiangadapter;
import com.example.quanlybantingiaovien.model.taptinModel;
import com.example.quanlybantingiaovien.model.thongtinbaigiangModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link chitietbaigiangFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chitietbaigiangFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mView;
    private TextView tenGiangVien, ngayDangTin, noiDungTin;
    private LinearLayout thongtinbaidang;
    private CircleImageView imageView_chitietbaigiang;
    private RecyclerView recyclerViewDanhSachTapTin;

    public chitietbaigiangFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chitietbaidangFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static chitietbaigiangFragment newInstance(String param1, String param2) {
        chitietbaigiangFragment fragment = new chitietbaigiangFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_chitietbaigiang, container, false);

        imageView_chitietbaigiang=mView.findViewById(R.id.profile_image_chitietbaigiang);
        tenGiangVien=mView.findViewById(R.id.tenGiaoVien_chitietbaigiang);
        ngayDangTin = mView.findViewById(R.id.ngayDangTin_chitietbaigiang);
        noiDungTin= mView.findViewById(R.id.noidung_chitietbaigiang);
        recyclerViewDanhSachTapTin = mView.findViewById(R.id.recycler_filectbaigiang);

        ImageView imageView =mView.findViewById(R.id.btnClickBackHome_chitietbaidang);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Quay lại màn hình ban đầu
                Navigation.findNavController(view).popBackStack();
            }
        });

        Bundle bundle=getArguments();
        thongtinbaigiangModel ttbgModel= (thongtinbaigiangModel) bundle.get("key_chitietbaigiang");

        imageView_chitietbaigiang.setImageResource(ttbgModel.getSrc());
        tenGiangVien.setText(ttbgModel.getTenGiangVien());
        ngayDangTin.setText( ttbgModel.getNgayDangTin().toString());
        noiDungTin.setText(ttbgModel.getNoiDungTin());

        List<taptinModel> files = ttbgModel.getTaptinModel();

        // Tạo adapter với danh sách tập tin
        taptinbaigiangadapter tapTinAdapter = new taptinbaigiangadapter(getContext(), files);

        // Thiết lập layout manager và adapter cho RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerViewDanhSachTapTin.setLayoutManager(layoutManager);
        recyclerViewDanhSachTapTin.setAdapter(tapTinAdapter);



        // Inflate the layout for this fragment
        return mView;
    }
}