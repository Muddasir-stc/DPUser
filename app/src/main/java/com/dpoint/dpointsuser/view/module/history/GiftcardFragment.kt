package com.dpoints.view.module.dashboard

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.model.ScanedOffer
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.fromJson
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import kotlinx.android.synthetic.main.content_dashboard.*


class GiftcardFragment : BaseFragment(){
    override val layout: Int=R.layout.fragment_giftcard_history
    override fun init(view: View) {

    }

}