package com.example.parttimeduedatemanagement.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "itemTable")
data class Item(
    @ColumnInfo var location : String,
    @ColumnInfo var itemName : String,
    @ColumnInfo var date : String,
    @ColumnInfo
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    @ColumnInfo var ea : Int = 0,
    @ColumnInfo var isEmpty : Boolean = false,
){
    constructor() : this("","","")
}
