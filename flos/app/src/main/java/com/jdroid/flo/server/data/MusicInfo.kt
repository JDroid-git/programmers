package com.jdroid.flo.server.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicInfo(
    val singer: String,
    val album: String,
    val title: String,
    val duration: Int,
    val image: String,
    val file: String,
    val lyrics: String
) : Parcelable