package ca.qc.cgodin.projet_final

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_connexion.*


class ConnexionActivity : AppCompatActivity() {

    private val projetViewModel: ProjetViewModel by lazy {
        ViewModelProvider(
            this,
            ProjetViewModelFactory(application)
        ).get(ProjetViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)
        //projetViewModel.deleteAll()
        val adapter = UtilisateurListAdapter(this)
        recyclerview.adapter = adapter
        projetViewModel.allUser.observe(this, Observer { utilisateurs ->
            utilisateurs?.let { adapter.setUtilisateur(it) }
        })

        var projetDao : ProjetDao = projetViewModel.projetDao

        btnConnexion.setOnClickListener {
            val username : String = editTextUsername.text.toString()
            val password : String = editTextPassword.text.toString()
            projetDao.findUser(username, password).observe(this, Observer<Utilisateur> { user -> // your code here
                if(user != null){
                    var intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("username",username)
                    }
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Connexion échouée", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnSenregistrer.setOnClickListener {
            var intent = Intent(this, EnregistrementActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        editTextUsername.text.clear()
        editTextPassword.text.clear()
    }

}