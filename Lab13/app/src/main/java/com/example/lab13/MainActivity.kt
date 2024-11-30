package com.example.lab13

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // BroadcastReceiver 使用 lateinit 初始化，避免重複聲明變數
    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 設定視窗邊距
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 初始化 BroadcastReceiver
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                // 更新 TextView 內容
                val message = intent.extras?.getString("msg")
                findViewById<TextView>(R.id.tvMsg).text = message
            }
        }

        // 設定按鈕的點擊事件，統一處理按鈕對應的頻道
        val channels = mapOf(
            R.id.btnMusic to "music",
            R.id.btnNew to "new",
            R.id.btnSport to "sport"
        )
        channels.forEach { (buttonId, channel) ->
            findViewById<Button>(buttonId).setOnClickListener {
                registerChannel(channel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解除註冊廣播接收器
        unregisterReceiver(receiver)
    }

    private fun registerChannel(channel: String) {
        // 註冊廣播接收器
        val intentFilter = IntentFilter(channel)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, intentFilter, RECEIVER_EXPORTED)
        } else {
            registerReceiver(receiver, intentFilter)
        }

        // 啟動服務並傳遞頻道資訊
        Intent(this, MyService::class.java).apply {
            putExtra("channel", channel)
            startService(this)
        }
    }
}
