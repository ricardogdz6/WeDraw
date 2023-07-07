package com.bupware.wedraw.android.ui.widget

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.theme.Lexend

class WeDrawWidget : GlanceAppWidget() {
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

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val prefs = currentState<Preferences>()
        val imageUri = prefs[WeDrawWidgetReceiver.imageUri]
        var loading by remember { mutableStateOf(true) }
        if (imageUri == null) {
            Log.i("ARM", "imageUri is null")
        } else {
            loading = false
        }

        if (!loading) {
            val uri = Uri.parse(imageUri)
            Log.i("ARM", "uri: $uri")
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)


            var width = bitmap.width
            var height = bitmap.height
            Log.i("ARM", "width: $width")
            Log.i("ARM", "height: $height")


            // Resto del código que utiliza el objeto Uri
            GlanceTheme {


                Box(
                    modifier = GlanceModifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                }

                Column(modifier = GlanceModifier.fillMaxSize()) {
                    Box(modifier = GlanceModifier) {

                        Image(
                            provider = ImageProvider(bitmap),
                            contentDescription = "",
                            modifier = GlanceModifier.fillMaxSize()
                        )
                        Text(
                            "BUPWARE",
                            modifier = GlanceModifier.padding(15.dp),
                            style = TextStyle(
                                color = GlanceTheme.colors.textColorPrimary,
                                fontSize = 20.sp,
                            )

                        )
                    }

                }
            }
        } else {
            CircularProgressIndicator()
        }




        when (LocalSize.current) {
            SMALL_BOX -> {
                Log.i("ARM", "SMALL_BOX")

            }

            BIG_BOX -> {
                Log.i("ARM", "BIG_BOX")
            }

            VERY_BIG_BOX -> {
                Log.i("ARM", "VERY_BIG_BOX")
            }

            ROW -> {
                Log.i("ARM", "ROW")
            }

            LARGE_ROW -> {
                Log.i("ARM", "LARGE_ROW")
            }

            COLUMN -> {
                Log.i("ARM", "COLUMN")
            }

            LARGE_COLUMN -> {
                Log.i("ARM", "LARGE_COLUMN")
            }
        }


    }
}




