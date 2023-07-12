package com.bupware.wedraw.android.logic.dataHandler

import android.content.Context
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.roomData.WDDatabase
import com.bupware.wedraw.android.roomData.tables.group.GroupRepository
import com.bupware.wedraw.android.roomData.tables.message.MessageRepository
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupUserCrossRef
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupWithUsers
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupWithUsersRepository
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.UsersWithGroup
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

    suspend fun saveGroups(groups: List<Group>) {

        //AÑADO LOS NUEVOS GRUPOS Y USERGROUPS
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

    companion object{

        lateinit var user: User

        lateinit var groupList: List<Group>

        //Map de idGrupo y los mensajes correspondientes
        var messageList = mutableMapOf<Long,MutableList<Message>>()

    }

}


