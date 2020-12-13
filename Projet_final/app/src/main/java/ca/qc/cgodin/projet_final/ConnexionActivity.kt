package ca.qc.cgodin.projet_final

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_connexion.*
import kotlinx.android.synthetic.main.activity_enregistrement.*
import org.json.JSONException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth

class ConnexionActivity : AppCompatActivity() {

    private val projetViewModel: ProjetViewModel by lazy {
        ViewModelProvider(
            this,
            ProjetViewModelFactory(application)
        ).get(ProjetViewModel::class.java)
    }
    var callbackManager = CallbackManager.Factory.create()
    val GOOGLE_SIGN_IN = 101
    lateinit var mGoogleSignInClient : GoogleSignInClient
    lateinit var intent1 : Intent;
    private var RC_SIGN_IN = 9001;
    lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)
        FirebaseApp.initializeApp(this);
        //projetViewModel.deleteAll()
        intent1 = Intent(this, MainActivity::class.java)

        recyclerview.isVisible = false;

        FacebookSdk.sdkInitialize(applicationContext);
        AppEventsLogger.activateApp(this)
        val adapter = UtilisateurListAdapter(this)
        recyclerview.adapter = adapter
        projetViewModel.allUser.observe(this, Observer { utilisateurs ->
            utilisateurs?.let { adapter.setUtilisateur(it) }
        })


        var projetDao : ProjetDao = projetViewModel.projetDao

        btnConnexion.setOnClickListener {
            val username : String = editTextUsername.text.toString()
            val password : String = editTextPassword.text.toString()
            projetDao.findUser(username, password).observe(
                this,
                Observer<Utilisateur> { user -> // your code here
                    if (user != null) {
                        var intent = Intent(this, MainActivity::class.java).apply {
                            putExtra("username", username)
                        }
                        MainActivity.utilConnecte = username
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Connexion échouée", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        btnSenregistrer.setOnClickListener {
            var intent = Intent(this, EnregistrementActivity::class.java)
            startActivity(intent)
        }

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {

                override fun onSuccess(loginResult: LoginResult?) {
                    // App code
                    val request = GraphRequest.newMeRequest(loginResult!!.accessToken) { `object`, response ->
                        Log.d("res", `object`.toString())
                        Log.d("res_obj", response.toString())
                        val id = `object`.getString("id")
                        if(id != null){
                            var count = 1;
                            projetDao.findIfUserExist(id).observe(this@ConnexionActivity, Observer<Utilisateur> { user -> // your code here
                                if(user != null && count == 1){
                                    Log.i("TAG", "OUI")
                                    var intent = Intent(this@ConnexionActivity, MainActivity::class.java).apply {
                                        putExtra("username", id)
                                    }
                                    MainActivity.utilConnecte = id
                                    startActivity(intent)
                                }
                                else if(count == 1){
                                    Log.i("TAG", "NON")
                                    projetViewModel.insert(Utilisateur(0,id,""))
                                    var intent = Intent(this@ConnexionActivity, MainActivity::class.java).apply {
                                        putExtra("username", id)
                                    }
                                    MainActivity.utilConnecte = id
                                    startActivity(intent)
                                    count++;
                                }
                            })

                        }
                        try {
                            val f_name = `object`.getString("first_name")
                            val l_name = `object`.getString("last_name")
                            val name = "$f_name $l_name"
                            val type = "facebook"
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id, first_name, last_name")
                    request.parameters = parameters
                    request.executeAsync()

                }

                override fun onCancel() {
                    // App code
                    Log.i("TAGGGGGGG", "CANCEL")
                }

                override fun onError(exception: FacebookException) {
                    // App code
                    Log.i("TAGGGGGGG", "ERROR")
                }
            })

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // [END config_signin]

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        sign_in_button.setOnClickListener {
            signIn()
        }
    }



    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onResume() {
        super.onResume()
        editTextUsername.text.clear()
        editTextPassword.text.clear()
        LoginManager.getInstance().logOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 64206){
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                } else {

                }

                // ...
            }
    }

}