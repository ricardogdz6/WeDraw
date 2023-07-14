package com.bupware.wedraw.android.ui.login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.bupware.wedraw.android.logic.dataHandler.DataUtils
import com.bupware.wedraw.android.logic.firebase.FBAuth
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var gameList by savedStateHandle.saveable { mutableStateOf("") }
    var initNContinue by savedStateHandle.saveable { mutableStateOf(false) }

    fun signInWithGoogleCredential(credential: AuthCredential, returningLambda:()->Unit) = viewModelScope.launch {
        try {
            FBAuth.auth.signInWithCredential(credential)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        Log.i("auth","Éxito iniciando sesión")
                        returningLambda()
                    }
                }
                .addOnFailureListener {
                    Log.i("auth","Inicio de sesión fallido")
                }


        }
        catch (e:Exception){
            Log.i("auth","Excepcion durante login")
        }
    }

    fun initData(context : Context){
        viewModelScope.launch {
            val dataUtils = DataUtils()
            Log.i("DataUtils","1")
            dataUtils.initData(context)
        }
    }

}