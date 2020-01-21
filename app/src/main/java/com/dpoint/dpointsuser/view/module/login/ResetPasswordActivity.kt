package com.dpoints.dpointsmerchant.view.module.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.ImageView
import androidx.lifecycle.Observer
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.auth.LoginModel
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.view.module.dashboard.Dashboard
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : BaseActivity() {
    override val layout: Int = R.layout.activity_reset_password
    private val viewModel by lazy { getVM<LoginViewModel>(this) }
    var email:String = ""

    override fun init() {
        val backBtn=findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }
        email = intent.getStringExtra("email")
        verifyEmail.text = email

        resetPassBtn.setOnClickListener {
            if(validate()){
                viewModel.resetPassword(email, newPass.text.toString(), forgotOTP.text.toString())
            }
        }

        addObserver()
    }

    private fun validate() = when {

        (forgotOTP.text != null && TextUtils.isEmpty(forgotOTP.text.toString().trim())) -> {
            forgotOTP.error = "Please Enter OTP"
            forgotOTP.requestFocus()
            false
        }
        (newPass.text != null && TextUtils.isEmpty(newPass.text.toString().trim())) -> {
            newPass.error = "Please Enter New Password"
            newPass.requestFocus()
            false
        }
        (confrmPass.text != null && TextUtils.isEmpty(confrmPass.text.toString().trim())) -> {
            confrmPass.error = "Please Confirm Password"
            confrmPass.requestFocus()
            false
        }
        (newPass.text.toString().trim() != confrmPass.text.toString().trim()) -> {
            confrmPass.error = "Password and Confirm password does not match"
            confrmPass.requestFocus()
            false
        }
        else -> {
            true
        }
    }

    private fun addObserver() {
        viewModel.resetState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer

            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }

            hideProgress()

            when (state) {
                is NetworkState.Success -> {
                    onVerifySuccess(state.data!!)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })

    }

    private fun onVerifySuccess(login: LoginModel) {
        Log.e("LOOGGED", login.toString())
        UserPreferences.instance.saveToken(this, login.token)
        UserPreferences.instance.saveUser(this, login.user!!)
        UserPreferences().setLoggedIn(this)
        startActivity<Dashboard>()
        finish()
    }

}
