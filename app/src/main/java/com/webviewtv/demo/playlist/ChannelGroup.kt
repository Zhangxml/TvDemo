package com.webviewtv.demo.playlist

data class ChannelGroup(
    var name: String,
    val channels: MutableList<Channel> = mutableListOf()
)