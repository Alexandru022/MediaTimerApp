package com.example.soundservice

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper

class TimerService : Service() {
    private var seconds = 0
    private val handler = Handler(Looper.getMainLooper()) // [cite: 199, 260]
    private lateinit var runnable: Runnable

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        seconds = 0
        runnable = object : Runnable {
            override fun run() {
                seconds++
                // Trimiterea timpului către MainActivity prin Broadcast
                val intent = Intent("TIMER_UPDATED")
                intent.putExtra("time", seconds)
                sendBroadcast(intent)

                handler.postDelayed(this, 1000) // Execuție la fiecare secundă
            }
        }
        handler.post(runnable)
        return START_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}