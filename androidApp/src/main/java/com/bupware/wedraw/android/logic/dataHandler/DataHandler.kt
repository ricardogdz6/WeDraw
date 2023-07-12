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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import java.sql.Date

class DataHandler(context: Context) {

    val room = WDDatabase.getDatabase(context = context)

    suspend fun saveMessage(message: Message, idGroup:Long){

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

    suspend fun loadMessages(){

        val messages = mutableListOf<Message>()

        MessageRepository(room.messageDao()).readAllData.collect{ messageList ->

            messageList.forEach {
                messages.add(Message(id = it.id, text= it.text, timeZone = null, senderId = it.ownerId, imageId = it.image_Id, groupId = it.owner_group_Id, date = it.date))
            }

            DataHandler.groupList.forEach {group ->
                DataHandler.messageList[group.id!!] = messages.filter { it.groupId == group.id }.toMutableList()
            }

            //Ahora que he cargado todo en local y memoría lo complimento con los mensajes restantes online si hay

            /*

            //region Obtener mensajes de internet
            //TODO()
            //TODO GET MESSAGES FROM DATE DE LOCAL
            groupList.forEach {
                var messageList = listOf<Message>()

                it.userGroups?.first()?.id?.let { it1 ->
                    messageList =
                        MessageRepository.getMessageByUserGroupId(it1)!!
                }

                DataHandler.messageList[it.id!!] = messageList.toMutableList()

            }
            //endregion

             */
        }

    }

    suspend fun saveGroups(groups: List<Group>) {

        //Guardo en memoria
        groupList = groups

        //AÑADO LOS NUEVOS GRUPOS Y USERGROUPS A LOCAL
        groups.forEach { group ->

            val roomGroup = com.bupware.wedraw.android.roomData.tables.group.Group(
                groupId = group.id!!, name = group.name, code = group.code
            )

            group.userGroups.forEach {userGroup ->
                val roomUserGroup = GroupUserCrossRef(groupId = group.id!!, userId = userGroup.userID, isAdmin = userGroup.isAdmin)
                GroupWithUsersRepository(room.groupWithUsersDao()).insert(roomUserGroup)
            }


            GroupRepository(room.groupDao()).insert(roomGroup)

        }

    }

    fun loadGroups(): Flow<List<Group>> = flow {

        var groupLocal = emptyList<com.bupware.wedraw.android.roomData.tables.group.Group>()
        var userGroupLocal = emptyList<GroupUserCrossRef>()

        var listaFinal = mutableListOf<Group>()

        GroupRepository(room.groupDao()).readAllData.collect{ groupList->
            groupLocal = groupList

            GroupWithUsersRepository(room.groupWithUsersDao()).readAllData.collect{ userGroup->
                userGroupLocal = userGroup

                val listaFinalGrupos = mutableListOf<Group>()
                groupLocal.forEach {group->

                    val userGroupFiltrado = userGroupLocal.filter { it.groupId == group.groupId }
                    val userGroups = mutableListOf<UserGroup>()
                    userGroupFiltrado.forEach { cross ->
                        userGroups.add(
                            UserGroup(
                                id = null,
                                userID = cross.userId,
                                groupID = cross.groupId,
                                isAdmin = cross.isAdmin
                            )
                        )
                    }

                    listaFinal.add(
                        Group(
                            id = group.groupId,
                            name = group.name,
                            code = group.code,
                            userGroups = userGroups.toSet()
                        )
                    )

                }

                DataHandler.groupList = listaFinal
                emit(listaFinal)

            }

        }

    }

    companion object{

        lateinit var user: User

        lateinit var groupList: List<Group>

        //Map de idGrupo y los mensajes correspondientes
        var messageList = mutableMapOf<Long,MutableList<Message>>()

    }

}


