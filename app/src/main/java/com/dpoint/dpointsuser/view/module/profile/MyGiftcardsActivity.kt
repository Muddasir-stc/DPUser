package com.dpoint.dpointsuser.view.module.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.userdata.MyGiftModel
import com.dpoint.dpointsuser.view.module.gifts.RedeemGiftActivity
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.OnItemClickListener
import com.dpoint.dpointsuser.utilities.getVM
import com.dpoint.dpointsuser.view.commons.base.BaseActivity
import com.dpoints.view.adapter.MyGiftAdapter
import kotlinx.android.synthetic.main.activity_my_giftcards.*
import kotlinx.android.synthetic.main.activity_my_giftcards.myGifts
import kotlinx.android.synthetic.main.fragment_profile.*

class MyGiftcardsActivity : BaseActivity(), OnItemClickListener {
     var myGiftList: MyGiftModel?=null
    override val layout: Int=R.layout.activity_my_giftcards
    private val viewModel by lazy { getVM<UserViewModel>(this) }
    override fun init() {
        myGifts.setHasFixedSize(true)
        myGifts.layoutManager=LinearLayoutManager(this)
        myGiftList=intent.getParcelableExtra("MYGIFTS")
        var backBtn=findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }
        if(myGiftList!=null){
            myGifts.adapter=MyGiftAdapter(this,this,myGiftList!!.data)
           // Toast.makeText(this,"${myGiftList!!.data.size}",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"Sorry Try Again!",Toast.LENGTH_LONG).show()
        }
        viewModel.getMyGiftCards(UserPreferences.instance.getTokken(this)!!)
addObserver()
    }

    private fun addObserver() {
        viewModel.myGiftState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    myGifts.adapter=MyGiftAdapter(this,this,state.data!!.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    override fun onItemClick(index: Int, adapter: Int) {
       //    Toast.makeText(this,myGiftList!!.data.get(index).unit,Toast.LENGTH_LONG).show()
        val intent= Intent(this, RedeemGiftActivity::class.java)
        intent.putExtra("GIFT",myGiftList!!.data.get(index))
        startActivity(intent)
    }
}
