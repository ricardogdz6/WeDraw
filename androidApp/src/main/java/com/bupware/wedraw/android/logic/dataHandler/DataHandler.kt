package com.bupware.wedraw.android.logic.dataHandler

import android.content.Context
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.roomData.WDDatabase
import com.bupware.wedraw.android.roomData.tables.group.GroupRepository
import com.bupware.wedraw.android.roomData.tables.message.MessageRepository
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

    suspend fun saveGroups(groups:List<Group>){

        //Vacío el estado de los grupos en la sesión anterior
        //TODO

        groups.forEach { group->
            val roomGroup = com.bupware.wedraw.android.roomData.tables.group.Group(
                groupId = group.id!!,
                name = group.name,
                leader_ID = group.userGroups?.first { it.isAdmin }?.userID?.id
            )

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


