/*
* HTML2Entities v1.3
* Copyright (C) Simon Raichl 2018
* MIT License
* Use this as you want, share it as you want, do basically whatever you want with this :)
*/

package com.VelociraptorCZE.HTML2Entities

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.ClipData
import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AlertDialog
import android.text.InputType
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

open class Main : AppCompatActivity() {

    private val chars = arrayOf("<", ">", "&", '"', "'", "©", "®", "€", "¢", "£", "¥", "§", "|", "$", "#", "@")
    private val entities = arrayOf("&lt;", "&gt;", "&amp;", "&quot;", "&apos;", "&copy;", "&reg;", "&euro;", "&cent;", "&pound;", "&yen;", "&sect;", "&verbar;", "&dollar;", "&num;", "&commat;")
    var webpage = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.clearBtn).setOnClickListener{
            getInput().setText("")
        }
        findViewById<Button>(R.id.convertPageBtn).setOnClickListener{
            getPageContents()
        }
        findViewById<Button>(R.id.copyBtn).setOnClickListener{
            copyAll()
        }

        OnChangeTextListener.Add(getInput(), ::convert)

        getOutput().isFocusable = false
        getOutput().isLongClickable = false
    }

    fun convert(){
        val inputField = getInput()
        val outputField = getOutput()
        val content = inputField.text.split("")
        var output = ""
        for (i in 0..(content.size-1)) {
            var char = content[i]
            for (a in 0..(chars.size-1)) {
                if (chars[a].toString() == char) {
                    char = entities[a]
                }
            }
            output += char
        }
        outputField.setText(output)
    }

    private fun copyAll(){
        val outputField = getOutput()
        val clip = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val content = ClipData.newPlainText("copy", outputField.text)
        clip.primaryClip = content
        toast("Copied to clipboard")
    }

    private fun getPageContents(){
        class DownloadPage : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void): String {
                val content: Document
                var code = ""
                try {
                    content = Jsoup.connect(webpage).get()
                    code = content.outerHtml()
                }
                catch (e: Exception) {}
                return code
            }
            override fun onPostExecute(content: String) {
                if (content == ""){
                    toast("Something went wrong, try to check your internet connection or your given URL, no response")
                }
                else{
                    getInput().setText(content)
                    convert()
                }
            }
        }
        with(AlertDialog.Builder(this)){
            setTitle("Import web page and convert")
            setMessage("Type the web page url, if you don't enter the protocol, it will be automatically assigned to your URL https:// prefix")
            val urlInput : EditText ?
            urlInput = EditText(context)
            urlInput.hint = "URL"
            urlInput.inputType = InputType.TYPE_CLASS_TEXT
            setView(urlInput)

            val posBtn = setPositiveButton("Download and convert") {
                popup, _ ->
                webpage = urlInput.text.toString()
                if (!webpage.contains("https://") && !webpage.contains("http://")){
                    webpage = "https://$webpage"
                }
                popup.dismiss()
            }
            posBtn.setOnDismissListener{
                DownloadPage().execute()
            }
        }.show()
    }

    fun toast(param: String){
        Toast.makeText(this, param, Toast.LENGTH_LONG).show()
    }

    private fun getInput(): EditText{
        val input = findViewById<EditText>(R.id.inputField); return input
    }

    private fun getOutput(): EditText{
        val output = findViewById<EditText>(R.id.outputField); return output
    }
}