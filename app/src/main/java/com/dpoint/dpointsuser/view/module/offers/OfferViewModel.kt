package com.dpoint.dpointsuser.view.module.offers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferModel
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferService
import com.dpoints.dpointsmerchant.utilities.Event

class OfferViewModel : ViewModel() {
    private val TAG = OfferViewModel::class.qualifiedName
    private val _offersState = MutableLiveData<Event<NetworkState<OfferModel>>>()
    val offersState: LiveData<Event<NetworkState<OfferModel>>> get() = _offersState

    fun getOffers(token: String) {
        _offersState.value = Event(NetworkState.Loading())

        OfferService.instance.getOffers(token,
            object : ApiCallbackImpl<OfferModel>(_offersState) {
                override fun onSuccess(success: OfferModel?) {
                    Log.e(TAG, success?.message.toString())
                    _offersState.value = Event(NetworkState.Success(success))
                }

            })
    }
}