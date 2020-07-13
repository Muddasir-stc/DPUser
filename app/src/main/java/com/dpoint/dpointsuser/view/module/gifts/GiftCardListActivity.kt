package com.dpoint.dpointsuser.view.module.gifts

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.budiyev.android.codescanner.*
import com.budiyev.android.codescanner.CodeScanner
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.model.ScannedGift
import com.dpoint.dpointsuser.datasource.remote.gift.Data
import com.dpoint.dpointsuser.datasource.remote.gift.GiftModel
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.utilities.fromJson
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.dpointsmerchant.view.module.dashboard.DashboardViewModel
import com.dpoints.view.adapter.GiftAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_gifts.*
import kotlinx.android.synthetic.main.activity_offers.recyclerView

class GiftCardListActivity : BaseActivity(), OnItemClickListener {
    private lateinit var dialog: BottomSheetDialog
    private lateinit var codeScanner: CodeScanner
    lateinit var selectedData: String
    private val viewModelDash by lazy { getVM<DashboardViewModel>(this) }
    private val CAMERA_PERMISSIONS_REQUEST = 2
    var qr_image: ImageView? = null
    override val layout: Int = R.layout.activity_gift_card_list
    private val viewModel by lazy { getVM<GiftCardsViewModel>(this) }
    private val viewQRModel by lazy { getVM<QrViewModel>(this) }
    private var giftModel: GiftModel? = null
    override fun init() {
        var backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }
        addObserver()
        var giftCards = intent.getParcelableArrayListExtra<Data>("data")
        var shopName = intent.getStringExtra("shopName")
        var data = ArrayList<Data>()
        for(gift in giftCards){
            if(gift.shop_name.equals(shopName,true))
                data.add(gift)
        }
        giftModel = GiftModel(data,"")
        setupRecyclerView(giftModel)
    }


    private fun addObserver() {
        viewModelDash.redeemState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    onSuccess(state.data!!.message)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
        viewQRModel.qrState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }
            hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    qr_image!!.setImageBitmap(state.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })


    }
    private fun setupRecyclerView(giftModel: GiftModel?) {

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = GiftAdapter(this, this, giftModel!!.data!!)
    }

    override fun onItemClick(index: Int, adapter: Int) {
        val giftOffer = giftModel!!.data!![index]
        //  Log.e("OFFER",data[index].toJson().toString())
        //  context?.startActivity(Intent(context,ScanActivity::class.java))
        val view = LayoutInflater.from(this)
            .inflate(R.layout.dialog_purchase, null, false)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        qr_image = view.findViewById<ImageView>(R.id.qr_image)
        val data =
            "{\"type\":\"purchase\",\"gift_card_id\":\"${giftOffer?.id}\",\"user_id\":\"${UserPreferences.instance.getUser(
                this
            )!!.id}\",\"gift_card_title\":\"${giftOffer?.title}\",\"amount\":\"${giftOffer?.amount}\",\"number_of_units\":\"${giftOffer?.number_of_units}\",\"unit\":\"${giftOffer?.unit}\"}"
        viewQRModel.getQrImage(data)
        val dialog = builder.create()
        dialog.show()

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); requestCameraPermission()
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

    override fun onPause() {
        when {
            (::codeScanner.isInitialized) -> {
                codeScanner.releaseResources();
            }
        }
        super.onPause()
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
                Log.e("GIFTDATA", it.text)
                //Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                val gift = it.text.fromJson<ScannedGift>()
                if (gift?.gift_card_id?.trim().equals(selectedData.trim())) {
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
