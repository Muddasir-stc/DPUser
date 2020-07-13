package com.dpoints.view.module.dashboard

import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.view.adapter.NavigationAdapter
import com.dpoint.dpointsuser.view.module.dashboard.HistoryFragment
import com.dpoint.dpointsuser.view.module.history.HistoryActivity
import com.dpoint.dpointsuser.view.module.membership.MemberShipCardActivity
import com.dpoint.dpointsuser.view.module.shops_near_me.ShopsNearMeActivity
import com.dpoints.dpointsmerchant.datasource.model.Item
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.auth.User
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.dpointsmerchant.view.module.shops.ShopViewModel
import com.dpoints.view.module.gifts.Gifts
import com.dpoints.view.module.notifications.Notification
import com.dpoints.view.module.offers.Offers
import com.dpoints.view.module.shops.Shops
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard.*

class Dashboard : BaseActivity(), OnItemClickListener,
    BottomNavigationView.OnNavigationItemSelectedListener {


    private lateinit var drawer: DrawerLayout
    private lateinit var rvDrawer: RecyclerView
    private var DRAWER_NAV = 1
    private lateinit var linearLayout: LinearLayout
    private val home = Home()
    private val profile = ProfileFragment()
    private val scanner = ScannerFragment()
    private val history = HistoryFragment()
    private val notifications = Notification()
    private val shopsNearMe = ShopsNearMeActivity()
    private var tag = "Home"
    private val viewModel by lazy { getVM<ShopViewModel>(this) }
    override val layout: Int = R.layout.activity_dashboard

    override fun init() {
        rvDrawer = findViewById<RecyclerView>(R.id.drawerRecyclerView)
        rvDrawer.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvDrawer.adapter = NavigationAdapter(getDrawerItems(), this)
        drawer = findViewById(R.id.nav_drawer)
        linearLayout = findViewById(R.id.drawer_linearlayout)
        bottomNav.setOnNavigationItemSelectedListener(this)
        showProgress(this)


        viewProfile.setOnClickListener {
            applyChanges(profile, "Profile")
        }

        if (intent.getStringExtra("NOTIFICATION") != null) {
            if (intent.getStringExtra("NOTIFICATION").equals("YES")) {
                bottomNav.selectedItemId = R.id.navigation_shop_near_me
                // applyChanges(notifications,"Notification")
            }
        } else {
            applyChanges(home, "Home")
        }
        //  Crashlytics.getInstance().crash()
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
            bottomNav.selectedItemId = R.id.navigation_profile
            applyChanges(profile, "Profile")
            drawer.closeDrawers()
        }
//
//        bottom_navigation.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.navigation_home -> {
//                    titleBarName.text = "Dashboard"
//                    applyChanges(Home())
//                }
//                R.id.navigation_notifications -> {
//                    titleBarName.text = "Notifications"
//                    applyChanges(Notification())
//                }
//                R.id.navigation_setting -> {
//                    true
//                }
//                R.id.navigation_profile -> {
//                    titleBarName.text = "Profile"
//                    applyChanges(Profile())
//                }
//            }
//            false
//        }

        hideProgress()

    }

    override fun onBackPressed() {
        if (!tag.equals("Home")) {
            bottomNav.selectedItemId = R.id.navigation_home
            applyChanges(home, "Home")
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                applyChanges(home, "Home")
                return true
            }
            R.id.navigation_shop_near_me -> {
                viewModel.getShops(UserPreferences.instance.getTokken(this)!!)
                addObserver()
                //Toast.makeText(this,"Notification",Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.navigation_profile -> {
                applyChanges(profile, "Profile")
                return true
            }
            R.id.va_scanner -> {
                applyChanges(scanner, "Scan Offer")
                return true
            }
            R.id.navigation_wallet -> {
                applyChanges(HistoryFragment(), "Wallet")
                return true
            }
            else -> {
                return false
            }
        }
    }

    private fun applyChanges(fr: Fragment, tag: String) {
        this.tag = tag
        titleBarName.text = tag
        supportFragmentManager.beginTransaction().replace(R.id.container, fr).commit()
    }

    private fun getDrawerItems() = listOf(
        Item("Shops", R.drawable.ic_users),
        //Item("Orders", R.drawable.ic_box),
        Item("Gift Cards", R.drawable.ic_giftcard),
     //   Item("MemberShip Card", R.drawable.ic_giftcard),
        Item("History", R.drawable.ic_transaction),
        Item("Shops With Offer", R.drawable.ic_users),
        Item("Offers", R.drawable.ic_users),
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
                    )
                }
                "MemberShip Card" -> {
                    drawer.closeDrawers()
                    startActivity(
                        Intent(
                            this,
                            MemberShipCardActivity::class.java
                        )
                    )
                }
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
                    )
                }
                "Shops With Offer" -> {
                    drawer.closeDrawers()
                    var intent = Intent(this, Shops::class.java)
                    intent.putExtra("data", "offer")
                    startActivity(intent)
                }
                "Offers" -> {
                    drawer.closeDrawers()
                    startActivity(
                        Intent(
                            this,
                            Offers::class.java
                        )
                    )
                }
                "History" -> {
                    drawer.closeDrawers()
                    startActivity(
                        Intent(
                            this,
                            HistoryActivity::class.java
                        )
                    )
                }
                "Logout" -> logout()

                //Toast.makeText(this, get.get(index).name, Toast.LENGTH_LONG).show()
            }

        }


    }

    private fun addObserver() {
        viewModel.shopsState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }
            hideProgress()
            Log.e("DATA", state.toString())
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message)
                    for (model in state.data!!.data) {
                        if (model.website == null)
                            model.website = ""
                        if (model.twitter == null)
                            model.twitter = ""
                        if (model.facebook == null)
                            model.facebook = ""
                        if (model.instagram == null)
                            model.instagram = ""
                    }
                    applyChanges(
                        ShopsNearMeActivity.newInstance("Shops Near By", state.data),
                        "Shops Near By"
                    )
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })

    }
}
