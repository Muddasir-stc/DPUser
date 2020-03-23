package com.dpoint.dpointsuser.view.module.gifts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoint.dpointsuser.datasource.remote.userdata.MyGift
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import kotlinx.android.synthetic.main.activity_exchange.*
import kotlinx.android.synthetic.main.activity_redeem_gift.*
import kotlinx.android.synthetic.main.activity_redeem_gift.btnGenerate
import kotlinx.android.synthetic.main.activity_redeem_gift.qr_image
import kotlinx.android.synthetic.main.activity_redeem_gift.tv_value

class   RedeemGiftActivity : BaseActivity() {
    private var units: Int=0
    private var gift: MyGift?=null
    override val layout: Int=R.layout.activity_redeem_gift
    private val viewQRModel by lazy { getVM<QrViewModel>(this) }
    override fun init() {

        if (intent.getParcelableExtra<MyGift>("GIFT") != null) {
            gift = intent.getParcelableExtra<MyGift>("GIFT")
            Log.e("GIFTER", gift!!.title)
        }
        val btnBack = findViewById<ImageView>(R.id.backBtn)
        btnBack.setOnClickListener {
            onBackPressed()
        }
        txtTitle.text=gift!!.title
        unit.text="${gift!!.unit} Available"
        tv_value.text="${gift!!.number_of_units}"


        et_units.addTextChangedListener(object : TextWatcher {

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
                    units=Integer.parseInt(str)

                }catch (e:Exception){
                    et_units.setText("0")
                }

            }
        })
        btnGenerate.setOnClickListener {
            if(units>gift!!.number_of_units){
               onError("Please enter unit less than available unit")
            }
           else if(units>0){
               val data="{\"type\":\"redeem\",\"gift_card_id\":\"${gift?.gift_card_id}\",\"user_id\":\"${UserPreferences.instance.getUser(this)!!.id}\",\"shop_id\":\"${gift?.shop_id}\",\"user_gift_card_id\":\"${gift?.id}\",\"user_gift_card_title\":\"${gift?.title}\",\"amount\":\"${gift?.amount}\",\"number_of_units\":\"${units}\",\"unit\":\"${gift?.unit}\"}"
               viewQRModel.getQrImage(data)
           }else{
               onError("Number of units must be greater than 0")
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
