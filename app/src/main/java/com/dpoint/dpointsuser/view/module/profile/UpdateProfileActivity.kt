package com.dpoint.dpointsuser.view.module.profile

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.auth.LoginModel
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.dpointsmerchant.view.module.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_update_profile.*
import kotlinx.android.synthetic.main.activity_update_profile.et_dob
import kotlinx.android.synthetic.main.activity_update_profile.et_first_name
import kotlinx.android.synthetic.main.activity_update_profile.et_last_name
import kotlinx.android.synthetic.main.activity_update_profile.et_phone
import java.text.SimpleDateFormat
import java.util.*

class UpdateProfileActivity : BaseActivity() {
    override val layout: Int=R.layout.activity_update_profile
    private val viewModel by lazy { getVM<LoginViewModel>(this) }
    override fun init() {
        val user=UserPreferences.instance.getUser(this)!!
        et_first_name.setText(user.name)

        if(user.contact_number!=null){
            et_phone.setText("${user.contact_number}")
        }else{
            et_phone.setText("")
        }
        if(user.last_name!=null){
            et_last_name.setText("${user.last_name}")
        }else{
            et_last_name.setText("")
        }
        if(user.dob!=null){
            et_dob.setText("${user.dob}")
        }else{
            et_dob.setText("")
        }

        val myCalendar: Calendar = Calendar.getInstance()

        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd-MM-YYYY" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            et_dob.setText(sdf.format(myCalendar.time))
        }

        et_dob.setOnClickListener {
            DatePickerDialog(
                this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        updateBtn.setOnClickListener {
            viewModel.updateProfile(UserPreferences.instance.getTokken(this)!!,user.id.toString(),et_first_name.text.toString(),et_last_name.text.toString(),et_phone.text.toString(),et_dob.text.toString())
        }
        addObserver()
    }

    private fun addObserver() {
        viewModel.updateState.observe(this, androidx.lifecycle.Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer

            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }

            hideProgress()

            when (state) {
                is NetworkState.Success -> {
                    Log.d("chaa", state.data!!.message)
                    onSuccess(state.data.message)
                    onUpdateSuccess(state.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun onUpdateSuccess(data: LoginModel) {

        UserPreferences.instance.saveUser(this,data.user!!)
    }
}
