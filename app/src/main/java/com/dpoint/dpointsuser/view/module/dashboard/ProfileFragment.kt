package com.dpoints.view.module.dashboard

import android.content.Intent
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.view.module.profile.UpdateProfileActivity
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import com.dpoints.view.module.profile.ChangePasswordActivity
import com.dpoints.view.module.transaction.Transaction
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.content_dashboard.*


class ProfileFragment : BaseFragment(){

    override val layout: Int=R.layout.fragment_profile
    override fun init(view: View) {
        val myProfile=view.findViewById<CircleImageView>(R.id.myProfile)
        val myName=view.findViewById<TextView>(R.id.myName)
        val myEmail=view.findViewById<TextView>(R.id.myEmail)
        val profileUsed=view.findViewById<TextView>(R.id.profileUsed)
        val profileBal=view.findViewById<TextView>(R.id.profileBal)
        val goToTrans=view.findViewById<RelativeLayout>(R.id.goToTrans)
        val editProfile=view.findViewById<RelativeLayout>(R.id.editProfile)
        val myNumber=view.findViewById<TextView>(R.id.myNumber)
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
        if(user.points_used!=null){
            profileUsed.setText(user.points_used)
        }else{
            profileUsed.setText("0")
        }
        view.findViewById<Button>(R.id.changePasswordProfile).setOnClickListener {
            context!!.startActivity(Intent(context, ChangePasswordActivity::class.java))
        }


        goToTrans.setOnClickListener {
            context!!.startActivity(Intent(context,Transaction::class.java))
        }
        editProfile.setOnClickListener {
            context!!.startActivity(Intent(context,UpdateProfileActivity::class.java))
        }
    }




}