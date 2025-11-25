package com.vasthread.webviewtv.demo.adapter

import com.vasthread.webviewtv.demo.widget.WebpageAdapterWebView

class YoutubeWebpageAdapter : CommonWebpageAdapter() {

    override fun isAdaptedUrl(url: String) = url.contains("youtube.com")

    override fun userAgent() = null

    override suspend fun enterFullscreen(webView: WebpageAdapterWebView) {
        enterFullscreenByPressKey(webView)
    }
}