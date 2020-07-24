//package com.dpoint.dpointsuser.utilities
//
//import android.graphics.Bitmap
//import com.google.zxing.BarcodeFormat
//import com.google.zxing.MultiFormatWriter
//import com.google.zxing.WriterException
//import com.journeyapps.barcodescanner.BarcodeEncoder
//
//fun generateQrCode(text: String): Bitmap? {
//    val multiFormatWriter =  MultiFormatWriter();
//    var bitmap : Bitmap? = null
//
//    try {
//        val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
//        val barcodeEncoder = BarcodeEncoder();
//        bitmap = barcodeEncoder.createBitmap(bitMatrix);
//    } catch (e : WriterException) {
//        e.printStackTrace();
//    }
//    return bitmap
//}