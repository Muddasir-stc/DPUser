package com.dpoint.dpointsuser.view.module.membership

import android.content.Intent
import android.widget.ImageView
import androidx.lifecycle.Observer
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpoint.dpointsuser.datasource.remote.shop.Menu
import com.dpoint.dpointsuser.datasource.remote.shop.MenuModel
import com.dpoint.dpointsuser.view.adapter.MembershipAdapter
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener
import kotlinx.android.synthetic.main.activity_member_ship__cards.*

class MemberShipCardActivity : BaseActivity(), OnItemClickListener {
    override val layout: Int
        get() = R.layout.activity_member_ship__cards
    private val viewModel by lazy { getVM<MemberShipCardViewModel>(this) }
    private var membership: MenuModel? = null
    override fun init() {
        var backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }
        addMembershipCard.setOnClickListener {
            var intent = Intent(this, AddMembershipCardActivity::class.java)

            //  intent.putExtra("data", id.toString())
            intent.putExtra("action", "add_shop")
            startActivity(intent)
        }
        addObserver()
    }

    private fun addObserver() {
        viewModel.getMemberShipCardState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    if (state.data!!.data.size > 0) {
                        recyclerView.visibility = View.VISIBLE
                        no_data.visibility = View.GONE
                        membership = state.data
                        setData(state.data!!)
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

        viewModel.deleteMemberShipCardState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    onSuccess(state.data!!.message)
                    viewModel.getMemberShipCard(UserPreferences.instance.getTokken(this)!!)
                    Log.e("DATADELETE", state.data?.message.toString())

                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun setData(data: MenuModel) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = MembershipAdapter(this, this, data.data)


    }

    override fun onResume() {
        super.onResume()
        viewModel.getMemberShipCard(UserPreferences.instance.getTokken(this)!!)
    }

    override fun onItemClick(index: Int, adapter: Int) {
        val memberships = membership!!.data!![index]
        viewModel.deleteMemberShipCard(
            UserPreferences.instance.getTokken(this)!!,
            memberships.id.toString()
        )

    }

}
