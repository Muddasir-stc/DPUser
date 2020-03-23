package com.dpoints.view.module.shops

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.budiyev.android.codescanner.CodeScanner
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.MenuModel
import com.dpoint.dpointsuser.datasource.remote.shop.Shop

import com.dpoint.dpointsuser.view.adapter.ShopViewPagerAdapter
import com.dpoint.dpointsuser.view.module.gifts.QrViewModel
import com.dpoint.dpointsuser.view.module.shops.ExchangeActivity
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.offer.Data
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.utilities.toJson
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.dpointsmerchant.view.module.dashboard.DashboardViewModel
import com.dpoints.dpointsmerchant.view.module.shops.ShopViewModel
import com.dpoints.view.adapter.MenuAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_shop_detail.*
import java.util.*


class ShopDetailActivity : BaseActivity(), LocationListener {
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {
    }


    var mRating = ""
    private var locationManager: LocationManager? = null
    private var selectedType: Int = 0
    private lateinit var dialog: BottomSheetDialog
    private lateinit var codeScanner: CodeScanner
    private val CAMERA_PERMISSIONS_REQUEST = 2
    private val viewModelDash by lazy { getVM<DashboardViewModel>(this) }
    var selectedData: String? = null
    //    private var giftData: List<com.dpoints.dpointsmerchant.datasource.remote.gift.Menu>? = null
    private var data: List<Data>? = null
    override val layout: Int = R.layout.activity_shop_detail
    private val viewModel by lazy { getVM<ShopViewModel>(this) }
    var shop: Shop? = null
    lateinit var offers: RecyclerView
    lateinit var gifts: RecyclerView
    private val viewQRModel by lazy { getVM<QrViewModel>(this) }
    override fun init() {
        if (intent.getParcelableExtra<Shop>("SHOP") != null) {
            shop = intent.getParcelableExtra<Shop>("SHOP")
            Log.e("SHOPPER", shop!!.toJson())
        }

        menuList.setHasFixedSize(true)
        val lin = LinearLayoutManager(this)
        lin.orientation = RecyclerView.HORIZONTAL
        menuList.layoutManager = lin
        viewModel.getShopMenus(UserPreferences.instance.getTokken(this)!!, shop!!.id.toString())
        txtFb.setOnClickListener {
            getBrowser(shop!!.facebook)
        }
        txtInsta.setOnClickListener {
            getBrowser(shop!!.instagram)
        }
        txtTweet.setOnClickListener {
            getBrowser(shop!!.twitter)
        }
        txtWeb.setOnClickListener {
            getBrowser(shop!!.website)
        }
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                101
            )
        }
        try {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000,
                5f,
                this as android.location.LocationListener
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
        btnExchange.setOnClickListener {
            val intent = Intent(this, ExchangeActivity::class.java)
            Log.e("COINVALUE", shop!!.coin_value.toString())
            intent.putExtra("SHOP", shop)
            startActivity(intent)
        }
        val fragmentAdapter = ShopViewPagerAdapter(supportFragmentManager, shop!!)
        viewPager_shop!!.adapter = fragmentAdapter
        //  viewPager_shop!!.isNestedScrollingEnabled=true
        tabs_main.setupWithViewPager(viewPager_shop)

/*        coin_offer.setOnClickListener {
            var user = UserPreferences.instance.getUser(this)
            val data =
                "{\"user_id\":\"${user?.id}\",\"name\":\"${user?.name}\"}"
            viewQRModel.getQrImage(data)
        }*/

        btnRating.setOnClickListener {
            val view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_rate, null, false)
            val builder = AlertDialog.Builder(this)
            builder.setView(view)
            val ratingBar = view.findViewById<RatingBar>(R.id.rate)
            val txtFeedback = view.findViewById<TextView>(R.id.txtFeedback)
            val btnFeedback = view.findViewById<Button>(R.id.btnFeedback)

            mRating = ratingBar.rating.toString()
            ratingBar.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    //  Toast.makeText(this,rating.toString(),Toast.LENGTH_SHORT).show()
                    mRating = rating.toString()
                }

            val dialog = builder.create()
            dialog.show()
            btnFeedback.setOnClickListener {
                viewModel.submitShopRating(
                    UserPreferences.instance.getTokken(this)!!,
                    UserPreferences.instance.getUser(this)!!.id.toString(),
                    mRating,
                    shop!!.id.toString(),
                    txtFeedback.text.toString()
                )
                dialog.dismiss()
            }
        }
        //   titleTag.setText(shop!!.title)
        txtTitle.text = shop!!.shop_name
        txtDesc.text = shop!!.description
        txtRating.text = shop!!.rating
        Log.d("rating123",shop!!.rating)
        txtMembership.text = shop!!.membership_status
        txtCoinValue.text = "${shop!!.shop_percentage.toString()}" + " %"
        //txtPhone.text = shop!!.contact
        //txtEmail.text = shop!!.email
        //txtAddress.text = "Get Location"

        Glide.with(this).load(shop!!.profile_picture).placeholder(R.drawable.error).into(banner)
        //  Glide.with(this).load(shop!!.image).into(img)
//        offers = findViewById(R.id.offers)
//        gifts = findViewById(R.id.gifts)
//        offers.setHasFixedSize(true)
//        gifts.setHasFixedSize(true)
//        val layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
//        val layoutManager2 = LinearLayoutManager(this, HORIZONTAL, false)
//        offers.layoutManager = layoutManager
//        gifts.layoutManager = layoutManager2
        //  viewModel.getShopOffers(UserPreferences.instance.getTokken(this)!!,shop!!.id.toString())
        //viewModel.getShopGifts(UserPreferences.instance.getTokken(this)!!,shop!!.id.toString())
        addObserver()
        val btnBack = findViewById<ImageView>(R.id.backBtn)
        btnBack.setOnClickListener {
            onBackPressed()
        }

//        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        val location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//
//        onLocationChanged(location)
    }

    private fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        var distance = FloatArray(2)
        Location.distanceBetween(lat1, lon1, lat2, lon2, distance)
        return distance[0];
    }

    override fun onProviderDisabled(provider: String) {
        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show()
    }


    override fun onLocationChanged(location: Location?) {
        val locationtext = "Latitude:" + location?.latitude + "\n Longitude :" + location?.longitude

        try {
            var lat2: Double = shop!!.latitude.toDouble()
            var lon2: Double = shop!!.longitude.toDouble()
            var distance =
                (getDistance(location!!.latitude, location!!.longitude, lat2, lon2) / 1000).toInt()
            Log.e("DISTANCE", distance.toString())
            txtDistance.text = "$distance km"
        } catch (e: java.lang.Exception) {
            txtDistance.text = "0 km"
        }

        try {
            val geocoder = Geocoder(
                this,
                Locale.getDefault()
            ) //transforming street address in longitude and latitude
            val addresses = geocoder.getFromLocation(location!!.latitude, location.longitude, 1)
            /*locationtext.setText(locationtext.getText() + "\n" + addresses.get(0).getAddressLine(0));*/if (addresses.size > 0) { // locationtext.setText(addresses.get(0).getCountryName());
//                locationtext1!!.text = addresses[0].adminArea
//                locationtext2!!.text = addresses[0].locality
//                locationtext3!!.text = addresses[0].postalCode
//                locationtext4!!.text = addresses[0].subLocality
            }
        } catch (e: Exception) {
        }
        Log.e("Location", locationtext)
    }

    private fun getBrowser(url: String) {
        var url = url
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url
        }
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun addObserver() {
        viewQRModel.qrState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }

            when (state) {
                is NetworkState.Success -> {
                    val builder: androidx.appcompat.app.AlertDialog.Builder =
                        androidx.appcompat.app.AlertDialog.Builder(this)
                    val dialogView: View =
                        LayoutInflater.from(this)
                            .inflate(R.layout.layout_qr_code, null, false)
                    builder.setView(dialogView)
                    var imageView = dialogView.findViewById<ImageView>(R.id.imageView_qrCode)

                    val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
                    imageView.setImageBitmap(state.data)
                    alertDialog.show()
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })

        viewModel.menuState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer //showProgress(this)
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    setupMenus(state?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
        viewModel.ratingState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer //showProgress(this)
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    onSuccess(state.data!!.message)
                    var oldRate = shop!!.rating.toFloat()
                    var newRate = mRating.toFloat()
                    var avgRate = (oldRate + newRate) / 2
                    txtRating.text = avgRate.toString()
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun setupMenus(data: MenuModel?) {
        if (data!!.data.size > 0) {
            textView_noMenu.visibility = View.GONE
            menuList.adapter = MenuAdapter(this, data!!.data)
        } else {
            textView_noMenu.visibility = View.VISIBLE
            menuList.visibility = View.GONE
        }
    }
//        viewModel.giftsState.observe(this, Observer {
//            it ?: return@Observer
//            val state = it.getContentIfNotHandled() ?: return@Observer
//            if (state is NetworkState.Loading) {
//                return@Observer// showProgress(this)
//            }
//            // hideProgress()
//            when (state) {
//                is NetworkState.Success -> {
//                    Log.e("DATA", state.data?.message.toString())
//                    giftData = state?.data?.data!!
//                    setupRecyclerViewGifts(state?.data)
//                }
//                is NetworkState.Error -> onError(state.message)
//                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
//                else -> onFailure(getString(R.string.connection_error))
//            }
//        })
//    }

//    private fun setupRecyclerViewGifts(data: GiftModel) {
//        gifts.adapter = GiftAdapter(this, this, data.data!!)
//    }
//
//    private fun setupRecyclerView(data: OfferModel) {
//        offers.adapter = OfferAdapter(data.data, this, this)
//    }

//    override fun onItemClick(index: Int, adapter: Int) {
//        selectedType = adapter
//        if (adapter == 1) {
//            selectedData = data?.get(index)?.id.toString()
//            getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            ); requestCameraPermission()
//        } else if (adapter == 2) {
//            selectedData = giftData?.get(index)?.id.toString()
//            getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            ); requestCameraPermission()
//        }
//    }
//
//    private fun requestCameraPermission() {
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CAMERA
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.CAMERA),
//                CAMERA_PERMISSIONS_REQUEST
//            )
//        } else {
//            openCamera()
//        }
//    }
//
//    private fun openCamera() {
//
//        val view = layoutInflater.inflate(R.layout.bottomsheet_scandata, null)
//        dialog = BottomSheetDialog(this)
//        dialog.setContentView(view)
//        (view.parent as View).setBackgroundColor(
//            ContextCompat.getColor(
//                this,
//                R.color.transparent
//            )
//        )
//        dialog.show()
//
//
//        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
//        codeScanner = CodeScanner(this, scannerView)
//        // Parameters (default values)
//        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
//        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
//        // ex. listOf(BarcodeFormat.QR_CODE)
//        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
//        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
//        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
//        codeScanner.isFlashEnabled = false // Whether to enable flash or not
//        codeScanner.startPreview()
//
//        // Callbacks
//        codeScanner.decodeCallback = DecodeCallback {
//            this.runOnUiThread {
//                //Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
//                if (selectedType == 1) {
//                    val offer = it.text.fromJson<ScanedOffer>()
//                    if (offer?.coin_offer_id?.trim().equals(selectedData?.trim())) {
//                        val builder = AlertDialog.Builder(this)
//
//                        // Set the alert dialog title
//                        //builder.setTitle("App background color")
//
//                        // Display a message on alert dialog
//                        builder.setMessage("Do You want to redeem this offer ?")
//
//                        // Set a positive button and its click listener on alert dialog
//                        builder.setPositiveButton("YES") { dialog, which ->
//                            // Do something when user press the positive button
//                            viewModelDash.assignOffer(
//                                UserPreferences.instance.getTokken(this)!!,
//                                UserPreferences.instance.getUser(this)!!.id.toString(),
//                                offer!!.merchant_id,
//                                offer!!.shop_id,
//                                offer!!.coin_offer_id,
//                                offer!!.coin_offer_title,
//                                offer!!.offer,
//                                offer!!.amount
//                            )
//                        }
//
//
//                        // Display a negative button on alert dialog
//                        builder.setNegativeButton("No") { dialog, which ->
//                            dialog.dismiss()
//                        }
//
//                        // Finally, make the alert dialog using builder
//                        val dialog: AlertDialog = builder.create()
//
//                        // Display the alert dialog on app interface
//                        dialog.show()
//                        //  Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
//
//                    } else {
//                        // Toast.makeText(context, "Scan result: Null", Toast.LENGTH_LONG).show()
//                        onError("Invalid Offer Selected")
//                    }
//                } else {
//                    val gift = it.text.fromJson<ScannedGift>()
//                    if (gift?.gift_card_id?.trim().equals(selectedData?.trim())) {
//                        val builder = android.app.AlertDialog.Builder(this)
//
//                        // Set the alert dialog title
//                        //builder.setTitle("App background color")
//
//                        // Display a message on alert dialog
//                        builder.setMessage("Do You want to redeem this offer ?")
//
//                        // Set a positive button and its click listener on alert dialog
//                        builder.setPositiveButton("YES") { dialog, which ->
//                            // Do something when user press the positive button
//                            viewModelDash.redeemGift(
//                                UserPreferences.instance.getTokken(this)!!,
//                                UserPreferences.instance.getUser(this)!!.id.toString(),
//                                gift!!.merchant_id,
//                                gift!!.shop_id,
//                                gift!!.gift_card_id,
//                                gift!!.gift_card_title,
//                                gift!!.coins,
//                                gift!!.offer
//                            )
//                        }
//
//
//                        // Display a negative button on alert dialog
//                        builder.setNegativeButton("No") { dialog, which ->
//                            dialog.dismiss()
//                        }
//
//                        // Finally, make the alert dialog using builder
//                        val dialog: android.app.AlertDialog = builder.create()
//
//                        // Display the alert dialog on app interface
//                        dialog.show()
//                        //  Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
//
//                    } else {
//                        // Toast.makeText(context, "Scan result: Null", Toast.LENGTH_LONG).show()
//                        onError("Invalid Gift Card Selected")
//                    }
//                }
//
//                dialog.dismiss()
//            }
//        }
//        codeScanner.errorCallback = ErrorCallback {
//            // or ErrorCallback.SUPPRESS
//            this.runOnUiThread() {
//                //                Toast.makeText(context, "Camera initialization error: ${it.message}",
////                    Toast.LENGTH_LONG).show()
//                onError("Camera initialization error: ${it.message}")
//
//            }
//
//        }
//    }
}
