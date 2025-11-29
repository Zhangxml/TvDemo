package com.webviewtv.demo.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.webviewtv.demo.R
import com.webviewtv.demo.widget.WebpageAdapterWebView

class TbsDebugActivity: AppCompatActivity() {

    companion object {
        private const val DEBUG_URL = "https://debugtbs.qq.com";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tbs_debug)
        val webView = findViewById<WebpageAdapterWebView>(R.id.webView)
        webView.loadUrl(DEBUG_URL)
    }
}