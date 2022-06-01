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

class PlayingFragment : Fragment() {

    private lateinit var binding: FragmentPlayingBinding

    private var musicViewModel: MusicViewModel? = null

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
                }
            }
//            binding.imgMusic.setImageURI(Uri.parse(it.image))
        })
    }

    private suspend fun getImage(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
//            val bis = BufferedInputStream(URL(url).openStream(), 1024)
//            val baos = ByteArrayOutputStream()
//            val bos = BufferedOutputStream(baos, 1024)
//            copy()
            val inputStream = URL(url).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)

        } catch (e: IOException) {
            Log.e("wonjin", "${e.message}")
        }

        return bitmap
    }

}