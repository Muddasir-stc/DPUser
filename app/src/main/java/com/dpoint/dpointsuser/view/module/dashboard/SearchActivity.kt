package com.dpoint.dpointsuser.view.module.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dpoint.dpointsuser.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchView.requestFocus()
    }
}
