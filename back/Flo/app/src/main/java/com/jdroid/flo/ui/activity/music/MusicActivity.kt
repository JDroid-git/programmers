package com.jdroid.flo.ui.activity.music

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.flo.databinding.ActivityMusicBinding
import com.jdroid.flo.server.ServerApi
import com.jdroid.flo.server.ServerCallback
import com.jdroid.flo.util.Constants
import retrofit2.Call
import retrofit2.Response

class MusicActivity : AppCompatActivity(), View.OnClickListener, ServerCallback {

    private val binding by lazy { ActivityMusicBinding.inflate(layoutInflater) }
    private var mediaPlayer: MediaPlayer? = null

    override fun <Any> onSuccess(call: Call<Any>, response: Response<Any>) {
        when (call.request().url().toString()) {
            Constants.BASE_URL -> {

            }
        }
    }

    override fun <Any> onFailed(call: Call<Any>, response: Response<Any>?, t: Throwable?) {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnPlay.id -> {
                if (binding.btnPlay.isChecked) {
                    mediaPlayer?.let {
                        it.setDataSource()
                    }
                } else {

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initData()
        initLayout()
        initListener()
    }

    private fun initData() {
        ServerApi.getMusicInfo(this)
    }

    private fun initLayout() {
        setContentView(binding.root)
    }

    private fun initListener() {
        binding.btnPlay.setOnClickListener(this)
    }
}