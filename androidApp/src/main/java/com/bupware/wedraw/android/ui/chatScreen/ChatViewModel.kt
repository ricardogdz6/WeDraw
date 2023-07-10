package com.bupware.wedraw.android.ui.chatScreen

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.components.composables.SnackbarManager
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.retrofit.repository.MessageRepository
import com.bupware.wedraw.android.logic.sessionData.sessionData
import com.checkinapp.ui.theme.redWrong
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var switchDrawingStatus by savedStateHandle.saveable { mutableStateOf(false) }
    var writingMessage by savedStateHandle.saveable { mutableStateOf("") }

    var moveLazyToBottom by savedStateHandle.saveable { mutableStateOf(true) }

    var groupId = 0
    var userID: String = Firebase.auth.currentUser?.uid.toString()

    var messageList by savedStateHandle.saveable { mutableStateOf(listOf<Message>()) }

    fun loadMessages(groupId:Int){
        messageList = sessionData.messageList.first { it.first == groupId }.second
    }

    fun sendMessage(context: Context){

        if (writingMessage.length >= 1000){
            SnackbarManager.newSnackbar(context.getString(R.string.el_mensaje_no_puede_superar_los_1000_car_cteres), redWrong)
        } else {

            if (writingMessage.isNotBlank()) {

                addMessageLocal()

                viewModelScope.launch {
                    if (MessageRepository.createMessage(
                            Message(
                                id = null,
                                text = writingMessage,
                                timeZone = TimeZone.getDefault(),
                                senderId = userID,
                                groupId = groupId,
                                date = null
                            )
                        )
                    ) {
                        //TODO()
                    }
                }

                writingMessage = ""

                moveLazyToBottom = true
                //TODO

            }
        }
    }


    fun addMessageLocal(){
        //TODO GUARDAR ESTO EN ROOM QUE SOLO ESTÁ EN MEMORIA AHORA MISMO
        val newMessage = Message(id = null, text = writingMessage, timeZone = TimeZone.getDefault(), senderId =userID ,groupId =groupId,date = Date())
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

