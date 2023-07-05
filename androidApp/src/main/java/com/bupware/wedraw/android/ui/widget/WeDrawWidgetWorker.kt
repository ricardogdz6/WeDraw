package com.bupware.wedraw.android.ui.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.bupware.wedraw.android.data.WDDatabase
import com.bupware.wedraw.android.data.tables.image.ImageRepository

class WeDrawWidgetWorker(
    private val appContext: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(appContext, workerParameters) {

    private val imageRepository = WDDatabase.getDatabase(appContext).imageDao()
    private val repository = ImageRepository(imageRepository)

    override suspend fun doWork(): Result {



        val glanceId = GlanceAppWidgetManager(appContext).getGlanceIds(WeDrawWidget::class.java).firstOrNull()
        println("ARM: glanceId: $glanceId")


        if (glanceId == null) {
            return Result.failure(
                Data
                    .Builder()
                    .putString("FAILURE_REASON", "NULL_GLANCE_ID")
                    .build(),
            )
        }
        val result = repository.getDrawingImage(1)


    }

}