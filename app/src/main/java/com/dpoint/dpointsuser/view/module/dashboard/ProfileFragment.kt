package com.dpoints.view.module.dashboard

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.view.module.gifts.QrViewModel
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
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : BaseFragment() {

    lateinit var myGifts: TextView
    lateinit var myProfile: CircleImageView
    lateinit var myName: TextView
    lateinit var myEmail: TextView
    lateinit var btnRefresh: TextView
    lateinit var myNumber: TextView
    lateinit var profileBal: TextView
    lateinit var goToTrans: RelativeLayout
    lateinit var editProfile: RelativeLayout
    lateinit var giftsLayout: RelativeLayout


    override val layout: Int = R.layout.fragment_profile
    private val viewModel by lazy { getVM<UserViewModel>(activity!!) }
    private val viewQRModel by lazy { getVM<QrViewModel>(activity!!) }

    override fun init(view: View) {
        myProfile = view.findViewById<CircleImageView>(R.id.myProfile)
        myName = view.findViewById<TextView>(R.id.myName)
        myEmail = view.findViewById<TextView>(R.id.myEmail)
        btnRefresh = view.findViewById<TextView>(R.id.btnRefresh)
        myGifts = view.findViewById(R.id.myGifts)
        profileBal = view.findViewById<TextView>(R.id.profileBal)
        goToTrans = view.findViewById<RelativeLayout>(R.id.goToTrans)
        editProfile = view.findViewById<RelativeLayout>(R.id.editProfile)
        giftsLayout = view.findViewById<RelativeLayout>(R.id.giftsLayout)
        myNumber = view.findViewById<TextView>(R.id.myNumber)
        view.findViewById<Button>(R.id.changePasswordProfile).setOnClickListener {
            context!!.startActivity(Intent(context, ChangePasswordActivity::class.java))
        }


        goToTrans.setOnClickListener {
            context!!.startActivity(Intent(context, HistoryActivity::class.java))
        }
        editProfile.setOnClickListener {
            context!!.startActivity(Intent(context, UpdateProfileActivity::class.java))
        }
        btnRefresh.setOnClickListener {
            loadData()
        }
        loadData()
        viewModel.getMyGiftCards(UserPreferences.instance.getTokken(context!!)!!)
        viewModel.getUser(UserPreferences.instance.getTokken(context!!)!!)
        imageView_qrCodeScanner.setOnClickListener {
            var user = UserPreferences.instance.getUser(context!!)
            val data =
                "{\"user_id\":\"${user?.id}\",\"name\":\"${user?.name}\"}"
            viewQRModel.getQrImage(data)
        }
        addObserver()
    }

    private fun addObserver() {

        viewQRModel.qrState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }

            when (state) {
                is NetworkState.Success -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
                    val dialogView: View =
                        LayoutInflater.from(context!!)
                            .inflate(R.layout.layout_qr_code, null, false)
                    builder.setView(dialogView)
                    var imageView = dialogView.findViewById<ImageView>(R.id.imageView_qrCode)

                    val alertDialog: AlertDialog = builder.create()
                    imageView.setImageBitmap(state.data)
                    alertDialog.show()
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })

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
                    giftsLayout.setOnClickListener {
                        val intent = Intent(context, MyGiftcardsActivity::class.java)
                        intent.putExtra("MYGIFTS", state.data)
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
                    UserPreferences.instance.saveUser(context!!, state.data?.user!!)
                    loadData()
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun loadData() {
        val user = UserPreferences.instance.getUser(context!!)!!
        Glide.with(context!!).load(user.profile_picture).placeholder(R.drawable.profile)
            .into(myProfile)

        var username = user.name
        if (user.last_name != null) {
            username += " ${user.last_name}"
        }

        myName.setText("$username")

        myEmail.setText(user.email)
        if (user.contact_number != null) {
            myNumber.setText("${user.contact_number}")
        } else {
            myNumber.setText("NA")
        }
        if (user.total_points != null) {
            profileBal.setText(user.total_points)
        } else {
            profileBal.setText("0")
        }


    }


}