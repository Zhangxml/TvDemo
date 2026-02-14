package com.webviewtv.demo.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.webviewtv.demo.R
import com.webviewtv.demo.playlist.Channel

class ChannelBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DISMISS_DELAY = 1000*15L
    }

    private val tvChannelName: TextView
    private val tvChannelUrl: TextView
    //private val tvProgress: TextView

    private val dismissAction = Runnable { visibility = GONE }

    init {
        isClickable = true
        isFocusable = false
        setBackgroundResource(R.drawable.bg)
        LayoutInflater.from(context).inflate(R.layout.widget_channel_bar, this)
        tvChannelName = findViewById(R.id.tvChannelName)
        tvChannelUrl = findViewById(R.id.tvChannelUrl)
        //tvProgress = findViewById(R.id.tvProgress)
        visibility = GONE
    }

    fun setCurrentChannelAndShow(channel: Channel) {
        removeCallbacks(dismissAction)
        tvChannelName.text = channel.name

        tvChannelUrl.tag = channel.url
        tvChannelUrl.text = channel.url + " 0%"

        setProgress(0)
        visibility = VISIBLE
    }

    fun dismiss() {
        removeCallbacks(dismissAction)
        post { visibility = GONE }
        Log.i("ChannelBarView", "dismiss: >>>>>")
    }

    fun setProgress(progress: Int) {
        var progressCurrent = progress
        removeCallbacks(dismissAction)
        if (progressCurrent == 100) progressCurrent = 99
        tvChannelUrl.text = tvChannelUrl.tag.toString() + " $progressCurrent%"
        if (progress == 100) { // 进度100时，实际可能不会全屏播放在
            postDelayed(dismissAction, DISMISS_DELAY)
        }
    }
}