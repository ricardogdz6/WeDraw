package com.bupware.wedraw.android.logic.models

import java.util.Date


data class Group(
    var id:Long?,
    val name: String,
    var code: String,
    val userGroups: Set<UserGroup>
): java.io.Serializable

data class UserGroup(
    val id: Long?,
    val userID: String,
    val groupID: Long,
    //val messages: Set<Message>?,
    val isAdmin: Boolean = false
): java.io.Serializable

