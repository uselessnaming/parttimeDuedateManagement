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

    @Query("SELECT EXISTS(SELECT 1 FROM memoData WHERE title = (:title))")
    fun isTitle(title : String) : Boolean

    @Query("SELECT id FROM memoData WHERE title = (:title)")
    fun searchId(title : String) : Int

    @Query("SELECT * FROM memoData WHERE id = (:id)")
    fun searchMemo(id : Int) : Memo?
}