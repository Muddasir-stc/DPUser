//package com.chancecoin.chance.views.commons.dialog
//
//import android.annotation.SuppressLint
//import android.app.Dialog
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.os.Bundle
//import android.view.Gravity
//import android.view.View
//import android.widget.ImageView
//import androidx.appcompat.app.AlertDialog
//import androidx.fragment.app.DialogFragment
//import com.chancecoin.chance.R
//import com.chancecoin.chance.utilities.generateQrCode
//
//
//class QRDialog : DialogFragment() {
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        @SuppressLint("InflateParams")
//        val v = activity?.layoutInflater?.inflate(R.layout.dialog_qr, null)
//        val builder = AlertDialog.Builder(activity ?: return this!!.dialog)
//        builder.setView(v).setCancelable(true)
//
//        val dialog = builder.create()
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.window?.setGravity(Gravity.CENTER)
//
//        init(v)
//
//        return dialog
//    }
//
//    private fun init(view: View?) {
//        val json = arguments?.getString(JSON) ?: return
//        val bitmap = generateQrCode(json) ?: return
//        view?.findViewById<ImageView>(R.id.image_view)?.setImageBitmap(bitmap)
//    }
//
//    companion object {
//        private const val JSON = "json"
//
//        fun build(
//            json: String,
//            isCancellable: Boolean = true
//        ) = QRDialog().apply {
//            arguments = Bundle().apply { putString(JSON, json) }
//            isCancelable = isCancellable
//        }
//    }
//
//}