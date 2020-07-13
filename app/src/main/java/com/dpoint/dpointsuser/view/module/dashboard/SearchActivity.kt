package com.dpoint.dpointsuser.view.module.dashboard

import com.dpoint.dpointsuser.R
import kotlinx.android.synthetic.main.activity_search.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.dpointsmerchant.view.module.shops.ShopViewModel
import com.dpoints.view.adapter.ShopAdapter


class SearchActivity : BaseActivity() {


    private var list: List<Shop>? = null
    override val layout: Int=R.layout.activity_search
    private val viewModel by lazy { getVM<ShopViewModel>(this) }
    override fun init() {
        searchView.requestFocus()
        searchList.setHasFixedSize(true)
        searchList.layoutManager=LinearLayoutManager(this)
        searchView.addTextChangedListener(object : TextWatcher {

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
                viewModel.getSearchedShops(UserPreferences.instance.getTokken(this@SearchActivity)!!,str)
            }
        })
        addObserver()
    }

    private fun addObserver() {
        viewModel.searchedShopsState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                progressBar2.visibility= View.VISIBLE
                return@Observer
            }
            progressBar2.visibility= View.GONE
            Log.e("DATA",state.toString())
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA",state.data?.message)
                    list=state?.data?.data!!
                    setupShops(state?.data?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun setupShops(data: List<Shop>) {
        if(data.size>0){
            noData.visibility=View.GONE
        }else{
            noData.visibility=View.VISIBLE
        }
       var adapter = ShopAdapter(data!!,this,1)
        searchList.adapter = adapter
    }
  //  override fun onItemClick(index: Int, adapter: Int) {
//        Log.e("SHOPPER",list?.get(index).toJson())
//        val intent= Intent(this@SearchActivity, ShopDetailActivity::class.java)
//        var dt=list?.get(index)
//        dt!!.category_name = "abc"
//        intent.putExtra("SHOP", dt!!)
//        startActivity(intent)
//    }
}
