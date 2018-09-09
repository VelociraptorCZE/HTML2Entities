/*
* OnChangeTextListener
* Copyright (C) Simon Raichl 2018
* MIT License
* Use this as you want, share it as you want, do basically whatever you want with this :)
*/

package OnChangeTextListener

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class Add {
    constructor(txtObj: EditText, func: () -> Unit) {
        txtObj.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence, st: Int, b: Int, c: Int) {
                func.invoke()
            }
        })
    }
}
