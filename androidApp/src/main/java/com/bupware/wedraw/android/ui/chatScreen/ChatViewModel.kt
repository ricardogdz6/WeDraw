package com.bupware.wedraw.android.ui.chatScreen

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.components.composables.SnackbarManager
import com.bupware.wedraw.android.logic.dataHandler.DataHandler
import com.bupware.wedraw.android.logic.dataHandler.DataHandler.Companion.bitmapToBlob
import com.bupware.wedraw.android.logic.models.Image
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.retrofit.repository.MessageRepository
import com.bupware.wedraw.android.roomData.WDDatabase
import com.bupware.wedraw.android.theme.redWrong
import com.bupware.wedraw.android.ui.drawingScreen.convertImageBitmapToBitmap
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var switchDrawingStatus by savedStateHandle.saveable { mutableStateOf(false) }
    var writingMessage by savedStateHandle.saveable { mutableStateOf("") }

    var moveLazyToBottom by savedStateHandle.saveable { mutableStateOf(true) }
    var exportDrawing by savedStateHandle.saveable { mutableStateOf(false) }

    var groupId = 0L
    var userID: String = Firebase.auth.currentUser?.uid.toString()

    var messageList by savedStateHandle.saveable { mutableStateOf(listOf<Message>()) }
    var messageUrisList by savedStateHandle.saveable { mutableStateOf(mutableMapOf<Long,Uri>()) }

    val colorsAvailable by savedStateHandle.saveable { mutableStateOf(listOf<Color>(
        Color.Red,
        Color.Blue,
        Color(0xFF00E1FF),
        Color.Yellow,
        Color(0xFFF789FF),
        Color(0xFF000000)
    )) }


    //MORE COLORS
    var controllerColor by savedStateHandle.saveable { mutableStateOf(Color.Black) }
    var colorWheelShow by savedStateHandle.saveable { mutableStateOf(false) }

    //TOOLS
    var drawState by savedStateHandle.saveable { mutableStateOf(true) }
    var eraseState by savedStateHandle.saveable { mutableStateOf(false) }
    var sizeState by savedStateHandle.saveable { mutableStateOf(1) }

    var sendConfirmation by savedStateHandle.saveable { mutableStateOf(false) }
    var removeCanva by savedStateHandle.saveable { mutableStateOf(false) }

    fun loadMessages(groupId:Long, context: Context){
        try {
            messageList = DataHandler.messageList[groupId]!!.sortedBy { it.date }
        }catch (e:Exception){
            Log.i("chat",e.stackTraceToString())
            messageList = emptyList()
            //Se acaba de unir al grupo y por tanto no hay historial
        }

        try {
            messageUrisList = DataHandler.uriList[groupId]!!.toMutableMap()
        }catch (e:Exception){
            Log.i("chat",e.stackTraceToString())
            messageUrisList = emptyMap<Long,Uri>().toMutableMap()
            //Se acaba de unir al grupo y por tanto no hay historial
        }
    }

    fun sendMessage(text: String, context: Context, image: Bitmap? = null) {


        if (text.length >= 1000) {
            SnackbarManager.newSnackbar(
                context.getString(R.string.el_mensaje_no_puede_superar_los_1000_car_cteres),
                redWrong
            )
        } else {

            if (text.isNotBlank()) {

                //Añado el mensaje a este viewModel para que aparezca instantaneamente
                //Además, guardo en memoria y local el mensaje con el id returneado de la API
                addMessageLocal()

                GlobalScope.launch(Dispatchers.Default) {
                    withContext(Dispatchers.IO) {
                        val idNewMessage = MessageRepository.createMessage(
                            Message(
                                id = null,
                                text = text,
                                timeZone = TimeZone.getDefault(),
                                senderId = userID,
                                groupId = groupId,
                                date = null,
                                imageId = null,
                                bitmap = null
                            )
                        )

                        //RECIBO EL ID DEL MESSAGE Y LO MANDO
                        DataHandler(context).saveMessage(
                            idGroup = groupId, message = Message(
                                id = idNewMessage,
                                text = text,
                                timeZone = TimeZone.getDefault(),
                                senderId = userID,
                                imageId = null,
                                groupId = groupId,
                                date = Date(),
                                bitmap = null
                            ), uri = null, bitmap = null
                        )
                    }
                }

                writingMessage = ""

                moveLazyToBottom = true
            }
        }


    }

    fun sendMessageWithImage(text:String,imageId:Long,uri: Uri,context: Context, image: Bitmap){

        addMessageLocal(image.asImageBitmap())
        moveLazyToBottom = true

        attemptSendMessage(text = text, imageId = imageId, context = context, uri = uri, bitmap = image)
    }


    fun addMessageLocal(image: ImageBitmap? = null){
        val newMessage = Message(id = null, text = if (image != null) "" else writingMessage, timeZone = TimeZone.getDefault(), senderId =userID ,groupId =groupId, imageId = null,date = Date(), bitmap = image)
        val oldList = messageList.toMutableList()
        oldList.add(newMessage)

        messageList = emptyList()
        messageList = oldList
    }

    fun attemptSendMessage(text: String,imageId: Long, context: Context, uri: Uri, bitmap: Bitmap){
        GlobalScope.launch(Dispatchers.Default) {

            withContext(Dispatchers.IO) {
                val idNewMessage = MessageRepository.createMessage(
                    Message(
                        id = null,
                        text = text,
                        timeZone = TimeZone.getDefault(),
                        senderId = userID,
                        groupId = groupId,
                        date = null,
                        imageId = imageId,
                        bitmap = null
                    )
                )

                if (idNewMessage == null){
                    DataHandler(context).saveFailedMessageWithImage(message = Message(
                       id= null,
                       text= "",
                       timeZone= TimeZone.getDefault(),
                       senderId= userID,
                       imageId= imageId,
                       groupId= groupId,
                       date= Date(),
                        bitmap = null
                    ),uri = uri, bitmap = bitmap, idGroup = groupId)
                }
                else {
                    //RECIBO EL ID DEL MESSAGE Y LO MANDO
                    DataHandler(context).saveMessage(
                        idGroup = groupId, message = Message(
                            id = idNewMessage,
                            text = text,
                            timeZone = TimeZone.getDefault(),
                            senderId = userID,
                            imageId = imageId ?: 0,
                            groupId = groupId,
                            date = Date(),
                            bitmap = null
                        ), uri = uri, bitmap = bitmap
                    )
                }


            }
        }
    }

    fun processDrawing(context: Context): (ImageBitmap?, Throwable?) -> Unit {
        return { imageBitmap, error ->

            if (imageBitmap != null) {
                if (hasColorOtherThanWhite(imageBitmap)) {

                    val bitmap = convertImageBitmapToBitmap(bitmap = imageBitmap)

                    viewModelScope.launch {
                        //Se guarda en local system y obtengo la uri
                        val uri = withContext(Dispatchers.IO) {
                            DataHandler(context).saveBitmapLocalOS(bitmap)
                        }

                        //Lo mandamos para obtener el id con el que guardarlo en local
                        val imageID = withContext(Dispatchers.IO) {
                            MessageRepository.createImage(
                                Image(
                                    id = null,
                                    bitmap = bitmapToBlob(bitmap)
                                )
                            )
                        }

                        if (imageID != null) {
                            viewModelScope.launch {

                                sendMessageWithImage(
                                    text = "",
                                    imageId = imageID,
                                    uri = uri,
                                    context = context,
                                    image = bitmap
                                )

                                //Lo guardo en memoria
                                DataHandler(context).saveMessageWithImageMemory(
                                    imageID = imageID,
                                    groupId = groupId,
                                    uri = uri.toString()
                                )

                                //Ahora lo guardo en Room local
                                DataHandler(context).saveMessageWithImageLocal(imageID, uri)
                            }

                        } else {
                            DataHandler(context).saveFailedMessageWithImage(
                                uri = uri, bitmap = bitmap, idGroup = groupId, message = Message(
                                    id = null,
                                    text = "",
                                    timeZone = TimeZone.getDefault(),
                                    senderId = userID,
                                    imageId = null,
                                    groupId = groupId,
                                    date = Date(),
                                    bitmap = null
                                )
                            )
                        }

                    }

                } else SnackbarManager.newSnackbar("No dejes el dibujo vacío", redWrong)
            }
        }
    }

}


fun isColorWhite(color: Color): Boolean {
    return color == Color.White
}
fun hasColorOtherThanWhite(imageBitmap: ImageBitmap): Boolean {
    val bitmap = imageBitmap.asAndroidBitmap()

    for (x in 0 until bitmap.width) {
        for (y in 0 until bitmap.height) {
            val pixelColor = bitmap.getPixel(x, y)
            if (!isColorWhite(Color(pixelColor))) {
                Log.i("wawa", "NO BLANCO: $x $y")
                return true
            }
        }
    }
    return false
}

fun hsvToColor(hue: Float, saturation: Float, value: Float): Color {
    val chroma = value * saturation
    val hue1 = hue / 60.0f
    val x = chroma * (1 - kotlin.math.abs(hue1 % 2 - 1))
    var red = 0f
    var green = 0f
    var blue = 0f

    when {
        hue1 >= 0 && hue1 < 1 -> {
            red = chroma
            green = x
        }
        hue1 >= 1 && hue1 < 2 -> {
            red = x
            green = chroma
        }
        hue1 >= 2 && hue1 < 3 -> {
            green = chroma
            blue = x
        }
        hue1 >= 3 && hue1 < 4 -> {
            green = x
            blue = chroma
        }
        hue1 >= 4 && hue1 < 5 -> {
            red = x
            blue = chroma
        }
        hue1 >= 5 && hue1 < 6 -> {
            red = chroma
            blue = x
        }
    }

    val m = value - chroma
    red += m
    green += m
    blue += m

    return Color(red, green, blue)
}

