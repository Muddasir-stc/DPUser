package com.dpoint.dpointsuser.view.module.shops

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.Observer
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoint.dpointsuser.view.module.gifts.QrViewModel
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import kotlinx.android.synthetic.main.activity_exchange.*
import kotlinx.android.synthetic.main.activity_search.*
import java.lang.Exception

class ExchangeActivity : BaseActivity() {
    override val layout: Int=R.layout.activity_exchange
    lateinit var shop: Shop
    private val viewQRModel by lazy { getVM<QrViewModel>(this) }
    override fun init()  {
        if (intent.getParcelableExtra<Shop>("SHOP") != null) {
            shop = intent.getParcelableExtra<Shop>("SHOP")
            Log.e("SHOPPER", shop.shop_name)
        }

        tv_value.text="${shop.coin_value}"
        txtCoins.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {

            }


            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) {
                var str=s.toString()
               try {
                   var coins=Integer.parseInt(str)
                   txtTotal.setText((coins*shop.coin_value).toString())
               }catch (e:Exception){
                   txtTotal.setText("0")
               }

            }
        })
        btnGenerate.setOnClickListener {
           if(!txtTotal.text.equals("0")){
               val data="{\"type\":\"exchange\",\"merchant_id\":\"${shop?.merchant_id}\",\"user_id\":\"${UserPreferences.instance.getUser(this)!!.id}\",\"shop_id\":\"${shop?.id}\",\"coins\":\"${txtCoins.text.toString()}\",\"total\":\"${txtTotal.text.toString()}\",\"`\":\"${shop.coin_value}\"}"
                viewQRModel.getQrImage(data)
           }else{
               onError("Coins must be greater than 1")
           }
        }
        addObserver()
    }

    private fun addObserver() {
        viewQRModel.qrState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }
            hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    qr_image!!.setImageBitmap(state.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }
}
