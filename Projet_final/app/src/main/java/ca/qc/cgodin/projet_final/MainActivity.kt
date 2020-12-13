package ca.qc.cgodin.projet_final

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_map.*
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MainActivity : AppCompatActivity(){

    companion object {
        var langue : String = "fr"

        var utilConnecte : String = ""

        var longitude : Double? = null
        var latitude : Double? = null

        var rechercheAdapter : SearchAdapter? = null

        var listeRestaurantRecherche : MutableList<ModelRestaurant> = mutableListOf()
        var listeRestaurantRechercheDistance : MutableList<ModelRestaurant> = mutableListOf()

        var ready : Boolean = false;

        fun listeRestaurant(distance: Double, recherche: Boolean){
            ready = false;

            val payload = "test payload"

            Log.i("bonjour", distance.toString())

            if (recherche){
                listeRestaurantRechercheDistance = mutableListOf()
            }else{
                listeRestaurantRecherche = mutableListOf()
            }

            val okHttpClient = OkHttpClient()
            val requestBody = payload.toRequestBody()
            val request = Request.Builder()
                .method("POST", requestBody)
                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=${latitude},${longitude}&radius=${distance}&type=restaurant&key=AIzaSyDTvnuL5i9PpEqcJAect0D9ZRkEhNTv3Oo")
                .build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Handle this
                    Log.i("test", "fail")
                }

                override fun onResponse(call: Call, response: Response) {
                    // Handle this
                    if (response.isSuccessful) {
                        var json = JSONObject(response.body?.string())

                        val results = json.getJSONArray("results")

                        val l = results.length() - 1

                        for (i in 0..l) {
                            var numeroTelephone: String = "rien"
                            var urlSite: String = "rien"
                            var openNow: Boolean = false

                            val rjson = results.getJSONObject(i)

                            var lattitudeResto =
                                rjson.getJSONObject("geometry").getJSONObject("location").getString(
                                    "lat"
                                ).toDouble();
                            var longitudeResto =
                                rjson.getJSONObject("geometry").getJSONObject("location").getString(
                                    "lng"
                                ).toDouble();

                            val resultPhotos = rjson.optJSONArray("photos")

                            var test = "rien"

                            if (resultPhotos != null) {
                                test = resultPhotos.getJSONObject(0).optString("photo_reference")
                            }

                            var temp: Bitmap? = null

                            try {
                                if (test != "rien") {
                                    val url =
                                        URL("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${test}&key=AIzaSyDTvnuL5i9PpEqcJAect0D9ZRkEhNTv3Oo")
                                    val connection: HttpURLConnection =
                                        url.openConnection() as HttpURLConnection
                                    connection.setDoInput(true)
                                    connection.connect()
                                    val input: InputStream = connection.getInputStream()
                                    temp = BitmapFactory.decodeStream(input)
                                }

                                val payload1 = "test payload"

                                val okHttpClient1 = OkHttpClient()
                                val requestBody1 = payload1.toRequestBody()
                                val request1 = Request.Builder()
                                    .method("POST", requestBody1)
                                    .url(
                                        "https://maps.googleapis.com/maps/api/place/details/json?place_id=${
                                            results.getJSONObject(
                                                i
                                            )
                                                .getString("place_id")
                                        }&fields=website,formatted_phone_number,opening_hours&key=AIzaSyDTvnuL5i9PpEqcJAect0D9ZRkEhNTv3Oo"
                                    )
                                    .build()
                                okHttpClient1.newCall(request1).enqueue(object : Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        // Handle this
                                        Log.i("test", "fail")
                                    }

                                    override fun onResponse(call: Call, response1: Response) {
                                        // Handle this
                                        if (response.isSuccessful) {

                                            var json1 = JSONObject(response1.body?.string())

                                            try {

                                                val results1 = json1.getJSONObject("result")

                                                urlSite = results1.optString("website")
                                                numeroTelephone =
                                                    results1.optString("formatted_phone_number")

                                                var openNowObject =
                                                    results1.optJSONObject("opening_hours")

                                                if (openNowObject != null) {
                                                    openNow =
                                                        results1.getJSONObject("opening_hours")
                                                            .optBoolean("open_now")
                                                }

                                                val restaurant = ModelRestaurant(
                                                    results.getJSONObject(i).getString("place_id"),
                                                    results.getJSONObject(i).getString("name"),
                                                    results.getJSONObject(i).optString("vicinity"),
                                                    results.getJSONObject(i).optString("rating"),
                                                    results.getJSONObject(i)
                                                        .optString("user_ratings_total"),
                                                    temp,
                                                    urlSite,
                                                    numeroTelephone,
                                                    !openNow,
                                                    lattitudeResto,
                                                    longitudeResto
                                                )

                                                if (!recherche) {
                                                    listeRestaurantRecherche.add(restaurant)
                                                } else {
                                                    listeRestaurantRechercheDistance.add(restaurant)
                                                }

                                                // rechercheAdapter?.updateRestaurant()

                                                Log.i(
                                                    "distance",
                                                    "nombre resto : " + listeRestaurantRechercheDistance.count()
                                                        .toString()
                                                )
                                                Log.i(
                                                    "distance",
                                                    "nombre resto distance : " + listeRestaurantRechercheDistance.count()
                                                        .toString()
                                                )
                                            } catch (e: Exception) {

                                            }

                                            ready = true;
                                        }
                                    }
                                })
                            } catch (e: Exception) {

                            }
                        }
                    }
                }
            })
        }


    }

    var locationManager: LocationManager? = null

    var init : Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        langue = Locale.getDefault().getLanguage();

        if(intent.getStringExtra("username") == null){
            val intent = Intent(this, ConnexionActivity::class.java)
            startActivity(intent)
        }else{
            utilConnecte = intent.getStringExtra("username") as String
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        getLocationPermission()
        trouverPosition()
        Log.i("test", "long : " + longitude.toString() + ", lat : " + latitude.toString())

        if (latitude == null && longitude == null) {
            latitude = 45.5128929
            longitude = -73.7063616
        }

        if (longitude != null && latitude != null) {
            listeRestaurant(5000.toDouble(), false)
        }
    }

    fun test (){
        Log.i("test", listeRestaurantRecherche[0].name)
    }

    fun trouverPosition (){

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationGPS =
                locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (locationGPS != null) {
                latitude = locationGPS.latitude
                longitude = locationGPS.longitude
            } else {
                Toast.makeText(this, resources.getString(R.string.position), Toast.LENGTH_SHORT).show()
            }
        }

        /*
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager =
                applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers: List<String> = locationManager!!.getProviders(true)
            var bestLocation: Location? = null
            for (provider in providers) {
                val l: Location = locationManager!!.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l
                    longitude = bestLocation.longitude;
                    latitude = bestLocation.latitude;
                }
            }
        }
         */
    }


    fun getLocationPermission() {
        try {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101
                )
            }

            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    101
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}