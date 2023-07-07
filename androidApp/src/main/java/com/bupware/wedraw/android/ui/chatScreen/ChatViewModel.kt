package com.bupware.wedraw.android.ui.chatScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var switchDrawingStatus by savedStateHandle.saveable { mutableStateOf(false) }
    var writingMessage by savedStateHandle.saveable { mutableStateOf("") }

    var userID: String = Firebase.auth.currentUser?.uid.toString()

    val messageList by savedStateHandle.saveable { mutableStateOf(listOf<MessageLocal>(
        MessageLocal(
            id = null,
            text = "aaaa",
            date = Date(),
            senderId = userID,
            hostMessage = true
        ),
        MessageLocal(
            id = null,
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam id cursus tortor. Nulla sed pharetra metus. Mauris feugiat tempus quam, quis ultrices purus euismod sit amet. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer maximus lacinia est, in tristique lectus consectetur in. Suspendisse p",
            date = Date(),
            senderId = userID,
            hostMessage = true
        ),
        MessageLocal(
            id = null,
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam id cursus tortor. Nulla sed pharetra metus. Mauris feugiat tempus quam, quis ultrices purus euismod sit amet. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer maximus lacinia est, in tristique lectus consectetur in. Suspendisse p",
            date = Date(),
            senderId = "efasfsefsef",
            hostMessage = false
        ),
        MessageLocal(
            id = null,
            text = "FSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF JFSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF J",
            date = Date(),
            senderId = "efasfsefsef",
            hostMessage = false
        ),
        MessageLocal(
            id = null,
            text = "aaaa",
            date = Date(),
            senderId = userID,
            hostMessage = true
        ),
        MessageLocal(
            id = null,
            text = "FSO0'EFOPSE EIOPFJSEIOPFJE SPIOFJ SEIOPF J",
            date = Date(),
            senderId = "34353",
            hostMessage = false
        ),
    )) }

    /*
    fun addMessage(){
        val oldList = messageList
        messageList =
    }
     */
}

data class MessageLocal(
    var id:Int?,
    val text: String,
    var date: Date,
    var senderId:String,
    var hostMessage: Boolean = false
): java.io.Serializable
