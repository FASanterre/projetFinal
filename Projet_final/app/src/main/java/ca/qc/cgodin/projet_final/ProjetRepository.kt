package ca.qc.cgodin.projet_final

import androidx.lifecycle.LiveData

class ProjetRepository(private val projetDao: ProjetDao) {

    val allFavorite: LiveData<List<Restaurant>> = projetDao.getFavorite()
    val allUser: LiveData<List<Utilisateur>> = projetDao.getUser()

    suspend fun insertUser(utilisateur : Utilisateur){
        projetDao.addUser(utilisateur)
    }

    suspend fun deleteAll(){
        projetDao.deleteAll()
    }

    suspend fun insertRestaurant(restaurant: Restaurant) {
        projetDao.addResto(restaurant)
    }


}