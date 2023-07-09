package com.bupware.wedraw.android.logic.sessionData

import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.models.User


object sessionData{

    lateinit var user: User

    lateinit var groupList: List<Group>

    //El pair es de IDGrupo y sus mensajes
    var messageList = mutableListOf<Pair<Int,List<Message>>>()

}


