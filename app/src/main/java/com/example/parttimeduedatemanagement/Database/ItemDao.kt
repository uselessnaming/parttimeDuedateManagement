package com.example.part_timedatemanagement.Database

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ItemDao {
    @Query("SELECT * FROM itemTable WHERE location = (:location)")
    fun searchContent(location : String) : LiveData<List<Item>>

            @Query("SELECT * FROM itemTable")
    fun getAll() : LiveData<List<Item>>

    @Query("SELECT location FROM itemTable")
    fun getLocation() : LiveData<List<String>>

    @Insert()
    fun insert(item : Item)

    @Update()
    fun update(item : Item)

    @Delete()
    fun delete(item : Item)
}