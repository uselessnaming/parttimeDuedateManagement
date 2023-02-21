package com.example.parttimeduedatemanagement.MemoDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MemoDao {
    @Query("DELETE FROM memoData WHERE id = (:id)")
    fun delete(id : Int)

    @Query("SELECT * FROM memoData")
    fun getAll() : List<Memo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMemo(memo : Memo)
}