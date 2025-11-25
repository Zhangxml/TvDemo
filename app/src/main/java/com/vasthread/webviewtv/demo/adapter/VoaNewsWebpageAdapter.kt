package com.vasthread.webviewtv.demo.adapter

import android.view.KeyEvent
import com.vasthread.webviewtv.demo.widget.WebpageAdapterWebView

class VoaNewsWebpageAdapter:CommonWebpageAdapter() {

    override fun isAdaptedUrl(url: String) = url.contains("voanews.com")

    override suspend fun enterFullscreen(webView: WebpageAdapterWebView) {
        enterFullscreenByPressKey(webView, KeyEvent.KEYCODE_F)
    }

}