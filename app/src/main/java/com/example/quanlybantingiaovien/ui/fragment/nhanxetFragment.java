package com.example.quanlybantingiaovien.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlybantingiaovien.R;
import com.example.quanlybantingiaovien.adapter.nhanxetadapter;
import com.example.quanlybantingiaovien.model.nhanxetModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link nhanxetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class nhanxetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mView;
    private nhanxetadapter nhanxetadapter;
    private List<nhanxetModel> list;
    private EditText ednoidung;
    private ImageView imageView_send;
    private RecyclerView recyclerView;


    public nhanxetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment nhanxetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static nhanxetFragment newInstance(String param1, String param2) {
        nhanxetFragment fragment = new nhanxetFragment();
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
        mView=inflater.inflate(R.layout.fragment_nhanxet, container, false);
        ImageView imageView =mView.findViewById(R.id.btnClickBackHome_nhanxet);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Quay lại màn hình ban đầu
                Navigation.findNavController(view).popBackStack();
            }
        });
        unitUI();
        nhanxetadapter=new nhanxetadapter(getContext());
        list=new ArrayList<>();
        nhanxetadapter.setData(list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(nhanxetadapter);
        nhanxetadapter.setData(list);
        imageView_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNhanXet();
            }
        });

        // Inflate the layout for this fragment
        return mView ;
    }
    private void AddNhanXet() {
        String ndnhanxet=ednoidung.getText().toString().trim();
        if(TextUtils.isEmpty(ndnhanxet)){
            return ;
        }
        Date date=new Date();
        try {
            date = parseDateTime("dd/MM/yyyy HH:mm", getCurrentDateTimeString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ednoidung.setText("");
        nhanxetModel model=new nhanxetModel(String.valueOf(R.drawable.custom_bogocbanner),"huy",date,ndnhanxet);
        list.add(model);
        nhanxetadapter.setData(list);
        nhanxetadapter.notifyDataSetChanged();


    }
    public void unitUI(){
        ednoidung=mView.findViewById(R.id.edtndnhanxet);
        imageView_send=mView.findViewById(R.id.send_nhanxet);
        recyclerView=mView.findViewById(R.id.recycler_ndnhanxet);
    }
    private Date parseDateTime(String pattern, String dateTimeString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(dateTimeString);
    }

    private String getCurrentDateTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(new Date());
    }
}