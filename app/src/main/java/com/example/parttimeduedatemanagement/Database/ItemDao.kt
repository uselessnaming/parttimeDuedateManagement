package com.example.part_timedatemanagement.Database

import androidx.room.*
import com.example.parttimeduedatemanagement.Database.Item

@Dao
interface ItemDao {
    @Query("SELECT * FROM itemTable")
    fun getAll() : List<Item>

    @Query("DELETE FROM itemTable")
    fun deleteAll()

    @Query("DELETE FROM itemTable WHERE id = (:id)")
    fun deleteItem(id : Int)

    @Query("DELETE FROM itemTable WHERE location = (:location) AND itemName = (:itemName)")
    fun deleteType(location : String, itemName : String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item : Item)

    @Query("UPDATE itemTable SET itemName = (:name), location = (:location), date = (:duedate) WHERE id = (:id)")
    fun update(id : Int, name : String, location : String, duedate : String)

    @Query("SELECT * FROM itemTable WHERE id = (:id) ")
    fun searchItem(id : Int) : Item

    @Query("SELECT location FROM itemTable")
    fun getType() : List<String>

    @Query("SELECT location FROM itemTable WHERE location = (:location)")
    fun checkType(location : String) : List<String>

    @Query("UPDATE itemTable SET ea = (:ea) WHERE id = (:id)")
    fun updateEA(id : Int, ea : Int)

    @Query("UPDATE itemTable SET isEmpty = (:isEmpty) WHERE id = (:id)")
    fun updateIsEmpty(id : Int, isEmpty : Int)

    @Query("SELECT EXISTS(SELECT 1 FROM itemTable WHERE itemName = (:itemName) AND location = (:location))")
    fun checkItem(itemName : String, location : String) : Boolean

    /* EA 초기화 */
    @Query("UPDATE itemTable SET ea = 0")
    fun resetEA()
}