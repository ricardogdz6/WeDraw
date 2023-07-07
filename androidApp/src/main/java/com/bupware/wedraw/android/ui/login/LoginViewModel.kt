package com.bupware.wedraw.android.ui.login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.bupware.wedraw.android.data.WDDatabase
import com.bupware.wedraw.android.data.tables.image.Image
import com.bupware.wedraw.android.data.tables.image.ImageRepository
import com.bupware.wedraw.android.data.tables.user.UserRepository

import com.bupware.wedraw.android.logic.firebase.FBAuth
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var gameList by savedStateHandle.saveable { mutableStateOf("") }


//    var imageUri by savedStateHandle.saveable { mutableStateOf("") }

//    var loading by savedStateHandle.saveable { mutableStateOf(true) }
    fun signInWithGoogleCredential(credential: AuthCredential, returningLambda: () -> Unit) =
        viewModelScope.launch {
            try {
                FBAuth.auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i("wawa", "god")
                            returningLambda()
                        }
                    }
                    .addOnFailureListener {
                        Log.i("wawa", "sadge")
                    }
            } catch (e: Exception) {
                Log.i("wawa", e.stackTraceToString())
            }
        }

//    fun readAllData(context: Context) = viewModelScope.launch {
//        val readAllData: LiveData<List<Image>>
//        val userDao = WDDatabase.getDatabase(context).imageDao()
//        val repository: ImageRepository = ImageRepository(userDao)
//        readAllData = repository.readAllData
//
//        readAllData.observeForever{
//                userList ->
//            userList.forEach{
//                Log.i("user", it.toString())
//                imageUri = it.uri
//            }
//            loading = false
//        }
//    }
    //pruebas

    /***

     *

    fun insertData(context: Context, user: User) = viewModelScope.launch {
    val userDao = UserDatabase.getDatabase(context).userDao()
    val repository: UserRepository = UserRepository(userDao)
    repository.insert(user)
    }
     */


}