package com.dpoints.view.module.shops

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.model.ScanedOffer
import com.dpoint.dpointsuser.datasource.model.ScannedGift
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.gift.GiftModel
import com.dpoints.dpointsmerchant.datasource.remote.offer.Data
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferModel
import com.dpoints.dpointsmerchant.datasource.remote.shop.Shop
import com.dpoints.dpointsmerchant.datasource.remote.shop.ShopModel
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.utilities.fromJson
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.dpointsmerchant.view.module.dashboard.DashboardViewModel
import com.dpoints.dpointsmerchant.view.module.shops.ShopViewModel
import com.dpoints.view.adapter.GiftAdapter
import com.dpoints.view.adapter.OfferAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_shop_detail.*
import java.lang.Exception

class ShopDetailActivity : BaseActivity(),OnItemClickListener {

    private var selectedType: Int=0
    private lateinit var dialog: BottomSheetDialog
    private lateinit var codeScanner: CodeScanner
    private val CAMERA_PERMISSIONS_REQUEST = 2
    private val viewModelDash by lazy { getVM<DashboardViewModel>(this) }
     var selectedData: String?=null
    private var giftData: List<com.dpoints.dpointsmerchant.datasource.remote.gift.Data>?=null
    private var data: List<Data>?=null
    override val layout: Int= R.layout.activity_shop_detail
    private val viewModel by lazy { getVM<ShopViewModel>(this) }
lateinit var shop:Shop
lateinit var offers:RecyclerView
lateinit var gifts:RecyclerView


    override fun init() {
        if(intent.getParcelableExtra<Shop>("SHOP")!=null){
            shop=intent.getParcelableExtra<Shop>("SHOP")
            Log.e("Shop",shop.title)
        }
        titleTag.setText(shop.title)
        txtTitle.text=shop.title
        txtDesc.text=shop.description
        txtPhone.text=shop.contact
        txtEmail.text=shop.email
        txtAddress.text="Get Location"

        txtAddress.setOnClickListener {
           try {
               val gmmIntentUri = Uri.parse("https://plus.codes/${shop.address}");


// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
               val mapIntent =  Intent(Intent.ACTION_VIEW, gmmIntentUri);
// Make the Intent explicit by setting the Google Maps package
               mapIntent.setPackage("com.google.android.apps.maps");

// Attempt to start an activity that can handle the Intent
               startActivity(mapIntent);
           }catch (e:Exception){
               Toast.makeText(this,"Invalid Location Found!",Toast.LENGTH_SHORT).show()
           }

        }
        Glide.with(this).load(shop.image).into(banner)
        Glide.with(this).load(shop.image).into(img)
        offers=findViewById(R.id.offers)
        gifts=findViewById(R.id.gifts)
        offers.setHasFixedSize(true)
        gifts.setHasFixedSize(true)
        val layoutManager=LinearLayoutManager(this,HORIZONTAL,false)
        val layoutManager2=LinearLayoutManager(this,HORIZONTAL,false)
        offers.layoutManager=layoutManager
        gifts.layoutManager=layoutManager2
        viewModel.getShopOffers(UserPreferences.instance.getTokken(this)!!,shop.id.toString())
        viewModel.getShopGifts(UserPreferences.instance.getTokken(this)!!,shop.id.toString())
        addObserver()
        val btnBack=findViewById<ImageView>(R.id.backBtn)
        btnBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun addObserver() {
        viewModel.offersState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer //showProgress(this)
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    data=state?.data?.data!!
                    setupRecyclerView(state?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
        viewModel.giftsState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer// showProgress(this)
            }
           // hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    giftData=state?.data?.data!!
                    setupRecyclerViewGifts(state?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun setupRecyclerViewGifts(data: GiftModel) {
        gifts.adapter=GiftAdapter(this,this,data.data!!)
    }

    private fun setupRecyclerView(data: OfferModel) {
        offers.adapter=OfferAdapter(data.data,this,this)
    }
    override fun onItemClick(index: Int, adapter: Int) {
        selectedType=adapter
        if(adapter==1){
            selectedData= data?.get(index)?.id.toString()
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); requestCameraPermission()
        }else if(adapter==2){
            selectedData= giftData?.get(index)?.id.toString()
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); requestCameraPermission()
        }
    }
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSIONS_REQUEST
            )
        } else {
            openCamera()
        }
    }

    private fun openCamera() {

        val view = layoutInflater.inflate(R.layout.bottomsheet_scandata, null)
        dialog = BottomSheetDialog(this)
        dialog.setContentView(view)
        (view.parent as View).setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.transparent
            )
        )
        dialog.show()


        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        codeScanner.startPreview()

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            this.runOnUiThread {
                //Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
               if(selectedType==1){
                   val offer = it.text.fromJson<ScanedOffer>()
                   if (offer?.coin_offer_id?.trim().equals(selectedData?.trim())) {
                       val builder = AlertDialog.Builder(this)

                       // Set the alert dialog title
                       //builder.setTitle("App background color")

                       // Display a message on alert dialog
                       builder.setMessage("Do You want to redeem this offer ?")

                       // Set a positive button and its click listener on alert dialog
                       builder.setPositiveButton("YES") { dialog, which ->
                           // Do something when user press the positive button
                           viewModelDash.assignOffer(
                               UserPreferences.instance.getTokken(this)!!,
                               UserPreferences.instance.getUser(this)!!.id.toString(),
                               offer!!.merchant_id,
                               offer!!.shop_id,
                               offer!!.coin_offer_id,
                               offer!!.coin_offer_title,
                               offer!!.offer,
                               offer!!.amount
                           )
                       }


                       // Display a negative button on alert dialog
                       builder.setNegativeButton("No") { dialog, which ->
                           dialog.dismiss()
                       }

                       // Finally, make the alert dialog using builder
                       val dialog: AlertDialog = builder.create()

                       // Display the alert dialog on app interface
                       dialog.show()
                       //  Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()

                   } else {
                       // Toast.makeText(context, "Scan result: Null", Toast.LENGTH_LONG).show()
                       onError("Invalid Offer Selected")
                   }
               }else{
                   val gift = it.text.fromJson<ScannedGift>()
                   if (gift?.gift_card_id?.trim().equals(selectedData?.trim())) {
                       val builder = android.app.AlertDialog.Builder(this)

                       // Set the alert dialog title
                       //builder.setTitle("App background color")

                       // Display a message on alert dialog
                       builder.setMessage("Do You want to redeem this offer ?")

                       // Set a positive button and its click listener on alert dialog
                       builder.setPositiveButton("YES") { dialog, which ->
                           // Do something when user press the positive button
                           viewModelDash.redeemGift(
                               UserPreferences.instance.getTokken(this)!!,
                               UserPreferences.instance.getUser(this)!!.id.toString(),
                               gift!!.merchant_id,
                               gift!!.shop_id,
                               gift!!.gift_card_id,
                               gift!!.gift_card_title,
                               gift!!.coins,
                               gift!!.offer
                           )
                       }


                       // Display a negative button on alert dialog
                       builder.setNegativeButton("No") { dialog, which ->
                           dialog.dismiss()
                       }

                       // Finally, make the alert dialog using builder
                       val dialog: android.app.AlertDialog = builder.create()

                       // Display the alert dialog on app interface
                       dialog.show()
                       //  Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()

                   } else {
                       // Toast.makeText(context, "Scan result: Null", Toast.LENGTH_LONG).show()
                       onError("Invalid Gift Card Selected")
                   }
               }

                dialog.dismiss()
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            // or ErrorCallback.SUPPRESS
            this.runOnUiThread() {
                //                Toast.makeText(context, "Camera initialization error: ${it.message}",
//                    Toast.LENGTH_LONG).show()
                onError("Camera initialization error: ${it.message}")

            }

        }
    }
}
