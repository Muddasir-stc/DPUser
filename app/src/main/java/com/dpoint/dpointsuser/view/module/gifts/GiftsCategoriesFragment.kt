package com.dpoint.dpointsuser.view.module.gifts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.model.GiftCardCategory
import com.dpoint.dpointsuser.datasource.remote.gift.Data
import com.dpoint.dpointsuser.datasource.remote.gift.GiftModel
import com.dpoint.dpointsuser.utilities.OnRemoveClickListener
import com.dpoint.dpointsuser.view.adapter.GiftCardCategoriesAdapter
import com.dpoint.dpointsuser.view.module.history.GiftHistoryActivity
import com.dpoint.dpointsuser.view.module.history.HistoryViewModel
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.utilities.toJson
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import kotlinx.android.synthetic.main.activity_offers.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [GiftsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GiftsCategoriesFragment : BaseFragment(), OnRemoveClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override val layout: Int
        get() = R.layout.fragment_gifts
    private val viewModel by lazy { getVM<GiftCardsViewModel>(activity!!) }
    private val viewModelHistory by lazy { getVM<HistoryViewModel>(activity!!) }
    private var giftModel: GiftModel? = null
    override fun init(view: View) {
        if (param1.equals("AllGifts"))
            viewModel.getGiftCards(UserPreferences.instance.getTokken(activity!!)!!)
        else if (param1.equals("history"))
            viewModelHistory.getAllUserUsedGiftCards(UserPreferences.instance.getTokken(context!!)!!)
        addObserver()
    }

    private fun addObserver() {
        viewModel.giftCardState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
//             hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    giftModel = state?.data
                    setupRecyclerView(state?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
        viewModelHistory.historyGiftState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.data.toJson())
                    setupRecyclerView(state?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private var hashMap: HashMap<String, ArrayList<Data>>? = null
    private fun setupRecyclerView(giftModel: GiftModel?) {
        hashMap = HashMap<String, ArrayList<Data>>()
        for (giftdata in giftModel!!.data) {
            if (hashMap!!.containsKey(giftdata.shop_category_name)) {
                var list = hashMap!![giftdata.shop_category_name]
                list!!.add(giftdata)
                hashMap!![giftdata.shop_category_name!!] = list
            } else {
                var list = ArrayList<Data>()
                list.add(giftdata)
                hashMap!![giftdata.shop_category_name!!] = list
            }
        }

        var arrayList = ArrayList<GiftCardCategory>()
        for ((key, value) in hashMap!!) {
            arrayList.add(GiftCardCategory(key, value))
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = GiftCardCategoriesAdapter(context!!, this, arrayList)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GiftsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            GiftsCategoriesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onRemoveClick(position: Int, itemId: String, shopName: String?) {
        if (param1.equals("AllGifts")) {
            var intent = Intent(activity, GiftCardListActivity::class.java)
            intent.putExtra("data", hashMap!![itemId])
            intent.putExtra("shopName",shopName)
            activity!!.startActivity(intent)
        } else if (param1.equals("history")) {
            var intent = Intent(activity, GiftHistoryActivity::class.java)
            intent.putExtra("data", hashMap!![itemId])
            intent.putExtra("shopName",shopName)
            activity!!.startActivity(intent)
        }
    }
}