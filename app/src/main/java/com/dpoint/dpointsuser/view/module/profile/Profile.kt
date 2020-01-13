package com.dpoints.view.module.profile

import android.content.Intent
import android.widget.Button
import android.widget.TextView
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.auth.User
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity

class Profile : BaseActivity() {
    override val layout: Int= R.layout.activity_profile

    override fun init() {
        findViewById<Button>(R.id.changePasswordProfile).setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        val user: User = UserPreferences.instance.getUser(this)!!
        if (user.last_name != null) {
             findViewById<TextView>(R.id.profileName).text = "${user!!.name} ${user!!.last_name}"
        } else {
             findViewById<TextView>(R.id.profileName).text = "${user!!.name}"
        }

        if (user.email != null) {
             findViewById<TextView>(R.id.profileEmail).text = "${user!!.email}"
        } else {
             findViewById<TextView>(R.id.profileEmail).text = "-"
        }

        if (user.points_used != null) {
             findViewById<TextView>(R.id.profileUsed).text = " ${user.points_used}"
        } else {
             findViewById<TextView>(R.id.profileUsed).text = "0"
        }

        if (user.total_points != null) {
             findViewById<TextView>(R.id.profileBal).text = " ${user.total_points}"
        } else {
             findViewById<TextView>(R.id.profileBal).text = "0"
        }

        if (user.dob != null) {
             findViewById<TextView>(R.id.profileDob).text = user.dob.toString()
        } else {
             findViewById<TextView>(R.id.profileDob).text = "-"
        }

        if (user.contact_number != null) {
             findViewById<TextView>(R.id.profileNumber).text = user.contact_number.toString()
        } else {
             findViewById<TextView>(R.id.profileNumber).text = "-"
        }
    }
}