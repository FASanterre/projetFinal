package ca.qc.cgodin.projet_final

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProjetViewModelFactory(private val application: Application)
    : ViewModelProvider.AndroidViewModelFactory(application){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(ProjetViewModel::class.java)){
            ProjetViewModel(application) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}