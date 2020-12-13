package ca.qc.cgodin.projet_final

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_all_restaurants.*
import kotlinx.android.synthetic.main.fragment_favorite_restaurants.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteRestaurantsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteRestaurantsFragment : Fragment() {
    private val projetViewModel: ProjetViewModel by lazy {
        ViewModelProvider(
            this,
            ProjetViewModelFactory(requireActivity().application)
        ).get(ProjetViewModel::class.java)
    }

    companion object {
        fun newInstance() = FavoriteRestaurantsFragment()
    }

    private lateinit var newsAdapter: FavoriteAdapter
    //private val newsViewModel:NewsViewModel by navGraphViewModels(R.id.news_nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_restaurants, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        newsAdapter = FavoriteAdapter()
        rvFavoriteRestaurants.adapter = newsAdapter

        projetViewModel.allFavorite.observe(viewLifecycleOwner, Observer { restaurants ->
            for (resto: Restaurant in restaurants) {
                if (resto.user == MainActivity.utilConnecte) {
                    var restoModel : ModelRestaurant = ModelRestaurant(resto.placeId,resto.name,resto.adresse,resto.note,resto.totalNote,null,resto.url,resto.numeroTelephone, resto.openNow, resto.latitude, resto.longitude)
                    newsAdapter.setRestaurants(restoModel)
                }
            }
        })

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_favoriteRestaurantsFragment_to_infosRestaurantsFragment,
                bundle
            )
        }
    }
}