package com.choque.authcares2.alerts

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback

class EmergencyAlertActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = Unit
        })

        showCurrentAlert()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        showCurrentAlert()
    }

    private fun showCurrentAlert() {
        val title = intent.getStringExtra(CriticalAlertNotifier.EXTRA_TITLE)
            ?: "¡Alerta crítica!"
        val message = intent.getStringExtra(CriticalAlertNotifier.EXTRA_MESSAGE)
            ?: "Revisa al niño inmediatamente."
        setContentView(createAlertView(title, message))
    }

    private fun createAlertView(title: String, message: String): LinearLayout {
        val padding = (32 * resources.displayMetrics.density).toInt()

        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(padding, padding, padding, padding)
            setBackgroundColor(Color.rgb(150, 0, 0))

            addView(TextView(context).apply {
                text = "⚠"
                textSize = 72f
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
            })

            addView(TextView(context).apply {
                text = title
                textSize = 30f
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            }, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            addView(TextView(context).apply {
                text = message
                textSize = 20f
                gravity = Gravity.CENTER
                setTextColor(Color.WHITE)
                setPadding(0, padding, 0, padding)
            }, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            addView(Button(context).apply {
                text = "ENTENDIDO, DETENER ALERTA"
                textSize = 16f
                setOnClickListener {
                    stopService(Intent(this@EmergencyAlertActivity, EmergencyAlarmService::class.java))
                    CriticalAlertNotifier.dismiss(this@EmergencyAlertActivity)
                    finishAndRemoveTask()
                }
            }, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }
}
