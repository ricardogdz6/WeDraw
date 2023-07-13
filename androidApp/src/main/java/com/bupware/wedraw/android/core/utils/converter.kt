package com.bupware.wedraw.android.core.utils

import com.bupware.wedraw.android.logic.models.UserGroup
import com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages.GroupUserCrossRef
import com.bupware.wedraw.android.roomData.tables.user.User
import com.bupware.wedraw.android.logic.models.User as UserDTO
import com.bupware.wedraw.android.logic.models.Group as GroupDTO
import com.bupware.wedraw.android.roomData.tables.user.User as UserRoom
import com.bupware.wedraw.android.roomData.tables.group.Group as GroupRoom

object Converter {
    fun convertGroupEntityToGroup(groupEntity: GroupRoom): GroupDTO {
        return GroupDTO(
            id = groupEntity.groupId,
            name = groupEntity.name,
            code = groupEntity.code,
            userGroups = null
        )
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
