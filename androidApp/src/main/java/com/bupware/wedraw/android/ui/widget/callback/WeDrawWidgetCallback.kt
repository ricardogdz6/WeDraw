package com.bupware.wedraw.android.ui.widget.callback

import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.bupware.wedraw.android.ui.widget.WeDrawWidgetConsolidator
import com.bupware.wedraw.android.ui.widget.WeDrawWidgetReceiver

class WeDrawWidgetCallback : ActionCallback {


    companion object {
        const val UPDATE_ACTION = "updateAction"
    }

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val intent = Intent(context, WeDrawWidgetReceiver::class.java).apply {
            action = UPDATE_ACTION
        }
        context.sendBroadcast(intent)
    }
}

object WDrawReverseLetterCallback : ActionCallback {
    val IS_REVERSE = booleanPreferencesKey("isReverse")

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            if (prefs[IS_REVERSE] == null) prefs[IS_REVERSE] = true else prefs[IS_REVERSE] =
                !prefs[IS_REVERSE]!!

        }

        WeDrawWidgetConsolidator().update(context, glanceId)
    }
}

object WDrawUpdateUriCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        WeDrawWidgetConsolidator().update(context, glanceId)
    }
}

