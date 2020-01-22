package com.dpoint.dpointsuser.view.module.gifts

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.editoffer.EditOfferService
import com.dpoints.dpointsmerchant.datasource.remote.editoffer.ShopModel
import com.dpoints.dpointsmerchant.datasource.remote.offer.AddOfferModel
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferService
import com.dpoints.dpointsmerchant.utilities.Event
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

class QrViewModel : ViewModel() {
   lateinit var bitMatrix: BitMatrix
    private val _qrState = MutableLiveData<Event<NetworkState<Bitmap>>>()
    val qrState: LiveData<Event<NetworkState<Bitmap>>> get() = _qrState


    fun getQrImage(value: String) {
        val QRcodeWidth=500
        _qrState.value = Event(NetworkState.Loading())

        try {
            bitMatrix = MultiFormatWriter().encode(
                value,
                BarcodeFormat.DATA_MATRIX,
                QRcodeWidth, QRcodeWidth, null
            )

        } catch (Illegalargumentexception: IllegalArgumentException) {

            _qrState.value = Event(NetworkState.Error("Argument Exception"))
        }

        val bitMatrixWidth = bitMatrix.width

        val bitMatrixHeight = bitMatrix.height

        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth

            for (x in 0 until bitMatrixWidth) {

                pixels[offset + x] = if (bitMatrix.get(x, y))
                    Color.BLACK
                else
                    Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
        _qrState.value = Event(NetworkState.Success(bitmap))
    }

}