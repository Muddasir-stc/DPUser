package com.dpoints.view.module.profile

import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.datasource.remote.auth.LoginModel
import com.dpoint.dpointsuser.datasource.remote.auth.User
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.getVM
import com.dpoint.dpointsuser.view.commons.base.BaseActivity
import com.dpoint.dpointsuser.view.module.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_profile.*

class Profile : BaseActivity() {
    override val layout: Int= R.layout.activity_profile
    private val viewModel by lazy { getVM<LoginViewModel>(this) }
    override fun init() {

//        viewModel.getUser(UserPreferences.instance.getTokken(this)!!)
//        addObserver()
        findViewById<Button>(R.id.changePasswordProfile).setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        backBtn.setOnClickListener {
            onBackPressed()
        }
        getUser()
    }

    private fun addObserver() {
        viewModel.userState.observe(this, Observer {
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

    private fun onVerifySuccess(data: LoginModel) {
        UserPreferences.instance.saveUser(this, data.user!!)
        getUser()
    }

    private fun getUser(){
        val user: User = UserPreferences.instance.getUser(this)!!
        if (user.last_name != null) {
            profileName.text = "${user!!.name} ${user!!.last_name}"
        } else {
            profileName.text = "${user!!.name}"
        }

        if (user.email != null) {
            profileEmail.text = "${user!!.email}"
        } else {
           profileEmail.text = "-"
        }

        if (user.points_used != null) {
           profileUsed.text = " ${user.points_used}"
        } else {
           profileUsed.text = "0"
        }

        if (user.total_points != null) {
            profileBal.text = " ${user.total_points}"
        } else {
            profileBal.text = "0"
        }

        if (user.dob != null) {
            profileDob.text = user.dob.toString()
        } else {
          profileDob.text = "-"
        }

        if (user.contact_number != null) {
            profileNumber.text = user.contact_number.toString()
        } else {
            profileNumber.text = "-"
        }
    }
}