package com.dpoint.dpointsuser.view.module.shops

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoint.dpointsuser.datasource.remote.gift.GiftModel
import com.dpoint.dpointsuser.datasource.remote.offer.AssignModel
import com.dpoint.dpointsuser.datasource.remote.shop.MenuModel
import com.dpoint.dpointsuser.datasource.remote.shop.ShopModel
import com.dpoint.dpointsuser.datasource.remote.ApiCallbackImpl
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.datasource.remote.offer.OfferModel
import com.dpoint.dpointsuser.datasource.remote.shop.ShopService
import com.dpoint.dpointsuser.utilities.Event
import com.dpoint.dpointsuser.utilities.toJson

class ShopViewModel : ViewModel() {

    private val _shopsState = MutableLiveData<Event<NetworkState<ShopModel>>>()
    val shopsState: LiveData<Event<NetworkState<ShopModel>>> get() = _shopsState

    private val _searchedShopsState = MutableLiveData<Event<NetworkState<ShopModel>>>()
    val searchedShopsState: LiveData<Event<NetworkState<ShopModel>>> get() = _searchedShopsState

    private val _offersState = MutableLiveData<Event<NetworkState<OfferModel>>>()
    val offersState: LiveData<Event<NetworkState<OfferModel>>> get() = _offersState

    private val _giftsState = MutableLiveData<Event<NetworkState<GiftModel>>>()
    val giftsState: LiveData<Event<NetworkState<GiftModel>>> get() = _giftsState

    private val _menuState = MutableLiveData<Event<NetworkState<MenuModel>>>()
    val menuState: LiveData<Event<NetworkState<MenuModel>>> get() = _menuState

    private val _ratingState = MutableLiveData<Event<NetworkState<AssignModel>>>()
    val ratingState: LiveData<Event<NetworkState<AssignModel>>> get() = _ratingState

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
    fun submitShopRating(token: String, userId: String, rating: String, shopId: String, feedback: String) {
        _ratingState.value = Event(NetworkState.Loading())

        ShopService.instance.submitShopRating(token,userId,shopId,rating,feedback,
            object : ApiCallbackImpl<AssignModel>(_ratingState) {
                override fun onSuccess(success: AssignModel?) {
                    Log.e("RATING",success?.message)
                    _ratingState.value = Event(NetworkState.Success(success))
                }
            })
    }

    fun getShopMenus(token: String,id:String) {
        _menuState.value = Event(NetworkState.Loading())

        ShopService.instance.getShopMenus(token,id,
            object : ApiCallbackImpl<MenuModel>(_menuState) {
                override fun onSuccess(success: MenuModel?) {
                    Log.e("RESPONSE HERE",success?.data.toJson())
                    _menuState.value = Event(NetworkState.Success(success))
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


    private val _shopsWithOfferState = MutableLiveData<Event<NetworkState<ShopModel>>>()
    val shopsWithOfferState: LiveData<Event<NetworkState<ShopModel>>> get() = _shopsWithOfferState
    fun getShopsWithOffers(token: String) {

        _shopsWithOfferState.value = Event(NetworkState.Loading())

        ShopService.instance.getShopsWithOffers(token,
            object : ApiCallbackImpl<ShopModel>(_shopsWithOfferState) {
                override fun onSuccess(success: ShopModel?) {
                    Log.e("Shop",success?.message)
                    _shopsWithOfferState.value = Event(NetworkState.Success(success))
                }
            })
    }


    private val _allMerchantOfferState = MutableLiveData<Event<NetworkState<OfferModel>>>()
    val allMerchantOfferState: LiveData<Event<NetworkState<OfferModel>>> get() = _allMerchantOfferState
    fun getAllMerchantOffers(token: String) {

        _allMerchantOfferState.value = Event(NetworkState.Loading())

        ShopService.instance.getAllMerchantOffers(token,
            object : ApiCallbackImpl<OfferModel>(_allMerchantOfferState) {
                override fun onSuccess(success: OfferModel?) {
                    Log.e("Shop",success?.message)
                    _allMerchantOfferState.value = Event(NetworkState.Success(success))
                }
            })
    }


    fun getSearchedShops(token: String,name:String) {

        _searchedShopsState.value = Event(NetworkState.Loading())

        ShopService.instance.getSearchedShops(token,name,
            object : ApiCallbackImpl<ShopModel>(_searchedShopsState) {
                override fun onSuccess(success: ShopModel?) {
                    Log.e("Shop",success?.message)
                    _searchedShopsState.value = Event(NetworkState.Success(success))
                }
            })
    }
}