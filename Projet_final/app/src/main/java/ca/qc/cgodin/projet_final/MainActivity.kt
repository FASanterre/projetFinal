package ca.qc.cgodin.projet_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        if(intent.getStringExtra("username") == null){
            val intent = Intent(this, ConnexionActivity::class.java)
            startActivity(intent)
        }
    }
}