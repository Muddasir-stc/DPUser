package com.dpoints.view.module.offers

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.model.ScanedOffer
import com.dpoint.dpointsuser.view.module.offers.OfferViewModel
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.offer.Data
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferModel
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.*
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.dpointsmerchant.view.module.dashboard.DashboardViewModel
import com.dpoints.view.adapter.OfferAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_offers.*

class Offers : BaseActivity(), GetDialogInterface, OnItemClickListener {

    private lateinit var dialog: BottomSheetDialog
    private lateinit var codeScanner: CodeScanner
    lateinit var data: List<Data>
    lateinit var selectedData: String
    private lateinit var banners: ArrayList<Int>
    override val layout: Int = R.layout.activity_offers
    private var offerModel: OfferModel? = null
    private val CAMERA_PERMISSIONS_REQUEST = 2
    private val viewModelDash by lazy { getVM<DashboardViewModel>(this) }
    private val viewModel by lazy { getVM<OfferViewModel>(this) }
    override fun init() {
        banners = ArrayList()
        banners.add(R.drawable.group_3)
        banners.add(R.drawable.group_3)
        banners.add(R.drawable.group_3)
        banners.add(R.drawable.group_3)
        setupViewPager()
        backBtn.setOnClickListener {
            onBackPressed()
        }
        //addOffer.bringToFront()
        addObserver()

    }

    private fun addObserver() {
        viewModelDash.assignState.observe(this, Observer {
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
        viewModel.offersState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }
             hideProgress()
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

    }

    override fun onResume() {
        super.onResume()
        viewModel.getOffers(
            UserPreferences.instance.getTokken(this)!!
        )
    }


    private fun setupRecyclerView(data: OfferModel?) {
        var linearlayoutmanger = LinearLayoutManager(this)
        linearlayoutmanger.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearlayoutmanger
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = OfferAdapter( data!!.data,this,this)
    }

    private fun setupViewPager() {

        val pager = findViewById<ViewPager>(R.id.viewPager)
        pager.adapter = CustomViewPagerAdapter(this, banners)
    }


    private class CustomViewPagerAdapter(@param:NonNull @field:NonNull internal val mContext: Context?, @param:NonNull @field:NonNull internal val mObjects: List<Int>) :
        PagerAdapter() {

        override fun getCount(): Int {
            return mObjects.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val layout = LayoutInflater.from(mContext).inflate(
                R.layout.item_banner,
                container,
                false
            ) as ViewGroup
            val imageView = layout.findViewById<ImageView>(R.id.img_banner)
            imageView.setImageResource(mObjects[position])
            container.addView(layout)
            return layout

        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onItemClick(index: Int, adapter: Int) {
        selectedData=data[index].id.toString()
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); requestCameraPermission()
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
                val offer = it.text.fromJson<ScanedOffer>()
                if (offer?.coin_offer_id?.trim().equals(selectedData.trim())) {
                    val builder = android.app.AlertDialog.Builder(this)

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
                    val dialog: android.app.AlertDialog = builder.create()

                    // Display the alert dialog on app interface
                    dialog.show()
                    //  Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()

                } else {
                    // Toast.makeText(context, "Scan result: Null", Toast.LENGTH_LONG).show()
                    onError("Invalid Offer Selected")
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
//    class OfferAdapter(
//        private var context: Context,
//        private var data: List<Shop>
//    ) : RecyclerView.Adapter<OfferAdapter.MyViewHolder>() {
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//            val view =
//                LayoutInflater.from(parent.context).inflate(R.layout.item_offer, parent, false)
//            return MyViewHolder(view)
//        }
//
//        override fun getItemCount(): Int {
//            return data.size
//        }
//
//        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//            var model = data[position]
//            holder.textView_offerName.text = model.title
//            holder.textView_restrauntName.text = "Shop - " + model.shop_id
//            holder.textView_offer.text = model.description
//            holder.textView_coins.text = model.offer + " DPoints on Shopping of " + model.amount
//            Glide.with(context).load(model.image).placeholder(context.getDrawable(R.drawable.bitmap)).into(holder.imageView)
//            holder.textView_assign.setOnClickListener {
//
//            }
////            holder.textView_edit.setOnClickListener {
////                var intent = Intent(context, EditOffer::class.java)
////                intent.putExtra("actionType", "edit")
////                intent.putExtra("data", model)
////                context.startActivity(intent)
////            }
////            holder.textView_delete.setOnClickListener {
////                removeListener.onRemoveClick(position, "h")
////            }
//
//        }
//
//        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//            val cardView: CardView = view.findViewById(R.id.cardView)
//            val imageView: ImageView = view.findViewById(R.id.imageView)
//            val textView_offerName: TextView = view.findViewById(R.id.textView_offerName)
//            val textView_restrauntName: TextView = view.findViewById(R.id.textView_restrauntName)
//            val textView_offer: TextView = view.findViewById(R.id.textView_offer)
//            val textView_coins: TextView = view.findViewById(R.id.textView_coins)
//            val textView_assign: TextView = view.findViewById(R.id.textView_assign)
//        }
//    }

    override fun getDialogInterface(dialog: DialogInterface) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
//    override fun onRemoveClick(position: Int, itemId: String) {
//        var builder = AlertDialog.Builder(this)
//        builder
//            .setMessage("Do you want to delete this offer ?")
//            .setCancelable(false)
//            .setPositiveButton("Yes") { dialog, id ->
//                viewModel_editoffer.deleteOffer(
//                    UserPreferences.instance.getTokken(this)!!,
//                    offerModel!!.data[position].id.toString()
//                )
//                dialog.dismiss()
//            }
//            .setNegativeButton(
//                "No"
//            ) { dialog, id ->
//                dialog.dismiss()
//            }
////Creating dialog box
//        val alert = builder.create()
////Setting the title manually
//        alert.setTitle("Warning")
//        alert.show()
//    }

}
