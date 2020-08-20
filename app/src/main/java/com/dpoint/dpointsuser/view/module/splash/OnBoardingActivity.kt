package com.dpoint.dpointsuser.view.module.splash

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoint.dpointsuser.view.module.login.LoginOptionActivity
import com.dpoints.view.module.dashboard.Dashboard
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class OnBoardingActivity : AppCompatActivity() {
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        handler = Handler()
        Log.e("Facebook HashKey",printKeyHash(this))
        runnable = Runnable {

            if (UserPreferences().isLoggedIn(this@OnBoardingActivity)) {
                startActivity(Intent(this@OnBoardingActivity, Dashboard::class.java))
            } else {
                startActivity(Intent(this@OnBoardingActivity, LoginOptionActivity::class.java))
            }
            finish()
        }
    }
    override fun onStart() {
        super.onStart()
        handler?.postDelayed(runnable, 1500)
    }

    fun printKeyHash(context: Activity): String? {
        val packageInfo: PackageInfo
        var key: String? = null
        try {
            //getting application package name, as defined in manifest
            val packageName = context.applicationContext.packageName

            //Retriving package info
            packageInfo = context.packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            Log.e("Package Name=", context.applicationContext.packageName)
            for (signature in packageInfo.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                key = String(Base64.encode(md.digest(), 0))

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("Name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("No such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        }
        return key
    }
}
