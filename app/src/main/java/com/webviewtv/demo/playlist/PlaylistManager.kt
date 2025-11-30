package com.webviewtv.demo.playlist

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.webviewtv.demo.misc.application
import com.webviewtv.demo.misc.preference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.File
import java.util.concurrent.TimeUnit

object PlaylistManager {

    private const val TAG = "PlaylistManager"
    private const val CACHE_EXPIRATION_MS = 24 * 60 * 60 * 1000L
    private const val KEY_PLAYLIST_URL = "playlist_url"
    private const val KEY_LAST_UPDATE = "last_update"
    private const val UPDATE_RETRY_DELAY = 10 * 1000L

    private val client = OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build()
    private val gson = GsonBuilder().setPrettyPrinting().create()!!
    private val jsonTypeToken = object : TypeToken<List<Channel>>() {}
    private val playlistFile = File(application.filesDir, "playlist.json")
    private val builtInPlaylists = listOf<Pair<String, String>>()

    var onPlaylistChange: ((Playlist) -> Unit)? = null
    var onUpdatePlaylistJobStateChange: ((Boolean) -> Unit)? = null
    private var updatePlaylistJob: Job? = null
    private var isUpdating = false
        set(value) {
            onUpdatePlaylistJobStateChange?.invoke(value)
        }

    fun getBuiltInPlaylists() = builtInPlaylists

    fun setPlaylistUrl(url: String) {
        preference.edit()
            .putString(KEY_PLAYLIST_URL, url)
            .putLong(KEY_LAST_UPDATE, 0)
            .apply()
        requestUpdatePlaylist()
    }

    fun getPlaylistUrl() = "https://api.jsonbin.io/v3/b/692bd7afd0ea881f4008efe7"

    fun setLastUpdate(time: Long, requestUpdate: Boolean = false) {
        preference.edit().putLong(KEY_LAST_UPDATE, time).apply()
        if (requestUpdate) requestUpdatePlaylist()
    }

    private fun requestUpdatePlaylist() {
        val lastJobCompleted = updatePlaylistJob?.isCompleted
        if (lastJobCompleted != null && !lastJobCompleted) {
            Log.i(TAG, "A job is executing, ignore!")
            return
        }
        updatePlaylistJob = CoroutineScope(Dispatchers.IO).launch {
            var times = 0
            val needUpdate = { System.currentTimeMillis() - preference.getLong(KEY_LAST_UPDATE, 0L) > CACHE_EXPIRATION_MS }
            isUpdating = true
            while (needUpdate()) {
                ++times
                Log.i(TAG, "Updating playlist... times=${times}")
                if (times == 3)break
                try {
                    val request = Request.Builder().url(getPlaylistUrl()).get().build()
                    val response = client.newCall(request).execute()
                    if (!response.isSuccessful) throw Exception("Response code ${response.code}")

                    val remote = response.body!!.string()
                    val local = runCatching { playlistFile.readText() }.getOrNull()

                    val listFromJson = getListFromJson(remote) // 从jsonbin中取，默认加了对象

                    if (remote != local) {
                        playlistFile.writeText(remote)
                        onPlaylistChange?.invoke(createPlaylistFromJson(listFromJson))
                    }

                    setLastUpdate(System.currentTimeMillis())
                    Log.i(TAG, "Update playlist successfully.")
                    break
                } catch (e: Exception) {
                    Log.w(TAG, "Cannot update playlist, reason: ${e.message}")
                }
                if (needUpdate()) {
                    delay(UPDATE_RETRY_DELAY)
                }
            }
            isUpdating = false
        }
    }

    private fun getListFromJson(string: String): String{
        return JsonParser.parseString(string).asJsonObject.get("record").toString()
    }

    private fun createPlaylistFromJson(json: String): Playlist {
        val channels = gson.fromJson(json, jsonTypeToken)
        return Playlist.createFromAllChannels("default", channels)
    }

    private fun loadBuiltInPlaylist(): Playlist {
        val json = application.assets.open("default_playlist.json").bufferedReader().use(BufferedReader::readText)
        return createPlaylistFromJson(json)
    }

    fun loadPlaylist(): Playlist {
        return try {
            val json = playlistFile.readText()
            val listFromJson = getListFromJson(json) // 从jsonbin中取，默认加了对象
            createPlaylistFromJson(listFromJson)
        } catch (e: Exception) {
            Log.w(TAG, "Cannot load playlist, reason: ${e.message}")
            setLastUpdate(0L)// 设置成 0 ，就会联网更新；
            loadBuiltInPlaylist()
        } finally {
            requestUpdatePlaylist()
        }
    }

}