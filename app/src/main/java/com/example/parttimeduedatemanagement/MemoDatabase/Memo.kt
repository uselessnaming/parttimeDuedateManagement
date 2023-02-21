package com.example.parttimeduedatemanagement.MemoDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="memoData")
data class Memo (
    @ColumnInfo var title : String,
    @ColumnInfo var content : String,
    @ColumnInfo var date : String,
    @ColumnInfo
    @PrimaryKey (autoGenerate = true) val id : Int = 0
){
    constructor() : this("","","")
}