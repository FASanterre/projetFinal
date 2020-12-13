package ca.qc.cgodin.projet_final

import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.restaurant_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class FavoriteAdapter (): RecyclerView.Adapter<FavoriteAdapter.RestaurantViewHolder>(){

    private var restaurants: MutableList<ModelRestaurant> = mutableListOf()
    private lateinit var onItemClickListener: ((ModelRestaurant) -> Unit)

    private var test : Int = 0

    inner class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logoResto: ImageView = itemView.findViewById(R.id.logo)
        val tvNom: TextView = itemView.findViewById(R.id.nom)
        val tvDescription: TextView = itemView.findViewById(R.id.adresse)
        val tvNote: TextView = itemView.findViewById(R.id.note)
        override fun toString(): String {
            return super.toString() + " '" + tvNom.text + "'"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_item, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holderArticle: RestaurantViewHolder, position: Int)
    {
        Log.i("test", "adapter")

        val resto = restaurants[position]
        holderArticle.tvNom.text = resto.name
        holderArticle.tvDescription.text = resto.adresse
        holderArticle.tvNote.text = resto.note
        //holderArticle.logoResto.setImageBitmap(resto.image)
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

    fun setRestaurants(model : ModelRestaurant) {
        this.restaurants.add(model)
        this.notifyDataSetChanged();
    }

    fun setOnItemClickListener(listener: (ModelRestaurant) -> Unit){
        onItemClickListener = listener
    }
}
