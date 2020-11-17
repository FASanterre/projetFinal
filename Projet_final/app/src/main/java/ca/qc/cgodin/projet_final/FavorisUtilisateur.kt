package ca.qc.cgodin.projet_final

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "favoris_utilisateur_table", primaryKeys = ["restaurant","utilisateur"])
data class FavorisUtilisateur(
    @ColumnInfo(name="restaurant")
    val Restaurant:Int,
    @ColumnInfo(name = "utilisateur")
    val Utilisateur: Int
)