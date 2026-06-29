package com.choque.authcares2.features.stats.share

import android.app.Activity
import android.content.Context
import android.content.Intent

object AndroidSummarySharer {

    fun share(context: Context, summary: String) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, summary)
        }
        val chooser = Intent.createChooser(sendIntent, "Compartir resumen")
        if (context !is Activity) {
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }
}
