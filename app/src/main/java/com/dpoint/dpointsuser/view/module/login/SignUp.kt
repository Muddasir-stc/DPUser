package com.dpoints.view.module.signup


import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.Observer
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.dpointsmerchant.view.module.login.LoginViewModel
import com.dpoints.dpointsmerchant.view.module.login.VerifyActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.text.SimpleDateFormat
import java.util.*


class SignUp : BaseActivity() {

    private val viewModel by lazy { getVM<LoginViewModel>(this) }
    override val layout: Int = R.layout.activity_sign_up
    override fun init() {

        backBtn.setOnClickListener {
            onBackPressed()
        }

        signUpBtn.setOnClickListener {
            Log.e("HERE", "HERE")
            if (validateRegister()) {
                val name = et_first_name.text.toString()
                val last_name = et_last_name.text.toString()
                val email = et_email.text.toString()
                val password = et_password.text.toString()
                val contact_number = et_phone.text.toString()
                val dob = et_dob.text.toString()
                val city = sp_city.selectedItem.toString()
                val referralId = et_referral_id.text.toString()
                register(name, last_name, email, password, contact_number, dob, city, referralId)
                hideKeyboard()
            }
        }

        addObserver()
        val myCalendar: Calendar = Calendar.getInstance()

        val date = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd-MM-YYYY" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            et_dob.setText(sdf.format(myCalendar.time))
        }

        et_dob.setOnClickListener {
            DatePickerDialog(
                this@SignUp, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

    }



    private fun register(name: String, last_name: String, email: String, password: String, contact_number: String, dob: String, city: String, referralId: String) {
        if (validateRegister()) {
            Log.e("HERE", dob)
            viewModel.register(name, last_name, email, password, contact_number, dob, city, referralId)
        }


    }

    private fun validateRegister() = when {
        (et_first_name.text != null && TextUtils.isEmpty(et_first_name.text.toString().trim())) -> {
            et_first_name.error = "Please Enter First Name"
            et_first_name.requestFocus()
            false
        }
        (et_last_name.text != null && TextUtils.isEmpty(et_last_name.text.toString().trim())) -> {
            et_last_name.error = "Please Enter Last Name"
            et_last_name.requestFocus()
            false
        }
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
        (et_phone.text != null && TextUtils.isEmpty(et_phone.text.toString().trim())) -> {
            et_phone.error = "Please Enter Phone Number"
            et_phone.requestFocus()
            false
        }
        (et_phone.text.toString().length !in 8..15) -> {
            et_phone.error = "Invalid Phone"
            et_phone.requestFocus()
            false
        }
        (et_password.text != null && TextUtils.isEmpty(et_password.text.toString().trim())) -> {
            et_password.error = "Please Enter Password"
            et_password.requestFocus()
            false
        }
        (et_password.text.length !in 6..15)-> {
            et_password.error = "Password Length Must Be 8-15"
            et_password.requestFocus()
            false
        }
//        (et_dob.text != null && TextUtils.isEmpty(et_dob.text.toString().trim())) -> {
//            et_dob.error = "Please Select DOB"
//            et_dob.requestFocus()
//            false
//        }
//        (sp_city.selectedItem != null && TextUtils.isEmpty(sp_city.selectedItem.toString().trim())) -> {
//            Toast.makeText(this, "Please Select City", Toast.LENGTH_LONG).show()
//            false
//        }
        else -> {
            true
        }
    }

    private fun addObserver() {
        viewModel.registerState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer

            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }

            hideProgress()

            when (state) {
                is NetworkState.Success -> {
                    startActivity<VerifyActivity>(Bundle().apply { putString("email", et_email.text.toString()) })
                    finish()
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })

//        viewModel.citiesState.observe(this, Observer {
//            it ?: return@Observer
//            val state = it.getContentIfNotHandled() ?: return@Observer
//
//            if (state is NetworkState.Loading) {
//                return@Observer showProgress(this)
//            }
//
//            hideProgress()
//
//            when (state) {
//                is NetworkState.Success -> {
//                    setupSpinner(state.data!!)
//                }
//                is NetworkState.Error -> onError(state.message)
//                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
//                else -> onFailure(getString(R.string.connection_error))
//            }
//        })
    }

//    fun setupSpinner(cities: List<CityModel>) {
////    }



}
