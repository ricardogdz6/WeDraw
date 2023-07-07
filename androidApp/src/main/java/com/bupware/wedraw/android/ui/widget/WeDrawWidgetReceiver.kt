package com.bupware.wedraw.android.ui.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.bupware.wedraw.android.core.imageService.ImageService
import com.bupware.wedraw.android.ui.widget.callback.WeDrawWidgetCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WeDrawWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = WeDrawWidget()

    private val coroutineScope = MainScope()

    @Inject
    lateinit var imageService: ImageService

    companion object {
        val imageUri = stringPreferencesKey("imagenBitmap")


    }

    private fun observeData(context: Context) {
        coroutineScope.launch {
            val glanceId =
                GlanceAppWidgetManager(context).getGlanceIds(WeDrawWidget::class.java).firstOrNull()

            glanceId?.let { glanceId ->
                updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { pref ->
                    val imageUri = imageService.getDrawingImage(context)
                    pref.toMutablePreferences().apply {
                        Log.i("ARM", "imageUri Observe: $imageUri")
                        this[WeDrawWidgetReceiver.imageUri] = imageUri
                    }
                }

                glanceAppWidget.update(context, glanceId)
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        observeData(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == WeDrawWidgetCallback.UPDATE_ACTION) {
            observeData(context)
        }
    }
}