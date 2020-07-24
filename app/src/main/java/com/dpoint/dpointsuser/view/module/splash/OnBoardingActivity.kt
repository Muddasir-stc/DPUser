package com.dpoints.view.module.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.view.module.login.LoginOptionActivity
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoints.view.module.dashboard.Dashboard
import com.dpoints.view.module.login.Login

class OnBoardingActivity : AppCompatActivity() {
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        handler = Handler()
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
}
