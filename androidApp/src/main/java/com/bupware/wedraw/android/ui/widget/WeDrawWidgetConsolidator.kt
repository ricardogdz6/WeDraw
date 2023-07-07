package com.bupware.wedraw.android.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.lifecycle.LiveData
import com.bupware.wedraw.android.data.WDDatabase
import com.bupware.wedraw.android.data.tables.image.Image
import com.bupware.wedraw.android.data.tables.image.ImageRepository

class WeDrawWidgetConsolidator : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    companion object {
        private val SMALL_BOX = DpSize(90.dp, 90.dp)
        private val BIG_BOX = DpSize(180.dp, 180.dp)
        private val VERY_BIG_BOX = DpSize(300.dp, 300.dp)
        private val ROW = DpSize(180.dp, 48.dp)
        private val LARGE_ROW = DpSize(300.dp, 48.dp)
        private val COLUMN = DpSize(48.dp, 180.dp)
        private val LARGE_COLUMN = DpSize(48.dp, 300.dp)
    }

    override val sizeMode: SizeMode
        get() = SizeMode.Responsive(
            setOf(
                SMALL_BOX,
                BIG_BOX,
                VERY_BIG_BOX,
                ROW,
                LARGE_ROW,
                COLUMN,
                LARGE_COLUMN
            )
        )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val imageDao = WDDatabase.getDatabase(context).imageDao()
        val repository: ImageRepository = ImageRepository(imageDao = imageDao)
        val drawingImage = repository.getDrawingImage(0)

        provideContent {
            val context = LocalContext.current
            var bitmap: Bitmap? = null
            val uri = drawingImage


            if (drawingImage != null) {
                val uri = drawingImage.uri
                val uriParse = Uri.parse(uri)
                Log.i("ARM", "uri: $uri")
                val inputStream = context.contentResolver.openInputStream(uriParse)
                bitmap = BitmapFactory.decodeStream(inputStream)
            }



            GlanceTheme {
                if (bitmap != null) {
                    val size = LocalSize.current
                    when (size) {
                        SMALL_BOX->{
                            Text(text ="SMALL_BOX")}
                        BIG_BOX->{
                            Text(text ="BIG_BOX", style = TextStyle(color = GlanceTheme.colors.errorContainer))
                            WeDrawWidgetUI(Bitmap = bitmap)
                        }
                        VERY_BIG_BOX-> {
                            Text(text = "VERY_BIG_BOX", style = TextStyle(color = GlanceTheme.colors.errorContainer))
                        }
                        ROW-> {
                            Text(text = "ROW", style = TextStyle(color = GlanceTheme.colors.errorContainer))
                        }
                        LARGE_ROW-> {
                            Text(text = "LARGE_ROW", style = TextStyle(color = GlanceTheme.colors.errorContainer))
                        }
                        COLUMN-> {
                            Text(text = "COLUMN", style = TextStyle(color = GlanceTheme.colors.errorContainer))
                        }
                        LARGE_COLUMN-> {
                            Text(text = "LARGE_COLUMN", style = TextStyle(color = GlanceTheme.colors.errorContainer))
                        }
                    }

                    }

                }
            }


        }
    }


