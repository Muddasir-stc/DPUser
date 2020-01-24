package com.dpoints.view.module.dashboard

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.view.module.history.HistoryActivity
import com.dpoint.dpointsuser.view.module.profile.MyGiftcardsActivity
import com.dpoint.dpointsuser.view.module.profile.UpdateProfileActivity
import com.dpoint.dpointsuser.view.module.profile.UserViewModel
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.utilities.toJson
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import com.dpoints.view.module.profile.ChangePasswordActivity
import com.dpoints.view.module.transaction.Transaction
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.content_dashboard.*


class ProfileFragment : BaseFragment(){

    lateinit var myGifts: TextView
    lateinit var myProfile: CircleImageView
    lateinit var myName: TextView
    lateinit var myEmail: TextView
    lateinit var myNumber: TextView
    lateinit var profileBal: TextView
    lateinit var goToTrans: RelativeLayout
    lateinit var editProfile: RelativeLayout


    override val layout: Int=R.layout.fragment_profile
    private val viewModel by lazy { getVM<UserViewModel>(activity!!) }

    override fun init(view: View) {
         myProfile=view.findViewById<CircleImageView>(R.id.myProfile)
         myName=view.findViewById<TextView>(R.id.myName)
         myEmail=view.findViewById<TextView>(R.id.myEmail)
        myGifts=view.findViewById(R.id.myGifts)
         profileBal=view.findViewById<TextView>(R.id.profileBal)
        goToTrans=view.findViewById<RelativeLayout>(R.id.goToTrans)
        editProfile=view.findViewById<RelativeLayout>(R.id.editProfile)
         myNumber=view.findViewById<TextView>(R.id.myNumber)
        view.findViewById<Button>(R.id.changePasswordProfile).setOnClickListener {
            context!!.startActivity(Intent(context, ChangePasswordActivity::class.java))
        }


        goToTrans.setOnClickListener {
            context!!.startActivity(Intent(context,HistoryActivity::class.java))
        }
        editProfile.setOnClickListener {
            context!!.startActivity(Intent(context,UpdateProfileActivity::class.java))
        }
            loadData()
        viewModel.getMyGiftCards(UserPreferences.instance.getTokken(context!!)!!)
        viewModel.getUser(UserPreferences.instance.getTokken(context!!)!!)
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
                    myGifts!!.setText(state.data!!.data.size.toString())
                    myGifts.setOnClickListener {
                        val intent=Intent(context,MyGiftcardsActivity::class.java)
                        intent.putExtra("MYGIFTS",state.data)
                        context!!.startActivity(intent)
                    }

                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })

        viewModel.userState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.user.toJson())
                    UserPreferences.instance.saveUser(context!!,state.data?.user!!)
                    loadData()
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun loadData() {
        val user=UserPreferences.instance.getUser(context!!)!!
        Glide.with(context!!).load(user.profile_picture).placeholder(R.drawable.profile).into(myProfile)

        var username=user.name
        if(user.last_name!=null){
            username+=" ${user.last_name}"
        }

        myName.setText("$username")

        myEmail.setText(user.email)
        if(user.contact_number!=null){
            myNumber.setText("${user.contact_number}")
        }else{
            myNumber.setText("NA")
        }
        if(user.total_points!=null){
            profileBal.setText(user.total_points)
        }else{
            profileBal.setText("0")
        }


    }


}