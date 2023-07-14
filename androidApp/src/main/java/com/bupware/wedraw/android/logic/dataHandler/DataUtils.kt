package com.bupware.wedraw.android.logic.dataHandler

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Telephony.MmsSms.PendingMessages
import android.util.Log
import com.bupware.wedraw.android.core.utils.Converter
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.logic.retrofit.repository.GroupRepository
import com.bupware.wedraw.android.logic.retrofit.repository.UserRepository
import com.bupware.wedraw.android.roomData.WDDatabase
import com.bupware.wedraw.android.roomData.tables.message.MessageFailed
import com.bupware.wedraw.android.roomData.tables.message.MessageFailedRepository
import com.bupware.wedraw.android.roomData.tables.message.MessageRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import com.bupware.wedraw.android.roomData.tables.group.GroupRepository as GroupRepositoryRoom
import com.bupware.wedraw.android.roomData.tables.group.Group as GroupRoom

import com.bupware.wedraw.android.roomData.tables.message.Message as MessageRoom
import com.bupware.wedraw.android.roomData.tables.user.User as UserRoom
import com.bupware.wedraw.android.roomData.tables.message.Message as MessageEntity
import com.bupware.wedraw.android.logic.retrofit.repository.MessageRepository as MessageRepositoryRetrofit

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

            DataHandler.messageList =
                getMapOfMessageByGroup(DataHandler.groupList, context).toMutableMap()
            Log.i("DataUtils", "initData: ${DataHandler.messageList}")


            /**
             * Obtengo los grupos en remoto los paso a local y los meto en memoria.
             * Si no hay grupos en remoto no hago nada.
             * */
            getGroupsRemote(context).also {
                if (it != null) {
                    remoteGroupsToLocal(it, context)
                }
            }?.let {

                DataHandler.groupList = it.toMutableList()
                DataHandler.forceUpdate.value = true
            }
            Log.i("DataUtils", "initData: ${DataHandler.groupList}")




        }
        sendPendingMessages(context)


    }

    private suspend fun sendPendingMessages(context: Context) {
        val room = WDDatabase.getDatabase(context)
        val pendingMessages = withContext(Dispatchers.Default) {
            MessageFailedRepository(room.messageFailedDao()).readAllData.first().toMutableList()
        }


        Log.i("DataUtilsSendOffline", "0sendPendingMessages: $pendingMessages")

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        var index = 0
        while (index < pendingMessages.size) {
            val pendingMessage : MessageFailed = pendingMessages[index]
            val network = connectivityManager.activeNetwork
            Log.i("DataUtilsSendOffline", "1sendPendingMessages: $network")
            if (network != null) {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                Log.i("DataUtilsSendOffline", "2sendPendingMessages: $networkCapabilities")
                if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
                    val returningId = sendPendingMessage(
                            pendingMessage.toMessage()
                    )
                    Log.i("DataUtilsSendOffline", "3sendPendingMessages: $returningId")
                    if (returningId != null) {
                        Log.i("DataUtilsSendOffline", "4sendPendingMessages: $returningId")
                        MessageFailedRepository(room.messageFailedDao()).deleteMessage(
                            pendingMessage
                        )
                        MessageRepository(room.messageDao()).insert(
                            Converter.convertMessageFailedToMessageEntity(
                                pendingMessage,
                                returningId
                            )
                        )
                        // Incrementa el índice solo si se elimina el mensaje
                        index++
                    }
                }
            }

            // Espera antes de la siguiente iteración del bucle
            delay(5000)
        }
        //MessageFailedRepository(room.messageFailedDao()).deleteAll()
    }

    private suspend fun sendPendingMessage(message: MessageRoom): Long? {
        return MessageRepositoryRetrofit.createMessage(
            Converter.convertMessageEntityToMessage(
                message
            )
        )
    }

    private suspend fun getMapOfMessageByGroup(
        group: MutableList<Group>,
        context: Context
    ): Map<Long, MutableList<Message>> {

        val map = mutableMapOf<Long, MutableList<Message>>()

        group.forEach { group ->
            val groupId = group.id ?: 0L // Conversión explícita de Long? a Long
            val messages = getMessagesLocalByGroupId(context, groupId)
            map[groupId] =
                messages?.let {
                    Converter.convertMessagesEntityToMessagesList(it).toMutableList()
                }!!
        }

        return map
    }

    private suspend fun getMessagesLocalByGroupId(
        context: Context,
        idGroup: Long
    ): Set<MessageRoom>? {

        val database = WDDatabase.getDatabase(context)
        val messageRepository =
            com.bupware.wedraw.android.roomData.tables.message.MessageRepository(database.messageDao())

        return messageRepository.getMessagesByGroupId(idGroup).first().toSet()
    }

    private suspend fun getGroupsRemote(context: Context): List<Group>? {

        val userId = Firebase.auth.currentUser?.uid.toString()
        val groups = withContext(Dispatchers.Default) {
            GroupRepository.getGroupByUserId(userId)
        }
        return groups
    }

    private suspend fun remoteGroupsToLocal(groups: List<Group>, context: Context): Boolean {

        val database = WDDatabase.getDatabase(context)
        val groupRepository =
            GroupRepositoryRoom(database.groupDao())
        try {
            groupRepository.insertAll(Converter.converterGroupsToGroupEntityList(groups))

        } catch (e: Exception) {
            return false
        }
        return true
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