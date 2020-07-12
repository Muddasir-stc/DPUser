package com.dpoint.dpointsuser.view.module.dashboard

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.userdata.MyGiftModel
import com.dpoint.dpointsuser.view.module.gifts.RedeemGiftActivity
import com.dpoint.dpointsuser.view.module.profile.UserViewModel
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.OnItemClickListener
import com.dpoint.dpointsuser.utilities.getVM
import com.dpoint.dpointsuser.view.commons.base.BaseFragment
import com.dpoints.view.adapter.MyGiftAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_member_ship__cards.*


//
// Created by Admin on 3/17/2020.
// Copyright (c) {2020} EMatrix All rights reserved.
//

class MyGiftCardFragment : BaseFragment(), OnItemClickListener {
    var myGiftList: MyGiftModel? = null
    override val layout: Int = R.layout.activity_member_ship__cards
    private val viewModel by lazy { getVM<UserViewModel>(activity!!) }
    private var addCard: FloatingActionButton? = null
    override fun init(view: View) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity!!)
        addCard = view.findViewById(R.id.addMembershipCard)
        addCard!!.visibility = View.GONE
        viewModel.getMyGiftCards(UserPreferences.instance.getTokken(activity!!)!!)
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
                    if (state.data!!.data.isNotEmpty()) {
                        myGiftList = state.data
                        recyclerView.visibility = View.VISIBLE
                        no_data.visibility = View.GONE
                        recyclerView.adapter = MyGiftAdapter(activity!!, this, state.data!!.data)
                    } else {
                        Log.e("DATAMEMBERSHIP", state.data?.message.toString())
                        recyclerView.visibility = View.GONE
                        no_data.visibility = View.VISIBLE
                    }

                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    override fun onItemClick(index: Int, adapter: Int) {
        //    Toast.makeText(this,myGiftList!!.data.get(index).unit,Toast.LENGTH_LONG).show()
        val intent = Intent(activity!!, RedeemGiftActivity::class.java)
        intent.putExtra("GIFT", myGiftList!!.data.get(index))
        context!!.startActivity(intent)
    }
}
