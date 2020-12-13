package ca.qc.cgodin.projet_final

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant_table")
data class Restaurant(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo(name="placeId")
    val placeId:String,
    @ColumnInfo(name="name")
    val name:String,
    @ColumnInfo(name="adresse")
    val adresse:String?,
    @ColumnInfo(name="note")
    val note:String?,
    @ColumnInfo(name="totalNote")
    val totalNote:String?,
    @ColumnInfo(name="image")
    val image:String?,
    @ColumnInfo(name="url")
    val url:String?,
    @ColumnInfo(name="numeroTelephone")
    val numeroTelephone:String?,
    @ColumnInfo(name="openNow")
    val openNow:Boolean?,
    @ColumnInfo(name="latitude")
    val latitude:Double,
    @ColumnInfo(name="longitude")
    val longitude:Double,
    @ColumnInfo(name="username")
    val user:String
)
