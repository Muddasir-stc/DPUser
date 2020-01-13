package com.dpoints.dpointsmerchant.view.module.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferModel
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferService
import com.dpoints.dpointsmerchant.datasource.remote.shop.ShopModel
import com.dpoints.dpointsmerchant.datasource.remote.shop.ShopService
import com.dpoints.dpointsmerchant.utilities.Event


class DashboardViewModel : ViewModel() {

    private val _shopsState = MutableLiveData<Event<NetworkState<ShopModel>>>()
    val shopsState: LiveData<Event<NetworkState<ShopModel>>> get() = _shopsState

    private val _offersState = MutableLiveData<Event<NetworkState<OfferModel>>>()
    val offersState: LiveData<Event<NetworkState<OfferModel>>> get() = _offersState
//    private val _addShopState = MutableLiveData<Event<NetworkState<ShopModel>>>()
//    val addShopState: LiveData<Event<NetworkState<ShopModel>>> get() = _addShopState
//
//    private val _updateShopState = MutableLiveData<Event<NetworkState<ShopModel>>>()
//    val updateShopState: LiveData<Event<NetworkState<ShopModel>>> get() = _updateShopState
//
//    private val _deleteShopState = MutableLiveData<Event<NetworkState<ShopModel>>>()
//    val deleteShopState: LiveData<Event<NetworkState<ShopModel>>> get() = _deleteShopState

    fun getShops(token: String) {

        _shopsState.value = Event(NetworkState.Loading())

        ShopService.instance.getShops(token,
            object : ApiCallbackImpl<ShopModel>(_shopsState) {
                override fun onSuccess(success: ShopModel?) {
                    Log.e("Data",success?.message)
                    _shopsState.value = Event(NetworkState.Success(success))
                }
            })
    }
    fun getOffers(token: String) {
        _offersState.value = Event(NetworkState.Loading())

        OfferService.instance.getOffers(token,
            object : ApiCallbackImpl<OfferModel>(_offersState) {
                override fun onSuccess(success: OfferModel?) {
                    Log.e("TAG", success?.message.toString())
                    _offersState.value = Event(NetworkState.Success(success))
                }

            })
    }
//    fun addShop(token: String,merchent_id:String,title: String,desc:String,image:String,ext:String,address:String,contact:String,email:String) {
//
//        _addShopState.value = Event(NetworkState.Loading())
//
//        ShopService.instance.addShop(token,merchent_id,title,desc,image,ext,address,contact,email,
//            object : ApiCallbackImpl<ShopModel>(_addShopState) {
//                override fun onSuccess(success: ShopModel?) {
//                    Log.e("Message",success?.message)
//                    _addShopState.value = Event(NetworkState.Success(success))
//                }
//            })
//    }
//    fun updateShop(token: String,merchent_id:String,title: String,desc:String,image:String,ext:String,address:String,contact:String,email:String,id:String) {
//
//        _updateShopState.value = Event(NetworkState.Loading())
//
//        ShopService.instance.updateShop(token,merchent_id,title,desc,image,ext,address,contact,email,id,
//            object : ApiCallbackImpl<ShopModel>(_updateShopState) {
//                override fun onSuccess(success: ShopModel?) {
//                    Log.e("Message",success?.message)
//                    _updateShopState.value = Event(NetworkState.Success(success))
//                }
//            })
//    }
//    fun deleteShop(token: String,id:String) {
//
//        _deleteShopState.value = Event(NetworkState.Loading())
//
//        ShopService.instance.deleteShop(token,id,
//            object : ApiCallbackImpl<ShopModel>(_deleteShopState) {
//                override fun onSuccess(success: ShopModel?) {
//                    Log.e("Data",success?.message)
//                    _deleteShopState.value = Event(NetworkState.Success(success))
//                }
//            })
//    }
}