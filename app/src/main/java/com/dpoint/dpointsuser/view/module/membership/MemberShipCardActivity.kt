package com.dpoint.dpointsuser.view.module.membership

import android.content.Intent
import androidx.lifecycle.Observer
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.getVM
import com.dpoint.dpointsuser.view.commons.base.BaseActivity
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpoint.dpointsuser.datasource.remote.shop.MenuModel
import com.dpoint.dpointsuser.view.adapter.MembershipAdapter
import com.dpoint.dpointsuser.utilities.OnItemClickListener
import com.dpoint.dpointsuser.view.commons.base.BaseFragment
import kotlinx.android.synthetic.main.activity_member_ship__cards.*

class MemberShipCardActivity : BaseFragment(), OnItemClickListener {
    override val layout: Int
        get() = R.layout.activity_member_ship__cards
    private val viewModel by lazy { getVM<MemberShipCardViewModel>(activity!!) }
    private var membership: MenuModel? = null
    override fun init(view : View) {
        addMembershipCard.setOnClickListener {
            var intent = Intent(activity!!, AddMembershipCardActivity::class.java)

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
                    viewModel.getMemberShipCard(UserPreferences.instance.getTokken(activity!!)!!)
                    Log.e("DATADELETE", state.data?.message.toString())

                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun setData(data: MenuModel) {
        recyclerView.layoutManager = LinearLayoutManager(activity!!)
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = MembershipAdapter(activity!!, this, data.data)


    }

    override fun onResume() {
        super.onResume()
        viewModel.getMemberShipCard(UserPreferences.instance.getTokken(activity!!)!!)
    }

    override fun onItemClick(index: Int, adapter: Int) {
        val memberships = membership!!.data!![index]
        viewModel.deleteMemberShipCard(
            UserPreferences.instance.getTokken(activity!!)!!,
            memberships.id.toString()
        )

    }

}
