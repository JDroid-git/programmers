package com.jdroid.flo.ui.activity.music.viewModel

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdroid.flo.server.ServerApi
import com.jdroid.flo.server.data.MusicInfo
import kotlinx.coroutines.*

class MusicViewModel : ViewModel() {

    private var musicJob: Job? = null

    val musicData = MutableLiveData<MusicInfo>()
    val musicLoading = MutableLiveData<Boolean>()

    val musicPosition = MutableLiveData<Int>()
    val musicPlayer = MutableLiveData<MediaPlayer>().apply { value = MediaPlayer() }

    val musicException = MutableLiveData<String?>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception : ${throwable.localizedMessage}")
    }

    fun callMusicInfo() {
        getMusicInfo()
    }

    private fun getMusicInfo() {
        musicLoading.value = true
        musicJob = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = ServerApi.getMusicInfo()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("wonjin", "${response.body()}")
                    musicData.value = response.body()
                    musicException.value = null
                    musicLoading.value = false
                } else {
                    onError("Error : ${response.message()}")
                }
            }
        }
    }

    private fun onError(message: String) {
        musicException.value = message
        musicLoading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        musicJob?.cancel()
    }
}