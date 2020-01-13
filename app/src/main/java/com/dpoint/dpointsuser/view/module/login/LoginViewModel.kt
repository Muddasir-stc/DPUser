package com.dpoints.dpointsmerchant.view.module.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.auth.AuthService
import com.dpoints.dpointsmerchant.datasource.remote.auth.CityModel
import com.dpoints.dpointsmerchant.datasource.remote.auth.LoginModel
import com.dpoints.dpointsmerchant.datasource.remote.dashboard.NotificationModel
import com.dpoints.dpointsmerchant.utilities.Event

class LoginViewModel : ViewModel() {

    private val _loginState = MutableLiveData<Event<NetworkState<LoginModel>>>()
    val loginState: LiveData<Event<NetworkState<LoginModel>>> get() = _loginState

    private val _registerState = MutableLiveData<Event<NetworkState<LoginModel>>>()
    val registerState: LiveData<Event<NetworkState<LoginModel>>> get() = _registerState

    private val _verifyState = MutableLiveData<Event<NetworkState<LoginModel>>>()
    val verifyState: LiveData<Event<NetworkState<LoginModel>>> get() = _verifyState

    private val _resendOtpState = MutableLiveData<Event<NetworkState<LoginModel>>>()
    val resendOtpState: LiveData<Event<NetworkState<LoginModel>>> get() = _resendOtpState

    private val _socialState = MutableLiveData<Event<NetworkState<LoginModel>>>()
    val socialState: LiveData<Event<NetworkState<LoginModel>>> get() = _socialState

    private val _citiesState = MutableLiveData<Event<NetworkState<List<CityModel>>>>()
    val citiesState: LiveData<Event<NetworkState<List<CityModel>>>> get() = _citiesState


    private val _forgotState = MutableLiveData<Event<NetworkState<LoginModel>>>()
    val forgotState: LiveData<Event<NetworkState<LoginModel>>> get() = _forgotState


    private val _resetState = MutableLiveData<Event<NetworkState<LoginModel>>>()
    val resetState: LiveData<Event<NetworkState<LoginModel>>> get() = _resetState


    private val _changeState = MutableLiveData<Event<NetworkState<LoginModel>>>()
    val changeState: LiveData<Event<NetworkState<LoginModel>>> get() = _changeState

    fun login(
        email: String,
        password: String,
        deviceId: String
    ) {

        _loginState.value = Event(NetworkState.Loading())

        AuthService.instance.login(
            email,
            password,
            deviceId,
            object : ApiCallbackImpl<LoginModel>(_loginState) {
                override fun onSuccess(success: LoginModel?) {
                    Log.e("Data",success?.message)
                    _loginState.value = Event(NetworkState.Success(success))
                }
            })
    }

    fun register(
        name: String,
        last_name: String,
        email: String,
        password: String,
        contact_number: String,
        dob: String,
        city: String,
        referralId: String
    ) {

        _registerState.value = Event(NetworkState.Loading())

        AuthService.instance.register(name,
            last_name,
            email,
            password,
            contact_number,
            dob,
            city,
            referralId,
            object : ApiCallbackImpl<LoginModel>(_registerState) {
                override fun onSuccess(success: LoginModel?) {
                    Log.e("Data",success?.message)
                    _registerState.value = Event(NetworkState.Success(success))
                }
            })
    }

    fun verify(
        email: String,
        deviceId: String,
        otp: String
    ) {

        _verifyState.value = Event(NetworkState.Loading())

        AuthService.instance.verify(email,
            deviceId,
            otp,
            object : ApiCallbackImpl<LoginModel>(_verifyState) {
                override fun onSuccess(success: LoginModel?) {
                    Log.e("Data",success?.message)
                    _verifyState.value = Event(NetworkState.Success(success))
                }
            })
    }

    fun resendOTP(
        email: String
    ) {

        _resendOtpState.value = Event(NetworkState.Loading())

        AuthService.instance.resendOTP(email,
            object : ApiCallbackImpl<LoginModel>(_resendOtpState) {
                override fun onSuccess(success: LoginModel?) {
                    Log.e("Data",success?.message)
                    _resendOtpState.value = Event(NetworkState.Success(success))
                }
            })
    }

    fun socialLogin(
        providerName: String,
        providerId: String,
        name: String,
        email: String,
        deviceId: String
    ) {

        _socialState.value = Event(NetworkState.Loading())

        AuthService.instance.socialLogin(providerName,
            providerId,
            name,
            email,
            deviceId,
            object : ApiCallbackImpl<LoginModel>(_socialState) {
                override fun onSuccess(success: LoginModel?) {
                    Log.e("Data",success?.message)
                    _socialState.value = Event(NetworkState.Success(success))
                }
            })
    }

    fun forgotPassword(
        email: String
    ) {

        _forgotState.value = Event(NetworkState.Loading())

        AuthService.instance.forgotPassword(
            email,
            object : ApiCallbackImpl<LoginModel>(_forgotState) {
                override fun onSuccess(success: LoginModel?) {
                    Log.e("Data",success?.message)
                    _forgotState.value = Event(NetworkState.Success(success))
                }
            })
    }

    fun resetPassword(
        email: String,
        password: String,
        otp: String
    ) {

        _resetState.value = Event(NetworkState.Loading())

        AuthService.instance.resetPassword(
            email,
            password,
            otp,
            object : ApiCallbackImpl<LoginModel>(_resetState) {
                override fun onSuccess(success: LoginModel?) {
                    Log.e("Data",success?.message)
                    _resetState.value = Event(NetworkState.Success(success))
                }
            })
    }


    fun changePassword(
        token: String,
        oldPassword: String,
        newPassword: String
    ) {
        Log.e("Data","Token = $token Old= $oldPassword new = $newPassword")
        _changeState.value = Event(NetworkState.Loading())

        AuthService.instance.changePassword(
            token,
            oldPassword,
            newPassword,
            object : ApiCallbackImpl<LoginModel>(_changeState) {
                override fun onFailure(t: Throwable, code: Int) {
                    super.onFailure(t, code)
                    t.printStackTrace()
                }
                override fun onSuccess(success: LoginModel?) {
                    Log.e("Data","Hello")
                    _changeState.value = Event(NetworkState.Success(success))
                }
            })
    }


    fun getCities() {

        _citiesState.value = Event(NetworkState.Loading())

        AuthService.instance.getCities(object : ApiCallbackImpl<List<CityModel>>(_citiesState) {
                override fun onSuccess(success: List<CityModel>?) {
                    _citiesState.value = Event(NetworkState.Success(success))
                }
            })
    }

}