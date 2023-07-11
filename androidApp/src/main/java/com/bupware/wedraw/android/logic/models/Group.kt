package com.bupware.wedraw.android.logic.models

import java.io.Serializable
import java.util.Date


data class Group(
    var id:Int?,
    val name: String,
    var code: String,
    val userGroups: Set<UserGroup>?
): java.io.Serializable

data class UserGroup(
    val id: Int?,
    val userID: String,
    val groupID: Int,
    //val messages: Set<Message>?,
    val isAdmin: Boolean = false
): java.io.Serializable

