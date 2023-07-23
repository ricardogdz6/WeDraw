package com.bupware.wedraw.android.ui.chatScreen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.components.composables.SnackbarManager
import com.bupware.wedraw.android.logic.dataHandler.DataHandler
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.retrofit.repository.MessageRepository
import com.bupware.wedraw.android.theme.redWrong
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
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

    var groupId = 0L
    var userID: String = Firebase.auth.currentUser?.uid.toString()

    var messageList by savedStateHandle.saveable { mutableStateOf(listOf<Message>()) }

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

    fun loadMessages(groupId:Long){
        try {
            messageList = DataHandler.messageList[groupId]!!.sortedBy { it.date }
        }catch (e:Exception){
            Log.i("chat",e.stackTraceToString())
            messageList = emptyList()
            //Se acaba de unir al grupo y por tanto no hay historial
        }
    }

    fun sendMessage(text:String,context: Context){

        if (text.length >= 1000){
            SnackbarManager.newSnackbar(context.getString(R.string.el_mensaje_no_puede_superar_los_1000_car_cteres), redWrong)
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
                                imageId = null
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
                                date = Date()
                            )
                        )
                    }
                }

                writingMessage = ""

                moveLazyToBottom = true
            }
        }
    }


    fun addMessageLocal(){
        val newMessage = Message(id = null, text = writingMessage, timeZone = TimeZone.getDefault(), senderId =userID ,groupId =groupId, imageId = null,date = Date())
        val oldList = messageList.toMutableList()
        oldList.add(newMessage)

        messageList = emptyList()
        messageList = oldList
    }


}

fun obtenerHoraMinuto(date: Date): String {
    val sdf = SimpleDateFormat("HH:mm")
    return sdf.format(date)
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

