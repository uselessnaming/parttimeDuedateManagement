package com.example.part_timedatemanagement.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.parttimeduedatemanagement.Database.CheckItemList

@Dao
interface ItemDao {
    @Query("SELECT * FROM itemTable")
    fun getAll() : List<Item>

    @Query("DELETE FROM itemTable")
    fun deleteAll()

    @Query("DELETE FROM itemTable WHERE id = (:id)")
    fun deleteItem(id : Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item : Item)

    @Query("UPDATE itemTable SET itemName = (:name), location = (:location), date = (:duedate) WHERE id = (:id)")
    fun update(id : Int, name : String, location : String, duedate : String)

    @Query("SELECT * FROM itemTable WHERE id = (:id) ")
    fun searchItem(id : Int) : Item

    @Query("SELECT location FROM itemTable")
    fun getType() : List<String>
}