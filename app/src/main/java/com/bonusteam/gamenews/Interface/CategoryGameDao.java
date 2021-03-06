package com.bonusteam.gamenews.Interface;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bonusteam.gamenews.Entity.CategoryGame;

import java.util.List;

@Dao
public interface CategoryGameDao {
    @Query("SELECT * FROM category_table")
    LiveData<List<CategoryGame>> getAllCategories();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(CategoryGame categoryGame);
}
