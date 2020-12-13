package ca.qc.cgodin.projet_final

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_search_restaurant.*
import kotlin.math.roundToInt


class SearchRestaurantFragment : Fragment() {

    companion object {
        fun newInstance() = SearchRestaurantFragment()
    }

    var seek : SeekBar? = null

    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v : View = inflater.inflate(R.layout.fragment_search_restaurant, container, false)

        seek = v.findViewById<SeekBar>(R.id.SliderRecherche)

        val args = arguments

        if (args?.getInt("distance") != null) {
            seek?.progress = args?.getInt("distance") as Int
        }

        seek?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                // write custom code for progress is changed
                IndicateurDistance.text = "${seek.progress / 10} km"
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
                seek.progress = (seek.progress / 10 * 10.toDouble()).roundToInt()

                var distanceRecherche = seek.progress

                if (seek.progress == 0){
                    distanceRecherche = 10
                }

                //searchAdapter.resetRestaurantsList()
                searchAdapter.setRestaurants(seek.progress)

                IndicateurDistance.text = "${distanceRecherche / 10} km"
            }
        })

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchAdapter = SearchAdapter()

        MainActivity.rechercheAdapter = searchAdapter

        RvRecherche.adapter = searchAdapter

        searchAdapter.setRestaurants(10)


        searchAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchRestaurantFragment_to_infosRestaurantsFragment,
                bundle
            )
        }
    }
}