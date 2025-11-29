package com.webviewtv.demo.adapter

import android.view.KeyEvent
import com.webviewtv.demo.widget.WebpageAdapterWebView

class TvbWebpageAdapter : CommonWebpageAdapter() {

    override fun isAdaptedUrl(url: String) = url.contains("news.tvb.com")

    override suspend fun enterFullscreen(webView: WebpageAdapterWebView) {
        enterFullscreenByPressKey(webView, KeyEvent.KEYCODE_F)
    }
}