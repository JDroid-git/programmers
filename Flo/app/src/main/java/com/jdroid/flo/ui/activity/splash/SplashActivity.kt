package com.jdroid.flo.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jdroid.flo.R
import com.jdroid.flo.databinding.ActivitySplashBinding
import com.jdroid.flo.ui.activity.music.MusicActivity
import java.util.*
import kotlin.concurrent.timer

class SplashActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val timerTask = object: TimerTask() {
            override fun run() {
                startActivity(Intent(this@SplashActivity, MusicActivity::class.java))
                finish()
            }
        }
        Timer().schedule(timerTask, 3000)
    }
}