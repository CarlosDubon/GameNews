package com.bonusteam.gamenews.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "category_table")
public class CategoryGame {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "category_name")
    private String categoryName;

}
