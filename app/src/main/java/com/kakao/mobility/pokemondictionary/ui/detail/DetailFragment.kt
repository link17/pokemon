package com.kakao.mobility.pokemondictionary.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kakao.mobility.pokemondictionary.R
import com.kakao.mobility.pokemondictionary.data.PokemonData
import com.kakao.mobility.pokemondictionary.databinding.FragmentDetailBinding
import com.kakao.mobility.pokemondictionary.ui.map.MapsFragment

class DetailFragment : Fragment() {

    companion object {
        private const val ARG_POKEMON = "ARG_POKEMON"

        fun newInstance(it: PokemonData) =  DetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_POKEMON,it)
            }
        }
    }

    private val pokemonData: PokemonData?
        get() = arguments?.getParcelable<PokemonData>(ARG_POKEMON)

    private val viewModel: DetailViewModel by lazy {
        ViewModelProviders.of(this).get(DetailViewModel::class.java).apply {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetailBinding.inflate(inflater, container, false).apply {
        viewModel = this@DetailFragment.viewModel
        lifecycleOwner = this@DetailFragment
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPokemonZip(pokemonData)
        viewModel.action.observe(this, Observer {
            fragmentManager?.beginTransaction()?.addToBackStack(null)?.setCustomAnimations(
                android.R.anim.slide_in_left,
               0, 0, android.R.anim.slide_out_right
            )
                ?.replace(R.id.container, MapsFragment.newInstance(it), MapsFragment.toString())
                ?.commit()
        })
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        when (requestCode) {
//            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
//                // If request is cancelled, the result arrays are empty.
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                } else {
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return
//            }
//
//            // Add other 'when' lines to check for other
//            // permissions this app might request.
//            else -> {
//                // Ignore all other requests.
//            }
//        }
//
//    }
}