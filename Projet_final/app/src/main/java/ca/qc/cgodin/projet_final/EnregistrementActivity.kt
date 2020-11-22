package ca.qc.cgodin.projet_final

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_connexion.*
import kotlinx.android.synthetic.main.activity_enregistrement.*

class EnregistrementActivity : AppCompatActivity() {

    private val projetViewModel: ProjetViewModel by lazy {
        ViewModelProvider(
            this,
            ProjetViewModelFactory(application)
        ).get(ProjetViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enregistrement)
        var projetDao : ProjetDao = projetViewModel.projetDao

        btnRegister.setOnClickListener {
            if(editTextUsernameSenregistrer.text.toString() != "" && editTextPasswordEnregistrer.text.toString() != ""
                && editTextPasswordEnregistrer2.text.toString() != ""
                && editTextPasswordEnregistrer.text.toString() == editTextPasswordEnregistrer2.text.toString() ){
                val username : String = editTextUsernameSenregistrer.text.toString()
                var count = 1;
                projetDao.findIfUserExist(username).observe(this, Observer<Utilisateur> { user -> // your code here
                    if(user != null && count == 1){
                        Toast.makeText(this,"Le nom d'utilisateur a déjà été utilisé!", Toast.LENGTH_SHORT).show()
                        count ++;
                    }
                    else if(count == 1){
                        projetViewModel.insert(Utilisateur(0,editTextUsernameSenregistrer.text.toString(), editTextPasswordEnregistrer.text.toString()))
                        var intent = Intent(this, ConnexionActivity::class.java)
                        startActivity(intent)
                        count++;
                    }
                })
            }else{
                Toast.makeText(this,"Veuillez entrez toute les informations requises!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        editTextPasswordEnregistrer.text.clear()
        editTextPasswordEnregistrer2.text.clear()
        editTextUsernameSenregistrer.text.clear()
    }
}