package com.dpoint.dpointsuser.utilities

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout


fun TextInputLayout.setErrorMessage(errorMessage: String) {
    error = errorMessage
}

fun TextInputLayout.clearErrorMessage() {
    error = null
    isErrorEnabled = false
}

inline fun EditText.onTextChanged(
    crossinline block: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit
) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            block(s, start, before, count)
        }
    })
}