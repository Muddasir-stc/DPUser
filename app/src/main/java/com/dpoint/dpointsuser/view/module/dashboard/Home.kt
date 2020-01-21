package com.dpoints.view.module.dashboard

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.offer.Data
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import com.dpoints.dpointsmerchant.view.module.dashboard.DashboardViewModel
import com.dpoints.view.adapter.OfferAdapter
import com.dpoints.view.adapter.ShopAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.datasource.model.ScanedOffer
import com.dpoint.dpointsuser.datasource.model.ScannedGift
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoint.dpointsuser.view.adapter.SliderAdapter
import com.dpoint.dpointsuser.view.module.dashboard.SearchActivity
import com.dpoint.dpointsuser.view.module.gifts.GiftCardsViewModel
import com.dpoints.dpointsmerchant.datasource.remote.gift.GiftModel
import com.dpoints.dpointsmerchant.utilities.fromJson
import com.dpoints.view.adapter.GiftAdapter
import com.dpoints.view.module.shops.ShopDetailActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView


class Home : BaseFragment(),OnItemClickListener {
    private var selectedGift: com.dpoints.dpointsmerchant.datasource.remote.gift.Data?=null
    private var selectedOffer: Data?=null
    private lateinit var dialog: BottomSheetDialog
    private lateinit var codeScanner: CodeScanner
    override val layout: Int=R.layout.fragment_home
    lateinit var mShops: RecyclerView
    lateinit var mOffers: RecyclerView
    lateinit var mGifts: RecyclerView
    lateinit var data: List<Data>
    lateinit var selectedData: String
    lateinit var list:List<Shop>
    var selectedType:Int=0
    lateinit var adapter: ShopAdapter
    lateinit var offerAdapter: OfferAdapter
    private val CAMERA_PERMISSIONS_REQUEST = 2
    lateinit var giftModel: GiftModel
    private val viewModel by lazy { getVM<DashboardViewModel>(activity!!) }
    private val giftCardsViewModel by lazy { getVM<GiftCardsViewModel>(activity!!) }
    override fun init(view: View) {
        shops.setHasFixedSize(true)
        offers.setHasFixedSize(true)
        gifts.setHasFixedSize(true)
        val layoutManager=LinearLayoutManager(context)
        val layoutManager2=LinearLayoutManager(context)
        val layoutManager3=LinearLayoutManager(context)
        layoutManager.orientation= HORIZONTAL
        layoutManager2.orientation= HORIZONTAL
        layoutManager3.orientation= HORIZONTAL
        mShops=view.findViewById(R.id.shops)
        mOffers=view.findViewById(R.id.offers)
        mGifts=view.findViewById(R.id.gifts)
       val txtSearch=view.findViewById<TextView>(R.id.txtSearch)

        txtSearch.setOnClickListener {
            context?.startActivity(Intent(context,SearchActivity::class.java))
        }
        val imageSlider=view.findViewById<SliderView>(R.id.imageSlider);
       val adapter =SliderAdapter(context);

        imageSlider.setSliderAdapter(adapter);

        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setIndicatorSelectedColor(Color.WHITE);
        imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
        imageSlider.startAutoCycle();

        mShops.layoutManager=layoutManager
        mOffers.layoutManager=layoutManager2
        mGifts.layoutManager=layoutManager3
       viewModel.getShops(UserPreferences.instance.getTokken(context!!)!!)
//        viewModel.getOffers(UserPreferences.instance.getTokken(context!!)!!)
//        giftCardsViewModel.getGiftCards(UserPreferences.instance.getTokken(context!!)!!)
        addObserver()
    }

    private fun addObserver() {
        viewModel.shopsState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            // hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA",state.data?.message)
                    list=state?.data?.data!!
                    setUpShops(state.data?.data!!)
                }
            }
        })
//        viewModel.offersState.observe(this, Observer {
//            it ?: return@Observer
//            val state = it.getContentIfNotHandled() ?: return@Observer
//            if (state is NetworkState.Loading) {
//                return@Observer
//            }
//                //hideProgress()
//            when (state) {
//                is NetworkState.Success -> {
//                    Log.e("DATA", state.data?.message.toString())
//                    data=state?.data?.data!!
//                    setOfferList(data)
//                }
//                is NetworkState.Error -> onError(state.message)
//                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
//                else -> onFailure(getString(R.string.connection_error))
//            }
//        })
//        viewModel.assignState.observe(this, Observer {
//            it ?: return@Observer
//            val state = it.getContentIfNotHandled() ?: return@Observer
//            if (state is NetworkState.Loading) {
//                return@Observer
//            }
//            //hideProgress()
//            when (state) {
//                is NetworkState.Success -> {
//                    Log.e("DATA", state.data?.message.toString())
//                    onSuccess(state.data!!.message)
//                }
//                is NetworkState.Error -> onError(state.message)
//                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
//                else -> onFailure(getString(R.string.connection_error))
//            }
//        })
//        giftCardsViewModel.giftCardState.observe(this, Observer {
//            it ?: return@Observer
//            val state = it.getContentIfNotHandled() ?: return@Observer
//            if (state is NetworkState.Loading) {
//                return@Observer
//            }
////             hideProgress()
//            when (state) {
//                is NetworkState.Success -> {
//                    Log.e("DATA",state.data?.message.toString())
//                    giftModel = state?.data!!
//                    setupRecyclerView(state?.data)
//                }
//                is NetworkState.Error -> onError(state.message)
//                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
//                else -> onFailure(getString(R.string.connection_error))
//            }
//        })
//        viewModel.redeemState.observe(this, Observer {
//            it ?: return@Observer
//            val state = it.getContentIfNotHandled() ?: return@Observer
//            if (state is NetworkState.Loading) {
//                return@Observer
//            }
//            //hideProgress()
//            when (state) {
//                is NetworkState.Success -> {
//                    Log.e("DATA", state.data?.message.toString())
//                    onSuccess(state.data!!.message)
//                }
//                is NetworkState.Error -> onError(state.message)
//                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
//                else -> onFailure(getString(R.string.connection_error))
//            }
//        })
    }

    private fun setOfferList(data: List<Data>) {
        offerAdapter = OfferAdapter(data,this,context!!)
        mOffers.adapter=offerAdapter
    }

    private fun setUpShops(data: List<Shop>) {
        adapter = ShopAdapter(data!!,this,context!!)
        mShops.adapter=adapter
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onItemClick(index: Int, adapter: Int) {
      if(adapter==0){
          val intent= Intent(context, ShopDetailActivity::class.java)
          intent.putExtra("SHOP",list.get(index))
          context!!.startActivity(intent)


      }else if(adapter==1){
          selectedData=data[index].id.toString()
          selectedOffer=data[index]
          selectedType=adapter
          //  Log.e("OFFER",data[index].toJson().toString())
          //  context?.startActivity(Intent(context,ScanActivity::class.java))


          getActivity()!!. getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); requestCameraPermission()

      }else if(adapter==2){
          selectedData= giftModel.data?.get(index)?.id.toString()
          selectedGift=giftModel.data?.get(index)
          selectedType=adapter
          //  Log.e("OFFER",data[index].toJson().toString())
          //  context?.startActivity(Intent(context,ScanActivity::class.java))


          getActivity()!!. getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); requestCameraPermission()
      }
    }



    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSIONS_REQUEST
            )
        } else {
            openCamera()
        }
    }

    private fun openCamera() {

        val view = layoutInflater.inflate(R.layout.bottomsheet_scandata, null)
        dialog = BottomSheetDialog(context!!)
        dialog.setContentView(view)
        (view.parent as View).setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                R.color.transparent
            )
        )
        dialog.show()


        val scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(context!!, scannerView)
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
            activity!!.runOnUiThread {
                //Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                if(selectedType==1){
                    val offer = it.text.fromJson<ScanedOffer>()
                    if (offer?.coin_offer_id?.trim().equals(selectedData.trim())) {

                        val view = LayoutInflater.from(context)
                            .inflate(R.layout.confirm_redeem_layout, null, false)
                        val textView_toolbar_title=view.findViewById<TextView>(R.id.textView_toolbar_title)
                        val textView_offerName=view.findViewById<TextView>(R.id.textView_offerName)
                        val textView_offer=view.findViewById<TextView>(R.id.textView_offer)
                        val textView_ammount=view.findViewById<TextView>(R.id.textView_ammount)
                        val textView_coins=view.findViewById<TextView>(R.id.textView_coins)
                        val btnConfirm=view.findViewById<Button>(R.id.btnConfirm)
                        val imgOffer=view.findViewById<ImageView>(R.id.imgOffer)
                        textView_toolbar_title.text="Offer Details"
                        textView_offerName.text=selectedOffer?.title
                        textView_ammount.text="\uF155 ${selectedOffer?.amount}"
                        textView_coins.text=" ${selectedOffer?.offer}"
                        textView_offer.text=selectedOffer?.description

                        Glide.with(context!!).load(selectedOffer?.image).into(imgOffer)
                        val builder = AlertDialog.Builder(context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        btnConfirm.setOnClickListener {

                            viewModel.assignOffer(
                                UserPreferences.instance.getTokken(context!!)!!,
                                UserPreferences.instance.getUser(context!!)!!.id.toString(),
                                offer!!.merchant_id,
                                offer!!.shop_id,
                                offer!!.coin_offer_id,
                                offer!!.coin_offer_title,
                                offer!!.offer,
                                offer!!.amount
                            )
                            dialog.dismiss()
                        }
//                        val builder = AlertDialog.Builder(context)
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
//                            viewModel.assignOffer(
//                                UserPreferences.instance.getTokken(context!!)!!,
//                                UserPreferences.instance.getUser(context!!)!!.id.toString(),
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

                    } else {
                        // Toast.makeText(context, "Scan result: Null", Toast.LENGTH_LONG).show()
                        onError("Invalid Offer Selected")
                    }
                }else if (selectedType==2){
                    val gift = it.text.fromJson<ScannedGift>()
                    if (gift?.gift_card_id?.trim().equals(selectedData.trim())) {


                        val view = LayoutInflater.from(context)
                            .inflate(R.layout.confirm_redeem_layout, null, false)
                        val textView_toolbar_title=view.findViewById<TextView>(R.id.textView_toolbar_title)
                        val textView_offerName=view.findViewById<TextView>(R.id.textView_offerName)
                        val textView_offer=view.findViewById<TextView>(R.id.textView_offer)
                        val textView_ammount=view.findViewById<TextView>(R.id.textView_ammount)
                        val textView_coins=view.findViewById<TextView>(R.id.textView_coins)
                        val btnConfirm=view.findViewById<Button>(R.id.btnConfirm)
                        val imgOffer=view.findViewById<ImageView>(R.id.imgOffer)
                        textView_toolbar_title.text="Gift Details"
                        textView_offerName.text=selectedGift?.title
                        textView_ammount.text="\uF155 ${selectedGift?.offer}"
                        textView_coins.text=" ${selectedGift?.coins}"
                        textView_offer.text=selectedGift?.description

                        Glide.with(context!!).load(selectedGift?.image).into(imgOffer)
                        val builder = AlertDialog.Builder(context)
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        btnConfirm.setOnClickListener {

                            viewModel.redeemGift(
                                UserPreferences.instance.getTokken(context!!)!!,
                                UserPreferences.instance.getUser(context!!)!!.id.toString(),
                                gift!!.merchant_id,
                                gift!!.shop_id,
                                gift!!.gift_card_id,
                                gift!!.gift_card_title,
                                gift!!.coins,
                                gift!!.offer
                            )
                            dialog.dismiss()
                        }


//                        val builder = android.app.AlertDialog.Builder(context)
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
//                            viewModel.redeemGift(
//                                UserPreferences.instance.getTokken(context!!)!!,
//                                UserPreferences.instance.getUser(context!!)!!.id.toString(),
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
            activity!!.runOnUiThread() {
                //                Toast.makeText(context, "Camera initialization error: ${it.message}",
//                    Toast.LENGTH_LONG).show()
                onError("Camera initialization error: ${it.message}")

            }

        }
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//        if (result != null) {
//            if (result.contents == null) {
//                Log.d("RESULT", "Cancelled scan")
//            } else {
//                Log.d("RESULT", result.contents)
//                Log.d("HomeFragment","HomeFragment ${data?.getStringExtra("ID")}")
//                val offer=result.contents.toString().fromJson<ScanedOffer>()
//
//            }
//        }
//    }

    private fun setupRecyclerView(giftModel: GiftModel?) {
        mGifts.adapter = GiftAdapter(context!!, this,giftModel!!.data!!)
    }
}