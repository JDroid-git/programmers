package com.jdroid.flo.ui.activity.music

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jdroid.flo.databinding.ActivityMusicBinding
import com.jdroid.flo.ui.activity.music.viewModel.MusicViewModel
import com.jdroid.flo.ui.fragment.music.PlayingFragment
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class MusicActivity : AppCompatActivity(), View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    private val binding by lazy { ActivityMusicBinding.inflate(layoutInflater) }

    private var viewModel: MusicViewModel? = null
    private var seekThread: Job? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnPlay.id -> {
                if (binding.btnPlay.isChecked) {
                    viewModel?.musicPlayer?.value?.start()
                } else {
                    viewModel?.musicPlayer?.value?.pause()
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
        binding.txtSeek.visibility = View.VISIBLE
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        viewModel?.musicPlayer?.value?.seekTo(seekBar?.progress ?: 0)
        binding.txtSeek.visibility = View.INVISIBLE
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            val defaultWidth = ((binding.root.width - (seekBar?.measuredWidth ?: 0)) / 2) - (binding.txtSeek.measuredWidth / 2)
            val x = ((seekBar?.measuredWidth ?: 0).toFloat() / (seekBar?.max ?: 0).toFloat() * progress.toFloat()) + defaultWidth
            binding.txtSeek.x = x
            binding.txtSeek.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(progress.toLong()))
        }
    }


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
                if (viewModel?.musicPlayer?.value?.isPlaying == true) {
                    withContext(Dispatchers.Main) {
                        binding.seekMusic.progress = viewModel?.musicPlayer?.value?.currentPosition ?: 0
                        viewModel?.musicPosition?.value = viewModel?.musicPlayer?.value?.currentPosition ?: 0
                        binding.txtPositionTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(viewModel?.musicPlayer?.value?.currentPosition?.toLong() ?: 0))
                    }
                }
                Thread.sleep(500)
            }
        }

        binding.txtDurationTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date((viewModel?.musicPlayer?.value?.duration ?: 0).toLong()))
        binding.seekMusic.max = viewModel?.musicPlayer?.value?.duration ?: 0

        supportFragmentManager.beginTransaction().add(binding.layoutContainer.id, PlayingFragment()).commit()
    }

    private fun initListener() {
        binding.btnPlay.setOnClickListener(this)
        binding.seekMusic.setOnSeekBarChangeListener(this)
        viewModel?.musicPlayer?.value?.setOnCompletionListener(this)
    }

    private fun observeViewModel() {
        viewModel?.musicLoading?.observe(this, Observer { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        viewModel?.musicData?.observe(this, Observer { data ->
            try {
                viewModel?.musicPlayer?.value?.setDataSource(data.file)
                viewModel?.musicPlayer?.value?.prepare()
            } catch (e: Exception) {
                Log.e("wonjin", "${e.message}")
            } finally {
                binding.txtDurationTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date((viewModel?.musicPlayer?.value?.duration ?: 0).toLong()))
                binding.seekMusic.max = viewModel?.musicPlayer?.value?.duration ?: 0
            }
        })
    }
}