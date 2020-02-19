package com.dpoint.dpointsuser.datasource.remote.shop

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.android.parcel.Parcelize

@Parcelize
class Shop(
    var address: String,
    var business_hours: String,
    var category_name: String,
    var coin_value: Int,
    var contact: String,
    var created_at: String,
    var description: String,
    var email: String,
    var facebook: String,
    var id: Int,
    var instagram: String,
    var latitude: String,
    var longitude: String,
    var map: String,
    var membership_status: String,
    var merchant_id: Int,
    var profile_picture: String,
    var rating: String,
    var shop_category_id: Int,
    var shop_name: String,
    var twitter: String,
    var updated_at: String,
    var website: String,
    var shop_percentage: String
) : Parcelable, ClusterItem {
    override fun getSnippet(): String {
        return description
    }

    override fun getTitle(): String {
        return shop_name
    }

    override fun getPosition(): LatLng {
        return LatLng(latitude.toDouble(), longitude.toDouble())
    }

}