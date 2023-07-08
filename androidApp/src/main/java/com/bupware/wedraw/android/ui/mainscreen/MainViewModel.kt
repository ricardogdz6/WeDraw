package com.bupware.wedraw.android.ui.mainscreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.logic.retrofit.repository.UserRepository
import com.bupware.wedraw.android.logic.sessionData.sessionData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var moreOptionsEnabled by savedStateHandle.saveable { mutableStateOf(false) }
    var showGroups by savedStateHandle.saveable { mutableStateOf(false) }
    var showSettings by savedStateHandle.saveable { mutableStateOf(false) }

    //Settings Menu
    var expandJoinGroup by savedStateHandle.saveable { mutableStateOf(false) }
    var expandCreateGroup by savedStateHandle.saveable { mutableStateOf(false) }

    var groupName by savedStateHandle.saveable { mutableStateOf("") }
    var joinCode by savedStateHandle.saveable { mutableStateOf("") }

    //Init
    var askForUsername by savedStateHandle.saveable { mutableStateOf(false) }

    //Username
    var username by savedStateHandle.saveable { mutableStateOf("") }

    init {
        viewModelScope.launch {
            gestionLogin { askForUsername = !askForUsername }
        }

    }
    fun expandButton(index:Int){

        when(index){

            1 -> {
                expandCreateGroup = !expandCreateGroup
                expandJoinGroup = false
            }

            2 -> {
                expandCreateGroup = false
                expandJoinGroup = !expandJoinGroup
            }

        }

    }

    fun launchUpdateUsername(){
        viewModelScope.launch {
            if (updateUsername(username)) askForUsername = !askForUsername
            else {Log.i("wawa","username existe")}/*TODO aqui lanzar un mensaje de error y arriba tambien uno de exito*/
        }
    }


}

suspend fun gestionLogin(askForUsername: () -> Unit){

    val userEmail = Firebase.auth.currentUser?.email.toString()

    //Primero obtengo la información de la sesión en la BBDD
    val user = withContext(Dispatchers.Default) { UserRepository.getUserByEmail(userEmail)?.firstOrNull() }

    if (user == null){
        //Si no existe creamos el usuario
        withContext(Dispatchers.Default) {UserRepository.createUser(User(
            id = Firebase.auth.currentUser?.uid,
            email = userEmail,
            premium = false,
            username = "",
            expireDate = null
        ))}
        //Acto seguido preguntamos por el username
        askForUsername()

    } else {
        //Si existe pero no tiene campo username, le pedimos que ponga un username
        if (user.username!!.isEmpty())
            askForUsername()
        else sessionData.user = user
    }

}

suspend fun updateUsername(newUsername:String):Boolean{

    var usernameExists = withContext(Dispatchers.Default) { UserRepository.getUserByUsername(newUsername)?.firstOrNull() }

    if (usernameExists != null){
        return false
    } else {

        val userEmail = Firebase.auth.currentUser?.email.toString()
        var user = withContext(Dispatchers.Default) {
            UserRepository.getUserByEmail(userEmail)?.firstOrNull()
        }
        user!!.username = newUsername

        if (user != null) {
            withContext(Dispatchers.Default) {
                UserRepository.updateUser(
                    email = userEmail,
                    user = user
                )
            }
            sessionData.user = user
            return true
        } else {
            Log.e("Error", "Usuario no existe, no puede actualizarse")
            return false
        }
    }

}

