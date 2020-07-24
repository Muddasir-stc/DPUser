package com.dpoints.view.module.transaction

import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.datasource.remote.transaction.Tran
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.OnItemClickListener
import com.dpoint.dpointsuser.utilities.getVM
import com.dpoint.dpointsuser.view.commons.base.BaseActivity
import com.dpoint.dpointsuser.view.module.transaction.TransactionViewModel
import com.dpoints.view.adapter.TransactionsAdapter
import kotlinx.android.synthetic.main.activity_transaction.*

class Transaction : BaseActivity(), OnItemClickListener {
    private lateinit var linearLayoutManager: LinearLayoutManager
    override val layout: Int= R.layout.activity_transaction
    private val viewModel by lazy { getVM<TransactionViewModel>(this) }
    lateinit var recyclerView:RecyclerView


    override fun init() {
        recyclerView = findViewById<RecyclerView>(R.id.transaction_recyclerview)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        backBtn.setOnClickListener {
            onBackPressed()
        }
    //    viewModel.getTransactions(UserPreferences.instance.getTokken(this)!!)

     //   addObserver()

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
                    Log.e("DATA",state.data?.message)
                 //   setupTransactions(state?.data?.data)
                }
//                is NetworkState.Error -> onError(state.message)
//                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
//                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    override fun onItemClick(index: Int, adapter: Int) {
//        Toast.makeText(this,"hello2",Toast.LENGTH_SHORT).show()
    }

//    private fun setupTransactions(data: List<Tran>?) {
//        val adapter = TransactionsAdapter(data!!,this)
//        recyclerView.adapter = adapter
//    }
}
