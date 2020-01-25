package com.dpoint.dpointsuser.view.module.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.userdata.MyGiftModel
import com.dpoint.dpointsuser.view.module.gifts.RedeemGiftActivity
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.view.adapter.MyGiftAdapter
import kotlinx.android.synthetic.main.activity_my_giftcards.*

class MyGiftcardsActivity : BaseActivity(), OnItemClickListener {
     var myGiftList: MyGiftModel?=null
    override val layout: Int=R.layout.activity_my_giftcards
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
    }

    override fun onItemClick(index: Int, adapter: Int) {
       //    Toast.makeText(this,myGiftList!!.data.get(index).unit,Toast.LENGTH_LONG).show()
        val intent= Intent(this, RedeemGiftActivity::class.java)
        intent.putExtra("GIFT",myGiftList!!.data.get(index))
        startActivity(intent)
    }
}
