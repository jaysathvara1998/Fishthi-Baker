package com.example.fishthibaker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class VisitFragment(val mContext: Context) : Fragment() {

//    var mMapView: MapView? = null
//    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_visit, container, false)

//        mMapView = rootView.findViewById(R.id.mapView)
//        mMapView!!.onCreate(savedInstanceState)
//        mMapView!!.onResume()

        val supportMapFragment:SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync { mMap ->
            val sydney = LatLng(23.4584119, 73.30038)

            mMap.addMarker(MarkerOptions().position(sydney).title("Fishthi Baker"))
            val cameraPosition = CameraPosition.Builder().target(sydney).zoom(15f).build()
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }

        try {
            MapsInitializer.initialize(mContext.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

//        mMapView!!.getMapAsync(object : OnMapReadyCallback {
//            override fun onMapReady(mMap: GoogleMap) {
//                googleMap = mMap
//
//                if (ActivityCompat.checkSelfPermission(
//                        mContext,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                        mContext,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    return
//                }
//                googleMap!!.isMyLocationEnabled = true
//
//                val sydney = LatLng(23.4584119, 73.30038)
//                googleMap!!.addMarker(
//                    MarkerOptions().position(sydney).title("Fishthi Baker")
//                )
//
//                val cameraPosition = CameraPosition.Builder().target(sydney).zoom(12f).build()
//                googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//            }
//
//        })

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}