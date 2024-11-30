package com.example.lab12

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.*

class MyService : Service() {

    private lateinit var job: Job

    override fun onCreate() {
        super.onCreate()
        // 建立一個 Job 來管理協程
        job = CoroutineScope(Dispatchers.Default).launch {
            try {
                delay(3000) // 延遲三秒
                // 宣告 Intent，從 MyService 啟動 SecActivity
                val intent = Intent(this@MyService, SecActivity::class.java).apply {
                    // 加入 Flag 表示要產生一個新的 Activity 實例
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 返回 START_NOT_STICKY 表示 Service 結束後不會重啟
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消協程以釋放資源
        if (::job.isInitialized) {
            job.cancel()
        }
    }

    // 綁定 Service 時調用，用於與 Activity 進行溝通
    override fun onBind(intent: Intent): IBinder? = null
}
