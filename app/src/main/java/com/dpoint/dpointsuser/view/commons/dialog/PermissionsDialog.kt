package com.chancecoin.chance.views.commons.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.dpoint.dpointsuser.R

class PermissionsDialog private constructor(
    private val title: String,
    private val message: String,
    private val negativeText: String,
    private val positiveText: String,
    private val cancelable: Boolean,
    private var negativeListener: () -> Unit,
    private val positiveListener: (Context) -> Unit = {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", it.packageName, null)
        intent.data = uri
        it.startActivity(intent)
    }
) {

    fun show(context: Context) {
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(context, R.style.AlertDialogStyle)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(positiveText) { dialog, _ ->
            dialog.dismiss()
            positiveListener(context)
        }

        builder.setNegativeButton(negativeText) { dialog, _ ->
            dialog.dismiss()
            negativeListener()
        }

        builder.setCancelable(cancelable)
        alertDialog = builder.create()
        alertDialog.show()
    }


    class Builder {
        private var mTitle: String? = null
        private var mDescription: String? = null
        private var mNegativeText: String = "Not now"
        private var mPositiveText: String = "App Settings"
        private var negativeListener: () -> Unit = {}
        private var cancelable = false
        private var positiveListener: ((Context) -> Unit)? = null

        fun setTitle(title: String): Builder {
            this.mTitle = title
            return this
        }

        fun setDescription(description: String): Builder {
            this.mDescription = description
            return this
        }

        fun setNegativeText(text: String): Builder {
            this.mNegativeText = text
            return this
        }

        fun setPositiveText(text: String): Builder {
            this.mPositiveText = text
            return this
        }

        fun addNegativeListener(block: () -> Unit = {}): Builder {
            this.negativeListener = block
            return this
        }

        fun addPositiveListener(listener: (Context) -> Unit): Builder {
            positiveListener = listener
            return this
        }

        fun isCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun build(): PermissionsDialog {
            val title = requireNotNull(mTitle)
            val description = requireNotNull(mDescription)

            return positiveListener?.let {
                PermissionsDialog(
                    title,
                    description,
                    mPositiveText,
                    mNegativeText,
                    cancelable,
                    negativeListener,
                    it
                )
            } ?: PermissionsDialog(
                title,
                description,
                mPositiveText,
                mNegativeText,
                cancelable,
                negativeListener
            )
        }
    }
}