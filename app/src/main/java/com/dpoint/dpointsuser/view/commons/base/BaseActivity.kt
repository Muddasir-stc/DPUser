package com.dpoints.dpointsmerchant.view.commons.base

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.chancecoin.chance.views.commons.dialog.ResponseDialog
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.ApiClient
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.LoginType
import com.dpoints.view.module.dashboard.Dashboard
import com.dpoints.view.module.login.Login
import com.facebook.login.LoginManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


abstract class BaseActivity : AppCompatActivity(){

    abstract val layout: Int @LayoutRes get
    abstract fun init()
    lateinit var progressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        setupFocusOutside(findViewById(android.R.id.content))
        init()

    }

    open fun showProgress(context:Context) {
        progressDialog = ProgressDialog.show(context, "Please wait...", "Processing ...", false, false)
    }

    open fun hideProgress() {
        when {
            (::progressDialog.isInitialized) -> {
                progressDialog.dismiss()
            }
        }
    }


    fun onFailure(message: CharSequence, view: View = findViewById(R.id.root_view)) {
        Log.e("FALIURE", "ON")
        onFailure(view, message)
    }

    fun onSuccess(message: CharSequence, view: View = findViewById(R.id.root_view)) {
        Log.e("SUCCESS", "ON")
        onSuccess(view, message)
    }

    @JvmOverloads
    open fun onError(
        message: String,
        clickListener: () -> Unit = {},
        cancellable: Boolean = false,
        @DrawableRes icon: Int = R.drawable.ic_error,
        btnText: String = getString(R.string.okay),
        view: View = findViewById(R.id.root_view)
    ) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorError))
        val textView =
            snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackBar.show()
    }

    open fun onFailure(view: View, message: CharSequence) {
        Log.e("FALIURE", "ON")
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorError))
        val textView =
            snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackBar.show()
    }

    open fun onSuccess(view: View, message: CharSequence) {
        Log.e("SUCCESS", "ON")
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        val textView =
            snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackBar.show()
    }

    open fun bottomnavigatio(view: View) {

    }

    fun logout() {
        ApiClient.clearInstance()
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()
        FirebaseAuth.getInstance().signOut()
        UserPreferences.instance.clear(this)
        startLoginActivity()
    }

    fun hideKeyboard() {
        val currentView = this.currentFocus
        if (currentView != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentView.windowToken, 0)
        }
    }

    open fun isNetworkConnected(): Boolean {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return manager?.activeNetworkInfo?.isConnected ?: false
    }

    open fun startLoginActivity() {
        val intent = Intent(Intent(this, Login::class.java))
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    fun startDashboardActivity() {
        startActivity(Intent(this, Dashboard::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    inline fun <reified T : Activity> startActivity(bundle: Bundle? = null) {
        if (bundle != null) {
            return startActivity(Intent(this, T::class.java).putExtras(bundle))
        }

        startActivity(Intent(this, T::class.java))
    }

    fun clearNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.cancelAll()
    }

    private fun setupFocusOutside(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideKeyboard()
                false
            }
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupFocusOutside(innerView)
            }
        }
    }

    fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode,
                    PLAY_SERVICES_RESOLUTION_REQUEST
                )
            }

            return false
        }

        return true
    }

    fun isGpsEnabled(): Boolean {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        return manager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
    }
    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    fun showPermissionDialog(msg:String, context:Context,permission:String) {
        val alertBuilder = AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg);
        alertBuilder.setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialogInterface, i ->
            ActivityCompat.requestPermissions(context as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        })
        val alert = alertBuilder.create();
        alert.show();
    }
    fun checkPermission_WRITE_EXTERNAL_STORAGE(context:Context):Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showPermissionDialog("External storage permission is necessary to access photos for upload", context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    companion object {
        private const val PLAY_SERVICES_RESOLUTION_REQUEST = 876
    }

}