package com.bupware.wedraw.android.logic.dataHandler

import android.content.Context
import android.util.Log
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.logic.models.UserGroup
import com.bupware.wedraw.android.roomData.WDDatabase
import com.bupware.wedraw.android.roomData.tables.group.GroupRepository
import com.bupware.wedraw.android.roomData.tables.message.MessageRepository
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupUserCrossRef
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupWithUsersRepository
import com.bupware.wedraw.android.roomData.tables.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.sql.Date

class DataHandler(context: Context) {

    val room = WDDatabase.getDatabase(context = context)

    suspend fun saveUser(user: User) {

        val userRoom = com.bupware.wedraw.android.roomData.tables.user.User(
            userId = user.id.toString(),
            name = user.username.toString()
        )

        UserRepository(room.userDao()).insert(userRoom)

    }

    suspend fun saveMessage(message: Message, idGroup: Long) {

        val roomMessage = com.bupware.wedraw.android.roomData.tables.message.Message(
            id = message.id!!,
            owner_group_Id = message.groupId,
            ownerId = message.senderId,
            text = message.text,
            image_Id = message.imageId,
            date = message.date?.let { Date(it.time) }
        )

        //Guardo el mensaje en memoria
        messageList[idGroup]?.add(message)

        //Guardo el mensaje en local [ROOM]
        MessageRepository(room.messageDao()).insert(roomMessage)

    }

    suspend fun loadMessages() {
        val messages = mutableListOf<Message>()
//        MessageRepository(room.messageDao()).deleteAll()
        val messageEntities = MessageRepository(room.messageDao()).readAllData.first()


        messageEntities.forEach {
            messages.add(
                Message(
                    id = it.id,
                    text = it.text,
                    timeZone = null,
                    senderId = it.ownerId,
                    imageId = it.image_Id,
                    groupId = it.owner_group_Id,
                    date = it.date
                )
            )
        }

        DataHandler.groupList.forEach { group ->
            DataHandler.messageList[group.id!!] =
                messages.filter { it.groupId == group.id }.toMutableList()
        }

        // Obtener mensajes de internet
        /*
        DataHandler.groupList.forEach { group ->
            val latestMessageId = messages.filter { it.groupId == group.id!! }
                .maxByOrNull { it.id!! }?.id ?: 0L

            val newMessages = com.bupware.wedraw.android.logic.retrofit.repository.MessageRepository.getMessagesFromDate(
                groupId = group.id!!,
                messageId = latestMessageId
            )

            newMessages?.forEach {
                DataHandler.messageList[group.id!!]?.add(
                    Message(
                        id = it.id,
                        text = it.text,
                        timeZone = null,
                        senderId = it.senderId,
                        imageId = it.imageId,
                        groupId = it.groupId,
                        date = it.date
                    )
                )
            }

            // Actualizar en local
            val roomMessageList = newMessages?.map {
                it.id?.let { it1 ->
                    com.bupware.wedraw.android.roomData.tables.message.Message(
                        id = it1,
                        owner_group_Id = it.groupId,
                        ownerId = it.senderId,
                        text = it.text,
                        image_Id = it.imageId,
                        date = it.date?.let { Date(it.time) }
                    )
                }
            } ?: emptyList()
            Log.i("DataHandler", "roomMessageList: $roomMessageList")
            MessageRepository(room.messageDao()).insertMessagesList(roomMessageList as List<com.bupware.wedraw.android.roomData.tables.message.Message>)
        }

         */
    }


    suspend fun saveGroups(groups: List<Group>) {


        //Guardo en memoria
        groupList = groups
        //AÑADO LOS NUEVOS GRUPOS Y USERGROUPS A LOCAL
        groups.forEach { group ->

            val roomGroup = com.bupware.wedraw.android.roomData.tables.group.Group(
                groupId = group.id!!, name = group.name, code = group.code
            )

            group.userGroups?.forEach { userGroup ->
                val roomUserGroup = GroupUserCrossRef(
                    groupId = group.id!!,
                    userId = userGroup.userID.id.toString(),
                    isAdmin = userGroup.isAdmin
                )
                GroupWithUsersRepository(room.groupWithUsersDao()).insert(roomUserGroup)
            }


            GroupRepository(room.groupDao()).insert(roomGroup)

        }

    }

    fun loadGroups(): Flow<List<Group>> = flow {

        //REPOSITORIOS
        val groupRepository = GroupRepository(room.groupDao())
        val userGroupRepository = GroupWithUsersRepository(room.groupWithUsersDao())
        val userRepository = UserRepository(room.userDao())

        //LISTAS
        val groupList = groupRepository.readAllData.first()
        val userGroupList = userGroupRepository.readAllData.first()

        //A DEVOLVER
        val listaFinalGrupos = mutableListOf<Group>()

        for (group in groupList) {
            val userGroupFiltrado = userGroupList.filter { it.groupId == group.groupId }
            val userGroups = mutableListOf<UserGroup>()

            for (cross in userGroupFiltrado) {
                val user = userRepository.getUserByID(cross.userId).first()
                val group = groupRepository.getGroupByGroupId(cross.groupId).first()

                val userGroup = UserGroup(
                    id = null,
                    userID = User(
                        username = user.name,
                        id = user.userId,
                        email = null,
                        premium = null,
                        expireDate = null
                    ),
                    groupID = Group(
                        id = group.groupId,
                        name = group.name,
                        code = group.code,
                        userGroups = null
                    ),
                    isAdmin = cross.isAdmin
                )
                userGroups.add(userGroup)
            }

            val groupWithUser = Group(
                id = group.groupId,
                name = group.name,
                code = group.code,
                userGroups = userGroups.toSet()
            )
            listaFinalGrupos.add(groupWithUser)
        }

        DataHandler.groupList = listaFinalGrupos
        emit(listaFinalGrupos)
    }

    companion object {

        lateinit var user: User

        lateinit var groupList: List<Group>

        //Map de idGrupo y los mensajes correspondientes
        var messageList = mutableMapOf<Long, MutableList<Message>>()

    }

}


