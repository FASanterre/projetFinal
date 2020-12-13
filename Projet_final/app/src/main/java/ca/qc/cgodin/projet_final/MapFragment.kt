package ca.qc.cgodin.projet_final

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private val projetViewModel: ProjetViewModel by lazy {
        ViewModelProvider(
            this,
            ProjetViewModelFactory(requireActivity().application)
        ).get(ProjetViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment =
            childFragmentManager.fragments[0] as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        return rootView
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        val point = CameraUpdateFactory.newLatLng(MainActivity.latitude?.let { MainActivity.longitude?.let { it1 ->
            LatLng(it,
                it1
            )
        } })
        mMap.moveCamera(point)
        mMap.addMarker(
            MainActivity.latitude?.let { MainActivity.longitude?.let { it1 -> LatLng(it, it1) } }
                ?.let {
                    MarkerOptions()
                        .position(it)
                        .title(resources.getString(R.string.maPosition))
                }
        )
        var count = 0
        projetViewModel.allFavorite.observe(this, Observer { restaurants ->
            if (count == 0) {
                for (resto: Restaurant in restaurants) {
                    Log.i("TAG",resto.user.toString() + "==" + MainActivity.utilConnecte)
                    if(resto.user == MainActivity.utilConnecte){
                        mMap.addMarker(
                            MarkerOptions().position(LatLng(resto.latitude, resto.longitude)).title(
                                resto.name
                            )
                        )
                    }
                }
            }
        })
        googleMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        if (p0 != null) {
            var count = 0
            projetViewModel.projetDao.findFavorite(p0.title).observe(
                this,
                Observer<Restaurant> { resto -> // your code here
                    if(count == 0){
                        if(p0.title != "Moi" && p0.title != "Me"){
                            var restoModel : ModelRestaurant = ModelRestaurant(resto.placeId,resto.name,resto.adresse,resto.note,resto.totalNote,null,resto.url,resto.numeroTelephone, resto.openNow, resto.latitude, resto.longitude)
                            val bundle = Bundle().apply {
                                putSerializable("article", restoModel)
                            }
                            findNavController().navigate(
                                R.id.action_mapFragment_to_infosRestaurantsFragment,
                                bundle
                            )
                        }
                        count++
                    }
                })
        }
        return false
    }
}