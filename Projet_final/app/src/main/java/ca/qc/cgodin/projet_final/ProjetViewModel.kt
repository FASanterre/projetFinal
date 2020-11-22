package ca.qc.cgodin.projet_final

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjetViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProjetRepository
    val allUser: LiveData<List<Utilisateur>>
    val projetDao : ProjetDao = ProjetRoomDatabase.getDatabase(application.applicationContext).projetDao()
    init {
        val projetDao = ProjetRoomDatabase.getDatabase(application).projetDao()
        repository = ProjetRepository(projetDao)
        allUser = repository.allUser
    }

    fun insert(utilisateur: Utilisateur) = viewModelScope.launch(Dispatchers.IO){
        repository.insertUser(utilisateur)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteAll()
    }

}