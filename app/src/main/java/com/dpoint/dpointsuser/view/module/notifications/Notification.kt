package com.dpoints.view.module.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.view.adapter.NotificationAdapter
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.datasource.remote.dashboard.Data
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.getVM
import com.dpoint.dpointsuser.view.commons.base.BaseFragment
import com.dpoint.dpointsuser.view.module.dashboard.DashboardViewModel
import java.util.ArrayList

class Notification : BaseFragment() {
    override val layout: Int= R.layout.fragment_notifications
    lateinit var notificationList: RecyclerView
    private val viewModel by lazy { getVM<DashboardViewModel>(activity!!) }


    override fun init(view: View) {
        activity?.title="Notifications"
        notificationList=view.findViewById<RecyclerView>(R.id.notification_list)

        viewModel.getNotification(UserPreferences.instance.getTokken(context!!)!!)
        addObserver()


        notificationList.setHasFixedSize(true)
        notificationList.layoutManager=LinearLayoutManager(context)
    }

    private fun addObserver() {
        viewModel.notificationState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            // hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA",state.data?.message)
                    setupNotifications(state?.data?.data)
                }
//                is NetworkState.Error -> onError(state.message)
//                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
//                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun setupNotifications(data: List<Data>?) {
        var adapter:NotificationAdapter?=null
       if(data!!.size>0){
           adapter= NotificationAdapter(context, data!!.reversed() as ArrayList<Data>?)
       }else{
           adapter= NotificationAdapter(context, data!! as ArrayList<Data>?)
       }
        notificationList.adapter=adapter
    }
}
