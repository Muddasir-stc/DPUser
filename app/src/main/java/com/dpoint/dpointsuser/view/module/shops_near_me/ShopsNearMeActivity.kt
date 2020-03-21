package com.dpoint.dpointsuser.view.module.shops_near_me

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoint.dpointsuser.datasource.remote.shop.ShopModel
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import com.dpoints.view.module.shops.ShopDetailActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ShopsNearMeActivity : BaseFragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener,
    ClusterManager.OnClusterClickListener<Shop>,
    ClusterManager.OnClusterItemClickListener<Shop>,
    ClusterManager.OnClusterItemInfoWindowClickListener<Shop> {
    override val layout: Int = R.layout.activity_shops_near_me

    private var googleMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private var clusterManager: ClusterManager<Shop>? = null
    private var shop: ShopModel? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    private val TAG = "MAP LOCATION"
    override fun init(view: View) {
        mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        if (checkPlayService()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!AppUtils.isLocationEnabled(activity!!)) { // notify user
                val dialog =
                    AlertDialog.Builder(activity!!)
                dialog.setMessage("Location not enabled!")
                dialog.setPositiveButton(
                    "Open location settings"
                ) { paramDialogInterface, paramInt ->
                    val myIntent =
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                }
                dialog.setNegativeButton(
                    "Cancel"
                ) { paramDialogInterface, paramInt ->

                }
                dialog.show()
            }
            buildGoogleApiClient()
        } else {
            Toast.makeText(activity!!, "Location not supported in this device", Toast.LENGTH_SHORT)
                .show()
        }
    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(activity!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
    }

    override fun onStart() {
        super.onStart()
        try {
            mGoogleApiClient!!.connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        try {
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
        if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected()) {
            mGoogleApiClient!!.disconnect()
        }
    }

    private fun checkPlayService(): Boolean {
        val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity!!)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(
                    resultCode, activity!!,
                    PLAY_SERVICES_RESOLUTION_REQUEST
                ).show()
            } else { //finish();
            }
            return false
        }
        return true
    }

    override fun onMapReady(mMap: GoogleMap?) {
        this.googleMap = mMap
        setupMap(mMap)
        clusterManager = ClusterManager<Shop>(activity!!, mMap)
        googleMap!!.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(-26.167616, 28.079329),
                10f
            )
        )
        googleMap!!.setOnCameraIdleListener(clusterManager)
        googleMap!!.setOnMarkerClickListener(clusterManager)
        googleMap!!.setOnInfoWindowClickListener(clusterManager)
        addShops()
        clusterManager!!.cluster()
        var renderer = CustomMarkerRenderer(activity!!, googleMap!!, clusterManager!!)
        clusterManager!!.renderer = renderer
        clusterManager!!.setOnClusterClickListener(this)
        clusterManager!!.setOnClusterItemClickListener(this)
        clusterManager!!.setOnClusterItemInfoWindowClickListener(this)
    }

    private fun addShops() {
        for (i in shop!!.data) {
            clusterManager!!.addItem(i)
        }
    }


    private fun setupMap(mMap: GoogleMap?) {
        mMap!!.setBuildingsEnabled(true)
        mMap.setIndoorEnabled(true)
        mMap.setTrafficEnabled(true)
        val mUiSettings: UiSettings = mMap.getUiSettings()
        mUiSettings.isZoomControlsEnabled = true
        mUiSettings.isCompassEnabled = true
        mUiSettings.isMyLocationButtonEnabled = true
        mUiSettings.isScrollGesturesEnabled = true
        mUiSettings.isZoomGesturesEnabled = true
        mUiSettings.isTiltGesturesEnabled = true
        mUiSettings.isRotateGesturesEnabled = true
        // permissions
        // permissions
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.setMyLocationEnabled(true)
    }

    private var param1: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            shop = it.getParcelable(ARG_PARAM2)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: ShopModel) =
            ShopsNearMeActivity().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putParcelable(ARG_PARAM2, param2)
                }
            }
    }

    private fun changeMap(location: Location) {
        Log.d("FetchLocationActivity", "Reaching map$googleMap")
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) { // TODO: Consider calling
            return
        }
        // check if map is created successfully or not
        if (googleMap != null) {
            googleMap!!.getUiSettings().isZoomControlsEnabled = false
            val latLong: LatLng
            latLong = LatLng(location.latitude, location.longitude)
            val cameraPosition = CameraPosition.Builder()
                .target(latLong).zoom(10.5f).build()
            googleMap!!.setMyLocationEnabled(true)
            googleMap!!.getUiSettings().isMyLocationButtonEnabled = true
            googleMap!!.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(cameraPosition)
            )

        } else {
            Toast.makeText(
                activity!!,
                "Sorry! unable to create maps", Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) { // TODO: Consider calling
//    ActivityCompat#requestPermissions
// here to request the missing permissions, and then overriding
//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                          int[] grantResults)
// to handle the case where the user grants the permission. See the documentation
// for ActivityCompat#requestPermissions for more details.
            return
        }
        val mLastLocation =
            LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient
            )
        if (mLastLocation != null) {
            changeMap(mLastLocation)
            Log.d("FetchLocationActivity", "ON connected")
        } else try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val mLocationRequest = LocationRequest()
            mLocationRequest.interval = 10000
            mLocationRequest.fastestInterval = 5000
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onConnectionSuspended(i: Int) {
        Log.i("FetchLocationActivity", "Connection suspended")
        mGoogleApiClient!!.connect()
    }

    override fun onLocationChanged(location: Location?) {
        try {
            location?.let { changeMap(it) }
            LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {}


    inner class CustomMarkerRenderer(
        private val context: Context,
        private val map: GoogleMap,
        private val clusterManager: ClusterManager<Shop>
    ) : DefaultClusterRenderer<Shop>(context, map, clusterManager) {
        override fun onBeforeClusterItemRendered(item: Shop?, markerOptions: MarkerOptions?) {
            markerOptions!!.title(item!!.title)
            super.onBeforeClusterItemRendered(item, markerOptions)

        }

        override fun onBeforeClusterRendered(
            cluster: Cluster<Shop>?,
            markerOptions: MarkerOptions?
        ) {

        }

        override fun shouldRenderAsCluster(cluster: Cluster<Shop>?): Boolean {
            return super.shouldRenderAsCluster(cluster)
        }
    }

    override fun onClusterClick(p0: Cluster<Shop>?): Boolean {
        return true
    }

    override fun onClusterItemClick(p0: Shop?): Boolean {
        return false
    }

    override fun onClusterItemInfoWindowClick(p0: Shop?) {
        val intent = Intent(activity!!, ShopDetailActivity::class.java)
        var model = p0
        if (model!!.website == null)
            model.website = ""
        if (model.twitter == null)
            model.twitter = ""
        if (model.facebook == null)
            model.facebook = ""
        if (model.instagram == null)
            model.instagram = ""
        intent.putExtra("SHOP", model)
        startActivity(intent)
    }


}
