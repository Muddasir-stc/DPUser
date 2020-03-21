package com.dpoints.view.module.dashboard

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.transaction.UsedOffer
import com.dpoint.dpointsuser.datasource.remote.transaction.UsedOfferModel
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.transaction.Tran
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import com.dpoints.dpointsmerchant.view.module.transaction.TransactionViewModel
import com.dpoints.view.adapter.TransactionsAdapter
import kotlinx.android.synthetic.main.fragment_offer_history.*


class OfferFragment : BaseFragment() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    override val layout: Int = R.layout.fragment_offer_history
    private val viewModel by lazy { getVM<TransactionViewModel>(activity!!) }
    lateinit var recyclerView: RecyclerView
    override fun init(view: View) {
        recyclerView = view.findViewById<RecyclerView>(R.id.transaction_recyclerview)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        viewModel.getTransactions(UserPreferences.instance.getTokken(context!!)!!)

        addObserver()
    }

    private fun addObserver() {
        viewModel.transState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            // hideProgress()
            when (state) {
                is NetworkState.Success -> {

                    if (state.data!!.data.isNullOrEmpty()) {
                        textView_noDataFound.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE

                    } else {
                        textView_noDataFound.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        setupTransactions(state?.data?.data)
                    }
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun setupTransactions(data: List<UsedOffer>?) {
        Log.e("DATTA", data?.size.toString())
        recyclerView.adapter = TransactionsAdapter(data!!,context!!)
    }
}