package com.bupware.wedraw.android.logic.dataHandler

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.logic.models.UserGroup
import com.bupware.wedraw.android.roomData.WDDatabase
import com.bupware.wedraw.android.roomData.tables.group.GroupRepository
import com.bupware.wedraw.android.roomData.tables.message.MessageFailedRepository
import com.bupware.wedraw.android.roomData.tables.message.MessageRepository
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupUserCrossRef
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupWithUsersRepository
import com.bupware.wedraw.android.roomData.tables.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.sql.Date

class DataHandler(val context: Context) {

    val room = WDDatabase.getDatabase(context = context)
    private val _groupListFlow: MutableStateFlow<Set<Group>> = MutableStateFlow(emptySet())
    val groupListFlow: Flow<Set<Group>> = _groupListFlow

    suspend fun saveUser(user: User) {

        val userRoom = com.bupware.wedraw.android.roomData.tables.user.User(
            userId = user.id.toString(),
            name = user.username.toString()
        )

        UserRepository(room.userDao()).insert(userRoom)

    }

    suspend fun saveMessage(message: Message, idGroup: Long) {

        if (message.id == null){

            val roomMessageFailed = com.bupware.wedraw.android.roomData.tables.message.MessageFailed(
                owner_group_Id = message.groupId,
                ownerId = message.senderId,
                text = message.text,
                image_Id = message.imageId,
                date = message.date?.let { Date(it.time) }
            )

            //Guardo el mensaje en memoria
            messageList[idGroup]?.add(message)

            //Guardo el mensaje en local [ROOM]
            MessageFailedRepository(room.messageFailedDao()).insert(roomMessageFailed)

            //Además, trato de enviarlo activamente
            val dataUtils = DataUtils()
            dataUtils.sendSinglePendingMessage(context = context,message = roomMessageFailed)

        } else {

            val roomMessage = com.bupware.wedraw.android.roomData.tables.message.Message(
                id = message.id,
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

    }


    suspend fun saveGroups(groups: List<Group>) {


        //Guardo en memoria
        groupList = groups.toMutableList()
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


    companion object {

        var forceUpdate = mutableStateOf(false)

        lateinit var user: User

        lateinit var userList : MutableSet<User>

        var groupList = mutableListOf<Group>()

        lateinit var userGroupList: MutableSet<UserGroup>

        //Map de idGrupo y los mensajes correspondientes
        var messageList = mutableMapOf<Long, MutableList<Message>>()


    }

}


