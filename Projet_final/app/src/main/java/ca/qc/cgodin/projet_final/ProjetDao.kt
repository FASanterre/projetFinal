package ca.qc.cgodin.projet_final

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProjetDao {
    @Query("SELECT * FROM utilisateur_table")
    fun getUser(): LiveData<List<Utilisateur>>

    @Query("SELECT * FROM utilisateur_table WHERE username = :username AND password = :password")
    fun findUser(username:String, password : String):  LiveData<Utilisateur>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(vararg utilisateur: Utilisateur)

    @Query("DELETE FROM utilisateur_table")
    fun deleteAll()

    @Query("SELECT * FROM utilisateur_table WHERE username = :username")
     fun findIfUserExist(username: String): LiveData<Utilisateur>
}