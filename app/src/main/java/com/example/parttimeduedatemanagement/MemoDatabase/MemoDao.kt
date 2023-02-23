package com.example.parttimeduedatemanagement.MemoDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MemoDao {
    @Query("DELETE FROM memoData WHERE id = (:id)")
    fun deleteMemo(id : Int)

    @Query("SELECT * FROM memoData")
    fun getAll() : List<Memo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMemo(memo : Memo)

    @Query("UPDATE memoData SET title = (:title), content = (:content), date = (:date) WHERE id = (:id)")
    fun updateMemo(id : Int, title : String, content : String, date : String)
}