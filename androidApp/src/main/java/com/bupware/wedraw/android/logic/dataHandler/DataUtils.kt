package com.bupware.wedraw.android.logic.dataHandler

import android.content.Context
import android.util.Log
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.logic.retrofit.repository.GroupRepository
import com.bupware.wedraw.android.logic.retrofit.repository.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataUtils {

    companion object{

        /*TODO BORRAR?
        fun InitData(context: Context){

            CoroutineScope(Dispatchers.IO).launch {
                gestionLogin(context = context)
                DataHandler(context).loadGroups()
            }

        }

         */
        suspend fun getUserGroups():List<Group>{

            val userId = Firebase.auth.currentUser?.uid.toString()
            val group = withContext(Dispatchers.Default) { GroupRepository.getGroupByUserId(userId)} ?: emptyList()

            return group
        }


        suspend fun gestionLogin(askForUsername: () -> Unit, context: Context){

            val userEmail = Firebase.auth.currentUser?.email.toString()

            //Primero obtengo la información de la sesión en la BBDD
            val user = withContext(Dispatchers.Default) { UserRepository.getUserByEmail(userEmail)?.firstOrNull() }

            if (user != null) {
                DataHandler(context).saveUser(user)
            }

            if (user == null){
                //Si no existe creamos el usuario
                withContext(Dispatchers.Default) {
                    UserRepository.createUser(
                        User(
                            id = Firebase.auth.currentUser?.uid,
                            email = userEmail,
                            premium = false,
                            username = "",
                            expireDate = null
                        )
                    )}
                //Acto seguido preguntamos por el username
                askForUsername()

            } else {
                //Si existe pero no tiene campo username, le pedimos que ponga un username
                if (user.username!!.isEmpty())
                    askForUsername()
                else DataHandler.user = user
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
                    DataHandler.user = user
                    return true
                } else {
                    Log.e("Error", "Usuario no existe, no puede actualizarse")
                    return false
                }
            }

        }
    }

}