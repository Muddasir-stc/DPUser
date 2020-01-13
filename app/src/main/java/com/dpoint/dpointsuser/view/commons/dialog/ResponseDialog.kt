package com.chancecoin.chance.views.commons.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.dpoint.dpointsuser.R
import kotlinx.android.synthetic.main.dialog_response.*

class ResponseDialog : DialogFragment() {

    private var actionBlock: () -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.layoutInflater
        @SuppressLint("InflateParams")

        val v = inflater?.inflate(R.layout.dialog_response, null)
        val builder = AlertDialog.Builder(activity ?: return this!!.dialog!!)
        builder.setView(v).setCancelable(true)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)

        init(v)

        return dialog
    }



    private fun init(view: View?) {
        arguments?.apply {
            view?.findViewById<ImageView>(R.id.image_view)?.setImageResource(getInt("icon"))
          /*  image_view.setColorFilter(
                ContextCompat.getColor(
                    context!!,
                    getInt("tintColor")
                ), android.graphics.PorterDuff.Mode.MULTIPLY
            )*/

            view?.findViewById<TextView>(R.id.tv_message)?.text = getString("message")
            view?.findViewById<TextView>(R.id.tv_action)?.text = getString("text")
        }

        view?.findViewById<TextView>(R.id.tv_action)?.setOnClickListener {
            dismiss()
            actionBlock()
        }
    }

    class Builder {
        private var actionBlock: () -> Unit = {}
        private var message = ""
        private var text = ""
        private var icon = R.drawable.ic_success
        private var isCancellable = false

        fun attachActionBlock(block : () -> Unit) : Builder {
            actionBlock = block
            return this
        }

        fun message (message: String) : Builder{
            this.message = message
            return this
        }

        fun icon (icon: Int) : Builder {
            this.icon = icon
            return this
        }

      fun btnText (text: String) : Builder {
          this.text = text
          return this
      }

        fun isCancellable(isCancellable : Boolean) : Builder {
            this.isCancellable = isCancellable
            return this
        }

        fun build() : ResponseDialog {
            val fragment = ResponseDialog()
            val bundle = Bundle()

            bundle.putString("message", message)
            bundle.putString("text", text)
            bundle.putInt("icon", icon)

            fragment.arguments = bundle
            fragment.isCancelable = isCancellable
            fragment.actionBlock = actionBlock

            return fragment
        }
    }


}