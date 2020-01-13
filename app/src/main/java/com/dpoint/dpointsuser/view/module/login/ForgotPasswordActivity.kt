package com.dpoints.dpointsmerchant.view.module.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.et_email

class ForgotPasswordActivity : BaseActivity() {
    private val viewModel by lazy { getVM<LoginViewModel>(this) }
    override val layout: Int = R.layout.activity_forgot_password

    override fun init() {
        forgotPssBtn.setOnClickListener {
            if(validate())
            viewModel.forgotPassword(et_email.text.toString())
        }
        val backBtn=findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }
        addObserver()

    }


    private fun validate() = when {


        (et_email.text != null && TextUtils.isEmpty(et_email.text.toString().trim())) -> {
            et_email.error = "Please Enter Email Address"
            et_email.requestFocus()
            false
        }
        (!Patterns.EMAIL_ADDRESS.matcher(et_email.text.toString()).matches()) -> {
            et_email.error = "Invalid Email!"
            et_email.requestFocus()
            false
        }
        else -> {
            true
        }
    }


    private fun addObserver() {
        viewModel.forgotState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer

            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }

            hideProgress()

            when (state) {
                is NetworkState.Success -> {
                    startActivity<ResetPasswordActivity>(Bundle().apply { putString("email", et_email.text.toString()) })
                    finish()
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })

    }

}
