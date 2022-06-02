package com.jdroid.flo.ui.activity.music

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jdroid.flo.databinding.ActivityMusicBinding
import com.jdroid.flo.server.ServerApi
import com.jdroid.flo.server.ServerCallback
import com.jdroid.flo.ui.activity.music.viewModel.MusicViewModel
import com.jdroid.flo.ui.fragment.music.PlayingFragment
import com.jdroid.flo.util.Constants
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response

class MusicActivity : AppCompatActivity(), View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    private val binding by lazy { ActivityMusicBinding.inflate(layoutInflater) }

    private var mediaPlayer = MediaPlayer()
    private var viewModel: MusicViewModel? = null
    private var seekThread: Job? = null
    private var musicState = false

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnPlay.id -> {
                musicState = if (binding.btnPlay.isChecked) {
                    binding.seekMusic.max = mediaPlayer.duration
                    mediaPlayer.start()
                    true
                } else {
                    mediaPlayer.pause()
                    false
                }
            }
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        binding.btnPlay.isChecked = false
        binding.seekMusic.progress = 0
        binding.seekMusic.max = 0
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        musicState = false
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        musicState = true
        mediaPlayer.seekTo(seekBar?.progress ?: 0)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initLayout()
        initData()
        initListener()
    }

    private fun initData() {
        viewModel = ViewModelProvider(this).get(MusicViewModel::class.java)
        viewModel?.callMusicInfo()
        observeViewModel()
    }

    private fun initLayout() {
        setContentView(binding.root)

        seekThread = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                if (musicState) {
                    withContext(Dispatchers.Main) {
                        binding.seekMusic.progress = mediaPlayer.currentPosition
                        viewModel?.musicPosition?.value = mediaPlayer.currentPosition
                    }
                }
                Thread.sleep(500)
            }
        }

        supportFragmentManager.beginTransaction().add(binding.layoutContainer.id, PlayingFragment()).commit()
    }

    private fun initListener() {
        binding.btnPlay.setOnClickListener(this)
        binding.seekMusic.setOnSeekBarChangeListener(this)
        mediaPlayer.setOnCompletionListener(this)
    }

    private fun observeViewModel() {
        viewModel?.musicLoading?.observe(this, Observer { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnPlay.visibility = View.INVISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        viewModel?.musicData?.observe(this, Observer { data ->
            mediaPlayer.setDataSource(data.file)
            mediaPlayer.prepare()

            binding.btnPlay.visibility = View.VISIBLE
        })
    }
}