package com.dpoints.view.module.dashboard

import android.content.Intent
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.view.adapter.NavigationAdapter
import com.dpoint.dpointsuser.view.module.history.HistoryActivity
import com.dpoints.dpointsmerchant.datasource.model.Item
import com.dpoints.dpointsmerchant.datasource.remote.auth.User
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.view.module.gifts.Gifts
import com.dpoints.view.module.notifications.Notification
import com.dpoints.view.module.offers.Offers
import com.dpoints.view.module.order.Order
import com.dpoints.view.module.profile.Profile
import com.dpoints.view.module.shops.Shops
import com.dpoints.view.module.transaction.Transaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard.*

class Dashboard : BaseActivity(), OnItemClickListener,BottomNavigationView.OnNavigationItemSelectedListener {


    private lateinit var drawer: DrawerLayout
    private lateinit var rvDrawer: RecyclerView
    private var DRAWER_NAV = 1
    private lateinit var linearLayout: LinearLayout
    private val home = Home()
    private val profile = ProfileFragment()
    private val scanner = ScannerFragment()
    private val notifications = Notification()
    private var tag="Home"
    override val layout: Int = R.layout.activity_dashboard

    override fun init() {
        rvDrawer = findViewById<RecyclerView>(R.id.drawerRecyclerView)
        rvDrawer.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvDrawer.adapter = NavigationAdapter(getDrawerItems(), this)
        drawer = findViewById(R.id.nav_drawer)
        linearLayout = findViewById(R.id.drawer_linearlayout)
        bottomNav.setOnNavigationItemSelectedListener(this)
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

        viewProfile.setOnClickListener {
            bottomNav.selectedItemId=R.id.navigation_profile
            applayChanages(profile,"Profile")
            drawer.closeDrawers()
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
        applayChanages(home,"Home")
        hideProgress()

    }

    override fun onBackPressed() {
        if(!tag.equals("Home")){
            bottomNav.selectedItemId=R.id.navigation_home
            applayChanages(home,"Home")
        }else {
            super.onBackPressed()
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navigation_home->{
                applayChanages(home,"Home")
                return true
            }
            R.id.navigation_notifications->{
                applayChanages(notifications,"Notification")
                //Toast.makeText(this,"Notification",Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.navigation_profile->{
                applayChanages(profile,"Profile")
                return true
            }R.id.va_scanner->{
                applayChanages(scanner,"Scan Offer")
                return true
            }
            else->{
                return false
            }
        }


    }
    private fun applayChanages(fr: Fragment,tag:String){
        this.tag=tag
        titleBarName.text=tag
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
        Item("Shops", R.drawable.ic_users),
        //Item("Orders", R.drawable.ic_box),
        Item("Gift Cards", R.drawable.ic_giftcard),
        Item("History", R.drawable.ic_transaction),
        Item("About Us", R.drawable.ic_question),
        Item("Logout", R.drawable.ic_logout)

        // Item("Refer", R.drawable.ic_user)
    )

    override fun onItemClick(index: Int, adapter: Int) {
        if (adapter == DRAWER_NAV) {
            when (getDrawerItems()[index].name) {
//                "Offers" -> {
//                    drawer.closeDrawers()
//                    startActivity(
//                        Intent(
//                            this,
//                            Offers::class.java
//                        )
//                    )}
                "Gift Cards" -> {
                    drawer.closeDrawers()
                    startActivity(
                        Intent(
                            this,
                            Gifts::class.java
                        )
                    )}
//                "Orders" -> {
//                    drawer.closeDrawers()
//                    startActivity(
//                        Intent(
//                            this,
//                            Order::class.java
//                        )
//                    )}
                "Shops" -> {
                    drawer.closeDrawers()
                    startActivity(
                        Intent(
                            this,
                            Shops::class.java
                        )
                    )}
                "History" -> {
                    drawer.closeDrawers()
                startActivity(
                    Intent(
                        this,
                        HistoryActivity::class.java
                    )
                )}
                "Logout" -> logout()

                //Toast.makeText(this, get.get(index).name, Toast.LENGTH_LONG).show()
            }

        }


    }
}
