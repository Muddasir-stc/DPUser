package com.dpoints.dpointsmerchant.view.module.shops

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoint.dpointsuser.datasource.remote.shop.ShopModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.gift.GiftModel
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferModel
import com.dpoints.dpointsmerchant.datasource.remote.shop.ShopService
import com.dpoints.dpointsmerchant.utilities.Event
import com.dpoints.dpointsmerchant.utilities.toJson

class ShopViewModel : ViewModel() {

    private val _shopsState = MutableLiveData<Event<NetworkState<ShopModel>>>()
    val shopsState: LiveData<Event<NetworkState<ShopModel>>> get() = _shopsState

    private val _offersState = MutableLiveData<Event<NetworkState<OfferModel>>>()
    val offersState: LiveData<Event<NetworkState<OfferModel>>> get() = _offersState

    private val _giftsState = MutableLiveData<Event<NetworkState<GiftModel>>>()
    val giftsState: LiveData<Event<NetworkState<GiftModel>>> get() = _giftsState

    fun getShopOffers(token: String,id:String) {
        _offersState.value = Event(NetworkState.Loading())

        ShopService.instance.getShopOffers(token,id,
            object : ApiCallbackImpl<OfferModel>(_offersState) {
                override fun onSuccess(success: OfferModel?) {
                    Log.e("RESPONSE HERE",success?.data.toJson())
                    _offersState.value = Event(NetworkState.Success(success))
                }

            })
    }

    fun getShopGifts(token: String,id:String) {
        _giftsState.value = Event(NetworkState.Loading())

        ShopService.instance.getShopGifts(token,id,
            object : ApiCallbackImpl<GiftModel>(_giftsState) {
                override fun onSuccess(success: GiftModel?) {
                    Log.e("TEST",success?.message)
                    _giftsState.value = Event(NetworkState.Success(success))
                }

            })
    }

    fun getShops(token: String) {

        _shopsState.value = Event(NetworkState.Loading())

        ShopService.instance.getShops(token,
            object : ApiCallbackImpl<ShopModel>(_shopsState) {
                override fun onSuccess(success: ShopModel?) {
                    Log.e("Shop",success?.message)
                    _shopsState.value = Event(NetworkState.Success(success))
                }
            })
    }
}