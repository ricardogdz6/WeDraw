package com.bupware.wedraw.android.logic.dataHandler

import android.content.Context
import android.util.Log
import com.bupware.wedraw.android.core.utils.Converter
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.logic.retrofit.repository.GroupRepository
import com.bupware.wedraw.android.logic.retrofit.repository.UserRepository
import com.bupware.wedraw.android.roomData.WDDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import com.bupware.wedraw.android.roomData.tables.group.GroupRepository as GroupRepositoryRoom
import com.bupware.wedraw.android.roomData.tables.group.Group as GroupRoom

import com.bupware.wedraw.android.roomData.tables.message.MessageRepository as MessageRepositoryRoom
import com.bupware.wedraw.android.roomData.tables.message.Message as MessageRoom
import com.bupware.wedraw.android.roomData.tables.user.User as UserRoom

class DataUtils {
    suspend fun initData(context: Context) {

        //primero localmente a memoria
        withContext(Dispatchers.Default) {

            DataHandler.groupList = Converter.converterGroupsEntityToGroupsList(
                getGroupsLocal(context) ?: emptyList()
            )
            Log.i("DataUtils", "initData: ${DataHandler.groupList}")
            DataHandler.userList =
                Converter.convertUsersEntityToUsersList(getUsersLocal(context) ?: emptySet())
            Log.i("DataUtils", "initData: ${DataHandler.userList}")

            DataHandler.messageList= getMapOfMessageByGroup(DataHandler.groupList, context).toMutableMap()
            Log.i("DataUtils", "initData: ${DataHandler.messageList}")


        }


//        withContext(Dispatchers.Default) {
//            DataHandler(context).saveGroups(getUserGroups(context))
//            getUsersAndSaveInLocal(context)
//        }
    }

    private suspend fun getMapOfMessageByGroup(group: Set<Group>, context: Context): Map<Long, MutableSet<Message>> {

        val map = mutableMapOf<Long, MutableSet<Message>>()

        group.forEach { group ->
            val groupId = group.id ?: 0L // Conversión explícita de Long? a Long
            val messages = getMessagesLocalByGroupId(context, groupId)
            map[groupId] = messages?.let { Converter.convertMessagesEntityToMessagesList(it).toMutableSet() }!!
        }

        return map
    }
    private suspend fun getMessagesLocalByGroupId(context: Context, idGroup: Long): Set<MessageRoom>? {

        val database = WDDatabase.getDatabase(context)
        val messageRepository =
            com.bupware.wedraw.android.roomData.tables.message.MessageRepository(database.messageDao())

        return messageRepository.getMessagesByGroupId(idGroup).first().toSet()
    }
    private suspend fun getGroupsLocal(context: Context): List<GroupRoom>? {

        val database = WDDatabase.getDatabase(context)
        val groupRepository =
            GroupRepositoryRoom(database.groupDao())

        return groupRepository.readAllData.first()
    }

    private suspend fun getUsersLocal(context: Context): Set<UserRoom>? {

        val database = WDDatabase.getDatabase(context)
        val userRepository =
            com.bupware.wedraw.android.roomData.tables.user.UserRepository(database.userDao())

        return userRepository.readAllData.first().toSet()
    }

    companion object {


        suspend fun gestionLogin(askForUsername: () -> Unit, context: Context) {

            val userEmail = Firebase.auth.currentUser?.email.toString()

            //Primero obtengo la información de la sesión en la BBDD
            val user = withContext(Dispatchers.Default) {
                UserRepository.getUserByEmail(userEmail)?.firstOrNull()
            }

            if (user != null) {
                DataHandler(context).saveUser(user)
            }

            if (user == null) {
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
                    )
                }
                //Acto seguido preguntamos por el username
                askForUsername()

            } else {
                //Si existe pero no tiene campo username, le pedimos que ponga un username
                if (user.username!!.isEmpty())
                    askForUsername()
                else DataHandler.user = user
            }

        }

        suspend fun updateUsername(newUsername: String): Boolean {

            var usernameExists = withContext(Dispatchers.Default) {
                UserRepository.getUserByUsername(newUsername)?.firstOrNull()
            }

            if (usernameExists != null) {
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

    private suspend fun getUserGroups(context: Context): List<Group> {
        val userId = Firebase.auth.currentUser?.uid.toString()
        Log.i("hilos", "getUserGroups")
        val group = withContext(Dispatchers.Default) {
            GroupRepository.getGroupByUserId(userId)
        } ?: emptyList()
        group.forEach { group ->
            group.userGroups?.forEach {
                Log.i(
                    "GROUPS",
                    it.userID.toString()
                )
            }
        }

        return group
    }

    private suspend fun getUsersAndSaveInLocal(context: Context, groupList: List<Group>) {
        val database = WDDatabase.getDatabase(context)
        val userRepository =
            com.bupware.wedraw.android.roomData.tables.user.UserRepository(database.userDao())
        Log.i("hilos", "getUsersAndSaveInLocal")

        groupList.forEach {
            it.userGroups?.forEach { userGroup ->
                Log.i("wawa", userGroup.userID.toString())
                Converter.converterUserToUserEntity(userGroup.userID)
                    ?.let { user -> userRepository.insert(user) }
            }
        }
    }

}