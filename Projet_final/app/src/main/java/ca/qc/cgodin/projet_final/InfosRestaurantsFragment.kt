package ca.qc.cgodin.projet_final

import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_enregistrement.*
import kotlinx.android.synthetic.main.fragment_infos_restaurants.*
import kotlinx.android.synthetic.main.fragment_infos_restaurants.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfosRestaurantsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfosRestaurantsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val projetViewModel: ProjetViewModel by lazy {
        ViewModelProvider(
            this,
            ProjetViewModelFactory(requireActivity().application)
        ).get(ProjetViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        /*
        val args = arguments
        val resto : ModelRestaurant? = args?.getSerializable("article") as ModelRestaurant?

        if (resto != null) {
            ImageResto.setImageBitmap(resto.image)
            NameResto.text = resto.name
            AdresseResto.text = resto.name
        };
         */

        return inflater.inflate(R.layout.fragment_infos_restaurants, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val args = arguments
        val resto : ModelRestaurant? = args?.getSerializable("article") as ModelRestaurant?

        if (resto != null) {
            button4.setOnClickListener {
                var projetDao : ProjetDao = projetViewModel.projetDao
                var newResto : Restaurant = Restaurant(0,resto.placeId,resto.name,resto.adresse,resto.note,resto.totalNote, resto.image.toString(), resto.url,resto.numeroTelephone, resto.openNow, resto.latitude, resto.longitude, MainActivity.utilConnecte)
                var count = 0
                projetViewModel.projetDao.findFavorite(newResto.name, newResto.user).observe(
                    viewLifecycleOwner,
                    Observer<Restaurant> { resto -> // your code here
                        if(count == 0){
                            if(resto == null){
                                projetViewModel.insertRestaurant(newResto)
                                Toast.makeText(requireActivity(),resources.getString(R.string.aAjouter), Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(requireActivity(),resources.getString(R.string.nonAjouter), Toast.LENGTH_LONG).show()
                            }
                            count++
                        }
                    })

            }
            ImageResto.setImageBitmap(resto.image)
            NameResto.text = resto.name
            AdresseResto.text = resto.adresse
            Note.text = "${resto.note}/5"
            TotalNote.text = "${resto.totalNote}"

            if (resto.openNow == false){
                Ouvert.text = resources.getString(R.string.ouvert);
                Ouvert.setTextColor(Color.parseColor("#4CAF50"))
            }else{
                Ouvert.text = resources.getString(R.string.ferme);
                Ouvert.setTextColor(Color.parseColor("#F44336"))
            }

            if (resto.numeroTelephone != "rien"){
                NumeroTelephone.text = resto.numeroTelephone
            }

            if (resto.url != "") {
                LienSite.isClickable = true
                /*
                LienSite.movementMethod = LinkMovementMethod.getInstance()
                val text = "<a href='${resto.url}'> ${resto.url} </a>"
                LienSite.text = Html.fromHtml(text)
                 */

                val bundle = Bundle().apply {
                    putSerializable("article", resto)
                }

                LienSite.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_infosRestaurantsFragment_to_webView,
                        bundle
                    )
                }
            }else{
                LienSite.visibility = View.INVISIBLE
            }
        };
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfosRestaurantsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfosRestaurantsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}