package com.dpoint.dpointsuser.view.module.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.datasource.remote.auth.LoginModel
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.getVM
import com.dpoint.dpointsuser.view.commons.base.BaseActivity
import com.dpoints.view.module.dashboard.Dashboard
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.mukesh.OtpView
import kotlinx.android.synthetic.main.activity_verify.*

class VerifyActivity : BaseActivity() {

    private val viewModel by lazy { getVM<LoginViewModel>(this) }
    override val layout: Int = R.layout.activity_verify
    private lateinit var otp_view: OtpView
    var email:String = ""

        override fun init() {
            email = intent.getStringExtra("email")
            verifyEmail.text = email

        resentOtpBtn.setOnClickListener {
            viewModel.resendOTP(email)
        }

        otp_view = findViewById(R.id.otp_view)

            otp_view.setOnClickListener {
                Log.e("CLICKED", "CLICKED" + it.id)
            }

        val imm = getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.showSoftInput(otp_view, 0)

        otp_view.setOtpCompletionListener { otp ->
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this){ instanceIdResult: InstanceIdResult ->
                val token = instanceIdResult.token
                viewModel.verify(email, token, otp)
            }

        }

        addObserver()

    }

    private fun getFirebseToken():String{
        var token: String = ""
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this){ instanceIdResult: InstanceIdResult ->
            token = instanceIdResult.token
            Log.e("newToken", token)
        }
        return token
    }

    private fun addObserver() {
        viewModel.verifyState.observe(this, Observer {
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

        viewModel.resendOtpState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer

            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }

            hideProgress()

            when (state) {
                is NetworkState.Success -> {
                    onSuccess("An OTP has been sent to $email")
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
