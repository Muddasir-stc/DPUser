package com.dpoints.view.module.profile

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.Observer
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.dpointsmerchant.view.module.login.LoginViewModel
import com.dpoints.dpointsmerchant.view.module.login.ResetPasswordActivity
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_reset_password.confrmPass
import kotlinx.android.synthetic.main.activity_reset_password.newPass

class ChangePasswordActivity : BaseActivity() {
    override val layout: Int = R.layout.activity_change_password
    private val viewModel by lazy { getVM<LoginViewModel>(this) }
    override fun init() {

        chngPssBtn.setOnClickListener {
            if(validate()){
                viewModel.changePassword(UserPreferences.instance.getTokken(this)!!, oldPass.text.toString(), newPasss.text.toString())
            }
        }
        val backBtn=findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }
        addObserver()
    }

    private fun validate() = when {


        (oldPass.text != null && TextUtils.isEmpty(oldPass.text.toString().trim())) -> {
            oldPass.error = "Please Enter Old Password"
            oldPass.requestFocus()
            false
        }
        (newPasss.text != null && TextUtils.isEmpty(newPasss.text.toString().trim())) -> {
            newPasss.error = "Please Enter New Password"
            newPasss.requestFocus()
            false
        }
        (newPasss.text.length !in 6..15)->{
            newPasss.error="Password Length Must Be 6-15"
            newPasss.requestFocus()
            false
        }
       
        (confrmPasss.text != null && TextUtils.isEmpty(confrmPasss.text.toString().trim())) -> {
            confrmPasss.error = "Please Conform Password"
            confrmPasss.requestFocus()
            false
        }
        (newPasss.text.toString().trim() != confrmPasss.text.toString().trim()) -> {
            confrmPasss.error = "Password and Confirm password does not match"
            confrmPasss.requestFocus()
            false
        }
        else -> {
            true
        }
    }

    private fun addObserver() {
        viewModel.changeState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer

            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }

            hideProgress()

            when (state) {
                is NetworkState.Success -> {
                    Log.e("DAta",state.data?.message)
                    onSuccess(state.data?.message.toString())
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })

    }
}
