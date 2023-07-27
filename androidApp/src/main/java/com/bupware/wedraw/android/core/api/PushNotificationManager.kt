package com.bupware.wedraw.android.core.api

import android.util.Log
import com.bupware.wedraw.android.core.utils.Converter
import com.bupware.wedraw.android.logic.dataHandler.DataHandler
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.retrofit.repository.MessageRepository as MessageRepositoryRetrofit
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PushNotificationManager : FirebaseMessagingService() {


    @Override
    override fun onMessageReceived(message: RemoteMessage) {

        //Log incoming message
        Log.v("CloudMessage", "From ${message.from}")

        //Log Data Payload
        if (message.data.isNotEmpty()) {
            Log.v("CloudMessage", "Message Data ${message.data}")
        }

        //Check if message contains a notification payload

        message.data.let {
            Log.v("CloudMessage", "Message Data Body ${it["body"]}")
            Log.v("CloudMessage", "Message Data Title  ${it["title"]}")
            //when app in forground that notification is not shown on status bar
            //lets write a code to display notification in status bar when app in forground.
            //showNotificationOnStatusBar(it)
        }

        if (message.notification != null) {

            Log.v("CloudMessage", "Notification ${message.notification}")
            Log.v("CloudMessage", "Notification Title ${message.notification!!.title}")
            Log.v("CloudMessage", "Notification Body ${message.notification!!.body}")

        }

        val context = applicationContext

        CoroutineScope(Dispatchers.IO).launch {

            val imageId = if (message.data["imageId"].toString() == "null") null else message.data["imageId"]!!.toLong()

            DataHandler(context).saveMessage(
                idGroup = message.data["groupId"]!!.toLong()
                ,message = Message(
                    id = message.data["id"]!!.toLong(),
                    text = message.data["text"].toString(),
                    timeZone = null,
                    senderId = message.data["senderId"].toString(),
                    imageId = imageId,
                    groupId = message.data["groupId"]!!.toLong(),
                    date = Converter.parseDate(message.data["date"].toString()),
                    bitmap = null
                ), uri = null, bitmap = null
            )

            //Si es un mensaje con imagen se pide la imagen para descargar a la BBDD
            if (imageId != null) {
                val byteArray = withContext(Dispatchers.IO) {MessageRepositoryRetrofit.getImage(imageId)}
                val bitmap = DataHandler.blobToBitmap(byteArray!!)
                val uri = DataHandler(context).saveBitmapLocalOS(bitmap)
                DataHandler(context).saveBitmapLocal(imageId!!,uri)
            }

            DataHandler.forceMessagesUpdate.value = true

        }

    }


    @Override
    override fun onNewToken(token: String) {
        //Log.i("wawa", "FCM token: $token")

    }
}