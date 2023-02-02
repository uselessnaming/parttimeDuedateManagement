package com.example.part_timedatemanagement.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.parttimeduedatemanagement.Database.CheckItemList

@Dao
interface ItemDao {
    @Query("SELECT * FROM itemTable")
    fun getNew() : List<Item>

    @Query("DELETE FROM itemTable")
    fun deleteAll()

    @Query("DELETE FROM itemTable WHERE id = (:id)")
    fun deleteItem(id : Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item : Item)

    @Update()
    fun update(item : Item)

    @Delete()
    fun delete(item : Item)
}