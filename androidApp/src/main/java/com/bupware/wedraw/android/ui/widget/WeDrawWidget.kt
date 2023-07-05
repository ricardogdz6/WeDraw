package com.bupware.wedraw.android.ui.widget

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.Column
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.unit.ColorProvider
import androidx.lifecycle.LiveData
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.data.WDDatabase
import com.bupware.wedraw.android.data.tables.image.Image
import com.bupware.wedraw.android.data.tables.image.ImageRepository
import kotlinx.coroutines.runBlocking

class WeDrawWidget : GlanceAppWidget() {
    @Composable
    override fun Content() {
        GlanceTheme {
            val context = LocalContext.current
            Box(
                modifier = GlanceModifier.fillMaxSize()
                    .background(GlanceTheme.colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {

            }
        }
    }


}


