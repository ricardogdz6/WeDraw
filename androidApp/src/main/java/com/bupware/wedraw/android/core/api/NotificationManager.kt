package com.bupware.wedraw.android.core.api

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.action.actionRunCallback
import com.bupware.wedraw.android.logic.dataHandler.DataUtils
import com.bupware.wedraw.android.ui.widget.callback.WDrawRefreshCallback
import com.bupware.wedraw.android.ui.widget.callback.WDrawReverseLetterCallback

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationManager : FirebaseMessagingService() {


    @Override
    override fun onMessageReceived(message: RemoteMessage) {
        val messageBody = message.data
        //Log.i("wawa", "Message body: $messageBody")

//        actionRunCallback(WDrawReverseLetterCallback::class.java)

    }


    @Override
    override fun onNewToken(token: String) {
        //Log.i("wawa", "FCM token: $token")

    }
}