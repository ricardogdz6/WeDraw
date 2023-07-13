package com.bupware.wedraw.android.core.utils

import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.UserGroup
import com.bupware.wedraw.android.roomData.tables.message.Message
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupUserCrossRef
import com.bupware.wedraw.android.roomData.tables.user.User
import com.bupware.wedraw.android.logic.models.User as UserDTO
import com.bupware.wedraw.android.logic.models.Group as GroupDTO
import com.bupware.wedraw.android.roomData.tables.user.User as UserRoom
import com.bupware.wedraw.android.roomData.tables.group.Group as GroupRoom

import com.bupware.wedraw.android.logic.models.Message as MessageDTO

object Converter {
    fun convertGroupEntityToGroup(groupEntity: GroupRoom): GroupDTO {
        return GroupDTO(
            id = groupEntity.groupId,
            name = groupEntity.name,
            code = groupEntity.code,
            userGroups = null
        )
    }

    fun converterGroupsEntityToGroupsList(groupsEntity: List<GroupRoom>): Set<Group> {
        val groups = mutableSetOf<GroupDTO>()
        groupsEntity.forEach { group ->
            groups.add(convertGroupEntityToGroup(group))
        }
        return groups
    }

    fun convertUserEntityToUser(user: UserRoom): UserDTO {
        return UserDTO(
            id = user.userId,
            username = user.name,
            email = null,
            premium = null,
            expireDate = null
        )
    }

    fun convertUsersEntityToUsersList(usersEntity: Set<UserRoom>): MutableSet<UserDTO> {
        val users = mutableSetOf<UserDTO>()
        usersEntity.forEach { user ->
            users.add(convertUserEntityToUser(user))
        }
        return users
    }

    fun convertMessagesEntityToMessagesList(messagesEntity: Set<Message>): Set<MessageDTO> {
        val messages = mutableSetOf<MessageDTO>()

        messagesEntity.forEach { message ->
            messages.add(
                MessageDTO(
                    id = message.id,
                    text = message.text,
                    date = message.date,
                    senderId = message.ownerId,
                    groupId = message.owner_group_Id,
                    imageId = message.image_Id,
                    timeZone = null //Todo: No se si el timezone aqui deberia ser null.
                )
            )
        }
        return messages
    }
    fun convertUserGroupEntityToUserGroup(
        userGroupEntity: GroupUserCrossRef,
        user: UserRoom,
        group: GroupRoom
    ): UserGroup {
        return UserGroup(
            id = userGroupEntity.groupId,
            userID = convertUserEntityToUser(user),
            groupID = convertGroupEntityToGroup(group),
            isAdmin = userGroupEntity.isAdmin
        )
    }

    fun converterGroupToGroupEntity() {
        TODO("Terminar de implementar el convertidor de Group a GroupEntity")
    }

    fun converterUserToUserEntity(UserDTO: UserDTO): User? {
        return UserDTO.id?.let {
            UserDTO.username?.let { it1 ->
                UserRoom(
                    userId = it,
                    name = it1
                )
            }
        }

    }
}
