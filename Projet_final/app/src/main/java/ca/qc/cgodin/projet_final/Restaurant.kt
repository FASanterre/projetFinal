package ca.qc.cgodin.projet_final

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant_table")
data class Restaurant(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    val id:Int,
    @ColumnInfo(name = "nom")
    val nom: String,
    @ColumnInfo(name = "adresse")
    val adresse: String,
    @ColumnInfo(name = "telephone")
    val telephone: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "urlImage")
    val urlImage: String
)
