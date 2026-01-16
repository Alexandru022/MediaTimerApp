package com.example.soundservice

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast

class SoundService : Service() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Redă melodia din res/raw/melodie.mp3 [cite: 55, 195]
        mediaPlayer = MediaPlayer.create(this, R.raw.melodie)
        mediaPlayer.isLooping = true
        mediaPlayer.start() // [cite: 59, 207]

        Toast.makeText(this, "Redare audio pornită", Toast.LENGTH_SHORT).show()
        return START_STICKY // Serviciul va fi repornit dacă este omorât de sistem [cite: 61, 212]
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.stop() // [cite: 67]
            mediaPlayer.release() // [cite: 218]
        }
        Toast.makeText(this, "Redare audio oprită", Toast.LENGTH_SHORT).show()
    }

    override fun onBind(intent: Intent?): IBinder? = null // [cite: 74, 220]
}