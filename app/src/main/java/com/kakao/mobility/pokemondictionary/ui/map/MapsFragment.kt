package com.kakao.mobility.pokemondictionary.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kakao.mobility.pokemondictionary.R
import com.kakao.mobility.pokemondictionary.data.LocationData


class MapsFragment : Fragment() {

    companion object {
        private const val ARG_LOCATIONS = "ARG_LOCATIONS"
        fun newInstance(locations: ArrayList<LocationData>) = MapsFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(ARG_LOCATIONS, locations)
            }
        }

        private const val LOCATION_REQUEST_CODE = 486

    }

    private lateinit var mMap: GoogleMap

    private val locations
        get() = arguments?.getParcelableArrayList<LocationData>(ARG_LOCATIONS)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.findFragmentById(R.id.map).apply {
            this as SupportMapFragment
            getMapAsync { googleMap ->
                mMap = googleMap

                val zoomLevel = 16.0f //This goes up to 21

                locations?.forEach {
                    mMap.addMarker(
                        MarkerOptions().position(LatLng(it.lat, it.lng)).title("Marker in Sydney")
                    )

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        activity?.let { it ->
                            if (ContextCompat.checkSelfPermission(
                                    it,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION
                                ) == PackageManager.PERMISSION_GRANTED
                            )
                                mMap.isMyLocationEnabled = true
                            else
                                requestPermissions( arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                                    LOCATION_REQUEST_CODE)
                        }
                    }

                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(it.lat, it.lng),
                            zoomLevel
                        )
                    )
                }

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode ==LOCATION_REQUEST_CODE){
            if (permissions.size == 1 &&
                permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = true
            }
        }
    }
}