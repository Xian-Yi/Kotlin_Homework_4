package com.example.lab13

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyService : Service() {

    private var channel: String = ""
    private var thread: Thread? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 解析 Intent 中的頻道名稱
        channel = intent?.getStringExtra("channel").orEmpty()

        // 發送首次廣播訊息
        sendBroadcastMessage(
            when (channel) {
                "music" -> "歡迎來到音樂頻道"
                "new" -> "歡迎來到新聞頻道"
                "sport" -> "歡迎來到體育頻道"
                else -> "頻道錯誤"
            }
        )

        // 如果有正在執行的 Thread，則中斷它
        thread?.takeIf { it.isAlive }?.interrupt()

        // 初始化新的 Thread
        thread = Thread {
            try {
                Thread.sleep(3000) // 延遲三秒
                sendBroadcastMessage(
                    when (channel) {
                        "music" -> "即將播放本月TOP10音樂"
                        "new" -> "即將為您提供獨家新聞"
                        "sport" -> "即將播報本週NBA賽事"
                        else -> "頻道錯誤"
                    }
                )
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.apply { start() } // 啟動執行緒

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    // 發送廣播訊息的輔助函式
    private fun sendBroadcastMessage(message: String) {
        val intent = Intent(channel).apply {
            putExtra("msg", message)
        }
        sendBroadcast(intent)
    }
}
