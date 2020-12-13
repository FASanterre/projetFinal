package ca.qc.cgodin.projet_final

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import kotlinx.android.synthetic.main.fragment_all_restaurants.*

class AllRestaurantsFragment : Fragment() {

    companion object {
        fun newInstance() = AllRestaurantsFragment()
    }

    private lateinit var newsAdapter: RestaurantAdapter
    //private val newsViewModel:NewsViewModel by navGraphViewModels(R.id.news_nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_restaurants, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        newsAdapter = RestaurantAdapter()
        rvAllRestaurants.adapter = newsAdapter
        newsAdapter.setRestaurants()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_allRestaurantsFragment_to_infosRestaurantsFragment,
                bundle
            )
        }
    }
}