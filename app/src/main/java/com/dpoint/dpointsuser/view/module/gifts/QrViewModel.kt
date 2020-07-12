package com.dpoint.dpointsuser.view.module.gifts

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.utilities.Event
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class QrViewModel : ViewModel() {
   lateinit var bitMatrix: BitMatrix
    private val _qrState = MutableLiveData<Event<NetworkState<Bitmap>>>()
    val qrState: LiveData<Event<NetworkState<Bitmap>>> get() = _qrState


    fun getQrImage(value: String) {
            _qrState.value = Event(NetworkState.Loading())
            val multiFormatWriter = MultiFormatWriter()
            try {
                val bitMatrix =
                    multiFormatWriter.encode(value, BarcodeFormat.QR_CODE, 500, 500)
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                _qrState.value = Event(NetworkState.Success(bitmap))
            } catch (e: WriterException) {
                e.printStackTrace()
                _qrState.value = Event(NetworkState.Error("Argument Exception"))
            }
    }
    /* fun getQrImage(value: String) {
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
    }*/

}