package com.example.quanlybantingiaovien.database;
import com.example.quanlybantingiaovien.model.nhanxetModel;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAONhanXet {
    @Insert
    void insertNhanXet(nhanxetModel nhanxet);
    @Query("select * from nhanxet")
    List<nhanxetModel> getNhanXet();
}
