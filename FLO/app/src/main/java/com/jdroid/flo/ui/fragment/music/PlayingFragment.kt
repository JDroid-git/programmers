package com.jdroid.flo.ui.fragment.music

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jdroid.flo.databinding.FragmentPlayingBinding
import com.jdroid.flo.ui.activity.music.viewModel.MusicViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PlayingFragment : Fragment() {

    private lateinit var binding: FragmentPlayingBinding

    private var musicViewModel: MusicViewModel? = null
    private var position = 0
    private val arrayLyricsTime = ArrayList<Date>()
    private val arrayLyrics = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlayingBinding.inflate(inflater, container, false)

        initData()

        return binding.root
    }


    private fun initData() {
        musicViewModel = ViewModelProvider(requireActivity()).get(MusicViewModel::class.java)
        observeViewModel()
    }

    private fun observeViewModel() {
        musicViewModel?.musicData?.observe(requireActivity(), Observer {
            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = getImage(it.image)
                withContext(Dispatchers.Main) {
                    binding.imgMusic.setImageBitmap(bitmap)
                    binding.txtMusicTitle.text = it.title
                    binding.txtAlbum.text = it.album
                    binding.txtSinger.text = it.singer
                }
                val simpleDateFormat = SimpleDateFormat("mm:ss:SSS", Locale.getDefault())
                it.lyrics.split("\n").forEach {
                    simpleDateFormat.parse(it.substring(1, 9))?.let { it1 -> arrayLyricsTime.add(it1) }
                    arrayLyrics.add(it.substring(11))
                }
            }
        })

        musicViewModel?.musicPosition?.observe(requireActivity(), Observer {
            CoroutineScope(Dispatchers.IO).launch {
                position = it
            }
        })
    }

    private fun getImage(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val inputStream = URL(url).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)

        } catch (e: IOException) {
            Log.e("wonjin", "${e.message}")
        }

        return bitmap
    }

}