package com.bupware.wedraw.android.ui.widget
import com.bupware.wedraw.android.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.*
import androidx.glance.layout.Alignment
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.Column
import androidx.glance.text.Text

object WeDrawWidget : GlanceAppWidget() {
    @Composable
    override fun Content() {
        Column(
            modifier = GlanceModifier.fillMaxSize().background(Color.DarkGray),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(text = LocalContext.current.getString(R.string.stringholder))
        }

    }
}


class WeDrawWidgetReciver() : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = WeDrawWidget
}
