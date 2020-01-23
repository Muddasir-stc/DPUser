package com.dpoints.view.module.shops

import android.Manifest
import android.app.AlertDialog
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.budiyev.android.codescanner.CodeScanner
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoint.dpointsuser.view.adapter.ShopViewPagerAdapter
import com.dpoints.dpointsmerchant.datasource.remote.offer.Data
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.dpointsmerchant.view.module.dashboard.DashboardViewModel
import com.dpoints.dpointsmerchant.view.module.shops.ShopViewModel
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_shop_detail.*
import android.location.LocationManager
import androidx.core.app.ComponentActivity.ExtraData
import android.content.pm.PackageManager
import android.Manifest.permission
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import androidx.core.app.ActivityCompat
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpoint.dpointsuser.datasource.remote.shop.MenuModel
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.view.adapter.MenuAdapter


class ShopDetailActivity : BaseActivity(), LocationListener {
    override fun onLocationChanged(p0: Location?) {
        Toast.makeText(this,"lat = ${p0?.latitude} lon = ${p0?.longitude}",Toast.LENGTH_SHORT).show()
    }

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
    lateinit var shop: Shop
    lateinit var offers: RecyclerView
    lateinit var gifts: RecyclerView


    override fun init() {
        if (intent.getParcelableExtra<Shop>("SHOP") != null) {
            shop = intent.getParcelableExtra<Shop>("SHOP")
            Log.e("SHOPPER", shop.shop_name)
        }

        menuList.setHasFixedSize(true)
        val lin=LinearLayoutManager(this)
        lin.orientation=RecyclerView.HORIZONTAL
        menuList.layoutManager=lin
        viewModel.getShopMenus(UserPreferences.instance.getTokken(this)!!,shop.id.toString())
        txtFb.setOnClickListener {
            getBrowser(shop.facebook)
        }
        txtInsta.setOnClickListener {
            getBrowser(shop.instagram)
        }
        txtTweet.setOnClickListener {
            getBrowser(shop.twitter)
        }
        txtWeb.setOnClickListener {
            getBrowser(shop.website)
        }
        val fragmentAdapter = ShopViewPagerAdapter(supportFragmentManager, shop)
        viewPager_shop.adapter = fragmentAdapter
      //  viewPager_shop.isNestedScrollingEnabled=true
        tabs_main.setupWithViewPager(viewPager_shop)

        btnRating.setOnClickListener {
            val view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_rate, null, false)
            val builder = AlertDialog.Builder(this)
            builder.setView(view)
            val ratingBar=view.findViewById<RatingBar>(R.id.rate)

            ratingBar.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->Toast.makeText(this,rating.toString(),Toast.LENGTH_SHORT).show() }
            val dialog = builder.create()
            dialog.show()
        }
     //   titleTag.setText(shop.title)
        txtTitle.text = shop.shop_name
        txtDesc.text = shop.description
        txtRating.text=shop.rating
        txtMembership.text=shop.membership_status
        txtCoinValue.text="${shop.coin_value.toString()} "
        //txtPhone.text = shop.contact
        //txtEmail.text = shop.email
        //txtAddress.text = "Get Location"

//        txtAddress.setOnClickListener {
//            try {
//                val gmmIntentUri = Uri.parse("https://plus.codes/${shop.address}");
//
//
//// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
//                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri);
//// Make the Intent explicit by setting the Google Maps package
//                mapIntent.setPackage("com.google.android.apps.maps");
//
//// Attempt to start an activity that can handle the Intent
//                startActivity(mapIntent);
//            } catch (e: Exception) {
//                Toast.makeText(this, "Invalid Location Found!", Toast.LENGTH_SHORT).show()
//            }
//
//        }
        Glide.with(this).load(shop.profile_picture).into(banner)
      //  Glide.with(this).load(shop.image).into(img)
//        offers = findViewById(R.id.offers)
//        gifts = findViewById(R.id.gifts)
//        offers.setHasFixedSize(true)
//        gifts.setHasFixedSize(true)
//        val layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
//        val layoutManager2 = LinearLayoutManager(this, HORIZONTAL, false)
//        offers.layoutManager = layoutManager
//        gifts.layoutManager = layoutManager2
        //  viewModel.getShopOffers(UserPreferences.instance.getTokken(this)!!,shop.id.toString())
        //viewModel.getShopGifts(UserPreferences.instance.getTokken(this)!!,shop.id.toString())
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

    private fun getBrowser(url:String) {
        var url=url
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url
        }
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

   private fun addObserver() {
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
   }

    private fun setupMenus(data: MenuModel?) {
        menuList.adapter= MenuAdapter(this,data!!.data)
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
