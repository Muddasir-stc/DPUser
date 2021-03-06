package com.dpoints.view.module.dashboard

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.model.ScanedOffer
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.fromJson
import com.dpoint.dpointsuser.utilities.getVM
import com.dpoint.dpointsuser.utilities.toJson
import com.dpoint.dpointsuser.view.commons.base.BaseFragment
import com.dpoint.dpointsuser.view.module.dashboard.DashboardViewModel
import kotlinx.android.synthetic.main.content_dashboard.*
import org.json.JSONObject
import java.lang.Exception


class ScannerFragment : BaseFragment(){
    private lateinit var codeScanner: CodeScanner
    private lateinit var scannerView: CodeScannerView
    override val layout: Int=R.layout.fragment_scanner
    private val viewModelDash by lazy { getVM<DashboardViewModel>(activity!!) }
    private val CAMERA_PERMISSIONS_REQUEST = 2
    override fun init(view: View) {
        scannerView=view.findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(context!!, scannerView)
        activity!!.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); requestCameraPermission()
        addObserver()
    }
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                context!!,
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
               try {
                   //Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                   val offer = JSONObject(it.text)
                   //Log.e("SCANNER",offer.())
                   val builder = android.app.AlertDialog.Builder(context)

                   // Set the alert dialog title
                   //builder.setTitle("App background color")

                   // Display a message on alert dialog
                   builder.setMessage("Do You want get ${offer?.getString("coin_offer_title")} offer ?")

                   // Set a positive button and its click listener on alert dialog
                   builder.setPositiveButton("YES") { dialog, which ->
                       // Do something when user press the positive button
                       viewModelDash.assignOffer(
                           UserPreferences.instance.getTokken(context!!)!!,
                           UserPreferences.instance.getUser(context!!)!!.id.toString(),
                           offer!!.getString("merchant_id"),
                           offer!!.getString("shop_id"),
                           offer!!.getString("coin_offer_id"),
                           offer!!.getString("coin_offer_title"),
                           offer!!.getString("offer"),
                           offer!!.getString("amount")
                       )
                   }


                   // Display a negative button on alert dialog
                   builder.setNegativeButton("No") { dialog, which ->
                       openCamera()
                       dialog.dismiss()
                   }

                   // Finally, make the alert dialog using builder
                   val dialog: android.app.AlertDialog = builder.create()

                   // Display the alert dialog on app interface
                   dialog.show()
                   //  Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()

               }catch (e:Exception){
                   onError("Invalid Offer")
               }

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

   private fun addObserver(){
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
                   openCamera()
               }
               is NetworkState.Error -> onError(state.message)
               is NetworkState.Failure -> onFailure(getString(R.string.request_error))
               else -> onFailure(getString(R.string.connection_error))
           }
       })
   }

    override fun onPause() {
        super.onPause()
        if (codeScanner != null)
            codeScanner.releaseResources()
    }
}