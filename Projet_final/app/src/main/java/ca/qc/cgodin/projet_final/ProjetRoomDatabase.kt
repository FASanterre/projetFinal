package ca.qc.cgodin.projet_final

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Restaurant::class, FavorisUtilisateur::class, Utilisateur::class), version = 1, exportSchema = false)
abstract class ProjetRoomDatabase: RoomDatabase() {
    abstract fun projetDao(): ProjetDao
    companion object{
        @Volatile
        private var INSTANCE: ProjetRoomDatabase? = null
        fun getDatabase(context: Context): ProjetRoomDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null)
            {
                return tempInstance
            }
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                ProjetRoomDatabase::class.java,
                "project_database"
            ).build()
            return INSTANCE as ProjetRoomDatabase
        }
    }
}
