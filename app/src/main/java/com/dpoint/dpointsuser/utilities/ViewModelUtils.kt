package com.dpoint.dpointsuser.utilities

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

inline fun <reified T: ViewModel> getVM(activity: FragmentActivity): T {
    return ViewModelProviders.of(activity).get(T::class.java)
}