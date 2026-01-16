package com.example.soundservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var tvTimer: TextView

    // Receptor pentru a capta datele de la TimerService în timp real
    private val timerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val totalSeconds = intent?.getIntExtra("time", 0) ?: 0
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            // Actualizarea vizuală a cronometrului
            tvTimer.text = String.format("%02d:%02d", minutes, seconds)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTimer = findViewById(R.id.tvTimerValue)

        // Pornire/Oprire Muzică [cite: 26-38]
        findViewById<Button>(R.id.button1).setOnClickListener {
            startService(Intent(this, SoundService::class.java)) // [cite: 33, 188]
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            stopService(Intent(this, SoundService::class.java)) // [cite: 37]
        }

        // Pornire/Oprire Cronometru
        findViewById<Button>(R.id.btnStartTimer).setOnClickListener {
            startService(Intent(this, TimerService::class.java))
        }
        findViewById<Button>(R.id.btnStopTimer).setOnClickListener {
            stopService(Intent(this, TimerService::class.java))
            tvTimer.text = "00:00"
        }
    }

    override fun onResume() {
        super.onResume()
        // Înregistrarea receptorului pentru actualizarea UI-ului
        val filter = IntentFilter("TIMER_UPDATED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(timerReceiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(timerReceiver, filter)
        }
    }

    override fun onPause() {
        super.onPause()
        // Dezactivarea receptorului pentru a evita scurgerile de memorie
        unregisterReceiver(timerReceiver)
    }
}