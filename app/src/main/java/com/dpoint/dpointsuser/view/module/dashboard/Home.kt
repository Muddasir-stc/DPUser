package com.dpoints.view.module.dashboard

import android.Manifest
import android.Manifest.permission_group.CAMERA
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.offer.Data
import com.dpoints.dpointsmerchant.datasource.remote.shop.Shop
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.utilities.toJson
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import com.dpoints.dpointsmerchant.view.module.dashboard.DashboardViewModel
import com.dpoints.view.adapter.OfferAdapter
import com.dpoints.view.adapter.ShopAdapter
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_home.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.dpoint.dpointsuser.datasource.model.ScanedOffer
import com.dpoints.dpointsmerchant.utilities.fromJson
import com.google.android.material.bottomsheet.BottomSheetDialog


class Home : BaseFragment(),OnItemClickListener {
    private lateinit var dialog: BottomSheetDialog
    private lateinit var codeScanner: CodeScanner
    override val layout: Int=R.layout.fragment_home
    lateinit var mShops: RecyclerView
    lateinit var mOffers: RecyclerView
    lateinit var data: List<Data>
    lateinit var adapter: ShopAdapter
    lateinit var offerAdapter: OfferAdapter
    private val CAMERA_PERMISSIONS_REQUEST = 2
    private val viewModel by lazy { getVM<DashboardViewModel>(activity!!) }
    override fun init(view: View) {
        shops.setHasFixedSize(true)
        offers.setHasFixedSize(true)
        val layoutManager=LinearLayoutManager(context)
        val layoutManager2=LinearLayoutManager(context)
        layoutManager.orientation= HORIZONTAL
        layoutManager2.orientation= HORIZONTAL
        mShops=view.findViewById(R.id.shops)
        mOffers=view.findViewById(R.id.offers)
        mShops.layoutManager=layoutManager
        mOffers.layoutManager=layoutManager2
        viewModel.getShops(UserPreferences.instance.getTokken(context!!)!!)
        viewModel.getOffers(UserPreferences.instance.getTokken(context!!)!!)
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
                    setUpShops(state.data?.data!!)
                }
            }
        })
        viewModel.offersState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
                //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    data=state?.data?.data!!
                    setOfferList(data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
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
      //  Log.e("OFFER",data[index].toJson().toString())
            //  context?.startActivity(Intent(context,ScanActivity::class.java))

        /*if (context?.checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( arrayOf(Manifest.permission.CAMERA), 100);
        }
        val inte = IntentIntegrator(activity)
        inte.initiateScan()
        inte.addExtra("ID", data[index].id.toString())
        inte.setOrientationLocked(false)*/
        getActivity()!!. getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); requestCameraPermission()
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
                Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            activity!!.runOnUiThread(){
                Toast.makeText(context, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onPause() {
        dialog.dismiss()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Log.d("RESULT", "Cancelled scan")
            } else {
                Log.d("RESULT", result.contents)
                Log.d("HomeFragment","HomeFragment ${data?.getStringExtra("ID")}")
                val offer=result.contents.toString().fromJson<ScanedOffer>()

            }
        }
    }


}