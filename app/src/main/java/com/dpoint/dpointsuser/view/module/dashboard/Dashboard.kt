package com.dpoints.view.module.dashboard

import android.content.Intent
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.model.ScanedOffer
import com.dpoint.dpointsuser.view.adapter.NavigationAdapter
import com.dpoints.dpointsmerchant.datasource.model.Item
import com.dpoints.dpointsmerchant.datasource.remote.auth.User
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.utilities.fromJson
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard.*

class Dashboard : BaseActivity(), OnItemClickListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var rvDrawer: RecyclerView
    private var DRAWER_NAV = 1
    private lateinit var linearLayout: LinearLayout


    override val layout: Int = R.layout.activity_dashboard

    override fun init() {
        rvDrawer = findViewById(R.id.drawerRecyclerView)
        rvDrawer.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvDrawer.adapter = NavigationAdapter(getDrawerItems(), this)
        drawer = findViewById(R.id.nav_drawer)
        linearLayout = findViewById(R.id.drawer_linearlayout)
        showProgress(this)
        ic_ham.setOnClickListener {
            if (drawer.isDrawerOpen(linearLayout))
                drawer.closeDrawers()
            else
                drawer.openDrawer(linearLayout)
        }
        iv_drawer_close.setOnClickListener {
            drawer.closeDrawers()
        }

        val user: User = UserPreferences.instance.getUser(this)!!

        user_email.text = user.email


        if (user.last_name != null) {
            user_name.text = "${user!!.name} ${user!!.last_name}"
        } else {
            user_name.text = "${user!!.name}"
        }

//
//        bottom_navigation.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.navigation_home -> {
//                    titleBarName.text = "Dashboard"
//                    applayChanages(Home())
//                }
//                R.id.navigation_notifications -> {
//                    titleBarName.text = "Notifications"
//                    applayChanages(Notification())
//                }
//                R.id.navigation_setting -> {
//                    true
//                }
//                R.id.navigation_profile -> {
//                    titleBarName.text = "Profile"
//                    applayChanages(Profile())
//                }
//            }
//            false
//        }
        applayChanages(Home())
        hideProgress()

    }

    private fun applayChanages(fr: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.container, fr).commit()
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
//        if (result != null) {
//            if (result.contents == null) {
//             //   Log.d("RESULT", "Cancelled scan")
//            } else {
//                Log.d("RESULT", result.contents)
//               // val offer=result.contents.toString().fromJson<ScanedOffer>()
//                Home().onActivityResult(requestCode,resultCode,data)
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data)
//        }
//    }
    /*  override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
      return onNavigarionItemSelected(menuItem)
      *//*when (menuItem.itemId) {
            R.id.home_menu -> {
                startActivity<DashboardActivity>()

                return true
            }
            R.id.orders_menu -> {
                startActivity<OrdersActivity>()
                return true
            }
            R.id.chat_menu -> {
                startActivity<ChatUserActivity>()
                return true
            }
            R.id.inbox_menu -> {
                Toast.makeText(this,"friends",Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.account_menu -> {
                startActivity<ProfileActivity>()

                return true
                startActivity<ProfileActivity>()
            }
        }
        return false*//*
    }
*/

    private fun getDrawerItems() = listOf(
        Item("My Orders", R.drawable.ic_box),
        Item("My Offers", R.drawable.ic_tag),
        Item("My Gift Cards", R.drawable.ic_giftcard),
        Item("About Dpoints", R.drawable.ic_question),
        Item("Logout", R.drawable.ic_logout)

        // Item("Refer", R.drawable.ic_user)
    )

    override fun onItemClick(index: Int, adapter: Int) {
        if (adapter == DRAWER_NAV) {
            when (getDrawerItems()[index].name) {
//                "About Dpoints" -> {
//                    drawer.closeDrawers()
//                    startActivity(
//                        Intent(
//                            this,
//                            AboutActivity::class.java
//                        )
//                    )}
//                "My Offers" -> {
//                    drawer.closeDrawers()
//                    startActivity(
//                        Intent(
//                            this,
//                            Offers::class.java
//                        )
//                    )}
//                "My Orders" -> {
//                    drawer.closeDrawers()
//                    startActivity(
//                        Intent(
//                            this,
//                            Order::class.java
//                        )
//                    )}
//                "My Gift Cards" -> {
//                    drawer.closeDrawers()
//                    startActivity(
//                        Intent(
//                            this,
//                            Gifts::class.java
//                        )
//                    )}
                "Logout" -> logout()

                //Toast.makeText(this, get.get(index).name, Toast.LENGTH_LONG).show()
            }

        }


    }
}
