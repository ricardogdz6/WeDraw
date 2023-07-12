package com.bupware.wedraw.android.ui.chatScreen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.launch
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

    fun loadMessages(groupId:Long){
        try {
            messageList = DataHandler.messageList[groupId]!!
        }catch (e:Exception){
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

                viewModelScope.launch {
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
                    DataHandler(context).saveMessage(idGroup = groupId,message = Message(
                        id = idNewMessage!!,
                        text = text,
                        timeZone = TimeZone.getDefault(),
                        senderId = userID,
                        imageId = null,
                        groupId = groupId,
                        date = Date() //TODO Está bien así?
                    ))

                }

                writingMessage = ""

                moveLazyToBottom = true
                //TODO

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

