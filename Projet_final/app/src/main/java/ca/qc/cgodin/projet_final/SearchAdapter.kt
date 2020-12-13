package ca.qc.cgodin.projet_final

import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.restaurant_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.fixedRateTimer


class SearchAdapter (): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(){

    private var restaurants: List<ModelRestaurant> = emptyList()
    private lateinit var onItemClickListener: ((ModelRestaurant) -> Unit)

    var quantite : Int = 0;
    var foisZero : Int = 0;
    var derniereDistance : Int = 0;

    var init : Boolean = false;

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logoResto: ImageView = itemView.findViewById(R.id.logo)
        val tvNom: TextView = itemView.findViewById(R.id.nom)
        val tvDescription: TextView = itemView.findViewById(R.id.adresse)
        val tvNote: TextView = itemView.findViewById(R.id.note)
        override fun toString(): String {
            return super.toString() + " '" + tvNom.text + "'"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_item, parent, false)
        return SearchViewHolder(view)
    }

    private fun loop() {
        CoroutineScope(IO).launch {
            delay(1000)
            CoroutineScope(Main).launch {
                Log.i("distance", "test " + MainActivity.ready )
                if (MainActivity.ready) {
                    restaurants = MainActivity.listeRestaurantRechercheDistance
                    updateRestaurant()
                    MainActivity.ready = false;
                }

                loop()
            }
        }
    }

    override fun onBindViewHolder(holderArticle: SearchViewHolder, position: Int)
    {
        Log.i("test", "ici")
        val resto = restaurants[position]
        holderArticle.tvNom.text = resto.name
        holderArticle.tvDescription.text = resto.adresse
        holderArticle.tvNote.text = resto.note
        holderArticle.logoResto.setImageBitmap(resto.image)
        holderArticle.itemView.apply {
            nom.text = resto.name
            adresse.text = resto.adresse
            note.text = resto.note
            logo.setImageBitmap(resto.image)
        }

        holderArticle.itemView.setOnClickListener {
            onItemClickListener(resto)
        }
    }

    override fun getItemCount(): Int = restaurants.size

    fun setRestaurants(distance : Int) {

        if (!init){
            loop()
            init = true;
        }

        derniereDistance = distance

        MainActivity.listeRestaurant(distance.toDouble() * 100, true)

        Log.i("test", (distance.toDouble() * 100).toString())
    }

    fun updateRestaurant (){
        this.notifyDataSetChanged()
        Log.i("test","update " + restaurants.size.toString())
    }

    fun resetRestaurantsList (){
        restaurants = emptyList()
    }

    fun setOnItemClickListener(listener: (ModelRestaurant) -> Unit){
        onItemClickListener = listener
    }
}
