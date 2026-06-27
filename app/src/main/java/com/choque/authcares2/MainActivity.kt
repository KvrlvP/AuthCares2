package com.choque.authcares2

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.choque.authcares2.app.AuthCaresApp
import com.choque.authcares2.features.alerts.service.CriticalAlertNotifier
import com.choque.authcares2.ui.theme.AuthCares2Theme
class MainActivity : ComponentActivity() {
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) showFullScreenPermissionExplanationIfNeeded()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CriticalAlertNotifier.createNotificationChannel(this)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        setContent {
            AuthCares2Theme {
                AuthCaresApp()
            }
        }
        requestCriticalAlertPermissions()
    }

    private fun requestCriticalAlertPermissions() {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            showFullScreenPermissionExplanationIfNeeded()
        }
    }

    private fun showFullScreenPermissionExplanationIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) return

        val notificationManager = getSystemService(NotificationManager::class.java)
        if (notificationManager.canUseFullScreenIntent()) return

        val preferences = getSharedPreferences("critical_alert_permissions", MODE_PRIVATE)
        if (preferences.getBoolean("full_screen_explanation_shown", false)) return
        preferences.edit().putBoolean("full_screen_explanation_shown", true).apply()

        AlertDialog.Builder(this)
            .setTitle("Permitir alertas críticas")
            .setMessage(
                "Para mostrar una emergencia sobre la pantalla bloqueada o sobre otras apps, " +
                    "activa el permiso de alertas en pantalla completa para AuthCares."
            )
            .setNegativeButton("Ahora no", null)
            .setPositiveButton("Abrir configuración") { _, _ ->
                startActivity(
                    Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT).apply {
                        data = Uri.parse("package:$packageName")
                    }
                )
            }
            .show()
    }
}
