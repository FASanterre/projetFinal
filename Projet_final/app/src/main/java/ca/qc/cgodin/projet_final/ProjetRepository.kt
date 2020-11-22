package ca.qc.cgodin.projet_final

import androidx.lifecycle.LiveData

class ProjetRepository(private val projetDao: ProjetDao) {

    val allUser: LiveData<List<Utilisateur>> = projetDao.getUser()

    suspend fun insertUser(utilisateur : Utilisateur){
        projetDao.addUser(utilisateur)
    }

    suspend fun deleteAll(){
        projetDao.deleteAll()
    }


}