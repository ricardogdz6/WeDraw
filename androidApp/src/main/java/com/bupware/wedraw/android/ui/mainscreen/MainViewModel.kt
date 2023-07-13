package com.bupware.wedraw.android.ui.mainscreen

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.components.composables.SnackbarManager
import com.bupware.wedraw.android.core.utils.Converter
import com.bupware.wedraw.android.logic.dataHandler.DataHandler
import com.bupware.wedraw.android.logic.dataHandler.DataUtils.Companion.getUserGroups
import com.bupware.wedraw.android.logic.dataHandler.DataUtils.Companion.gestionLogin
import com.bupware.wedraw.android.logic.dataHandler.DataUtils.Companion.updateUsername
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.UserGroup
import com.bupware.wedraw.android.logic.retrofit.repository.GroupRepository
import com.bupware.wedraw.android.roomData.WDDatabase
import com.bupware.wedraw.android.roomData.tables.user.UserRepository
import com.bupware.wedraw.android.theme.greenAchieve
import com.bupware.wedraw.android.theme.redWrong
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    var moreOptionsEnabled by savedStateHandle.saveable { mutableStateOf(false) }
    var showGroups by savedStateHandle.saveable { mutableStateOf(false) }
    var showSettings by savedStateHandle.saveable { mutableStateOf(false) }

    //Settings Menu
    var expandJoinGroup by savedStateHandle.saveable { mutableStateOf(false) }
    var expandCreateGroup by savedStateHandle.saveable { mutableStateOf(false) }

    var groupName by savedStateHandle.saveable { mutableStateOf("") }
    var joinCode by savedStateHandle.saveable { mutableStateOf("") }

    var targetNavigation by savedStateHandle.saveable { mutableStateOf(0L) }
    var navigateToChat by savedStateHandle.saveable { mutableStateOf(false) }

    //Init
    var askForUsername by savedStateHandle.saveable { mutableStateOf(false) }
    var groupList by savedStateHandle.saveable { mutableStateOf(listOf<Group>()) }

    //Username
    var username by savedStateHandle.saveable { mutableStateOf("") }

    fun initValues(context:Context){

        viewModelScope.launch {
            gestionLogin(context = context, askForUsername = { askForUsername = !askForUsername })
        }

        /*
        viewModelScope.launch {
            localInit(context)
        }

         */

        viewModelScope.launch {
            loadGroupsAndMessages(context)
        }

    }


    fun expandButton(index: Int) {
        when (index) {
            1 -> {
                expandCreateGroup = !expandCreateGroup
                expandJoinGroup = false
            }

            2 -> {
                expandCreateGroup = false
                expandJoinGroup = !expandJoinGroup
            }

        }

    }

    fun launchUpdateUsername(context: Context) {
        viewModelScope.launch {
            if (updateUsername(username)) {
                askForUsername = !askForUsername
                SnackbarManager.newSnackbar(
                    context.getString(R.string.usuario_elegido_con_xito),
                    greenAchieve
                )
            } else {
                SnackbarManager.newSnackbar(
                    context.getString(R.string.este_usuario_ya_est_cogido),
                    redWrong
                )
            }
        }
    }

    fun createGroupButton(context: Context) {
        //TODO METER RESTRICCION EN LA API
        if (groupName.isBlank()) {
            SnackbarManager.newSnackbar(
                context.getString(R.string.no_dejes_el_nombre_vac_o),
                redWrong
            )
        } else {

            if (DataHandler.user.premium == true) {

                if (groupList.size == 10) {
                    SnackbarManager.newSnackbar(
                        context.getString(R.string.ya_est_s_en_el_m_ximo_de_grupos_permitido),
                        greenAchieve
                    )
                } else createGroupAction(context)

            } else {
                if (groupList.size == 3) {
                    SnackbarManager.newSnackbar(
                        context.getString(R.string.ya_est_s_en_el_m_ximo_de_grupos_permitido),
                        greenAchieve
                    )
                } else createGroupAction(context)
            }

        }
    }

    private fun createGroupAction(context: Context) {
        viewModelScope.launch {
            val returningCode = withContext(Dispatchers.Default) {
                GroupRepository.createGroup(
                    groupName,
                    Firebase.auth.currentUser!!.uid
                )
            }
            getUserGroupsAndSave(context)
            targetNavigation = groupList.first { it.code == returningCode }.id!!
            moreOptionsEnabled = !moreOptionsEnabled
            expandCreateGroup = false
            groupName = ""
            navigateToChat = true
        }
    }

    fun joinGroupButton(context: Context) {

        if (joinCode.isBlank()) {
            SnackbarManager.newSnackbar(
                context.getString(R.string.no_dejes_el_c_digo_vac_o),
                redWrong
            )
        } else {
            viewModelScope.launch {

                val groupId =
                    withContext(Dispatchers.Default) { GroupRepository.getGroupByCode(joinCode)?.id }

                if (groupId == null) {
                    SnackbarManager.newSnackbar(
                        context.getString(R.string.codigo_invalido),
                        redWrong
                    )
                } else {

                    //Compruebo que el grupo no esté lleno
                    val isGroupFull =
                        withContext(Dispatchers.Default) { GroupRepository.isGroupFull(groupId!!) }

                    if (isGroupFull) {
                        SnackbarManager.newSnackbar(
                            context.getString(R.string.el_grupo_est_lleno),
                            redWrong
                        )
                    } else {
                        withContext(Dispatchers.Default) {
                            GroupRepository.insertUsertoUserGroup(
                                userId = Firebase.auth.currentUser?.uid.toString(),
                                groupId = groupId!!
                            )
                        }
                        getUserGroupsAndSave(context)
                        targetNavigation = groupId!!
                        moreOptionsEnabled = !moreOptionsEnabled
                        expandJoinGroup = false
                        joinCode = ""
                        navigateToChat = true
                    }

                }
            }
        }

    }

    //Obtiene todos los grupos del usuario y los guarda en la base de datos local y en memoria
    private suspend fun getUserGroupsAndSave(context: Context) {

        val groups = getUserGroups()

        //Actualizo la variable del viewModel
        groupList = groups

        DataHandler(context).saveGroups(groups)

        showGroups = true
    }

    private suspend fun getUsersAndSaveInLocal(context: Context) {
        val database = WDDatabase.getDatabase(context)
        val userRepository = UserRepository(database.userDao())
        //Log.i("hilos","getUsersAndSaveInLocal")

        groupList.forEach {
            it.userGroups?.forEach { userGroup ->
                //Log.i("wawa", userGroup.userID.toString())
                Converter.converterUserToUserEntity(userGroup.userID)
                    ?.let { user -> userRepository.insert(user) }
            }
        }
    }


    private suspend fun loadGroupsAndMessages(context: Context) {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        //Si hay internet que cargue de internet sino localmente
        if (networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {


            withContext(Dispatchers.Default) {
                getUserGroupsAndSave(context)
            }

            withContext(Dispatchers.Default) {
                getUsersAndSaveInLocal(context)
            }

            withContext(Dispatchers.Default) {
                DataHandler(context).loadMessages()
            }
        } else {
            //region Obtener grupos localmente
            val localGroups = DataHandler(context).loadGroups()
            localGroups.collect {
                groupList = it
                if (groupList.isNotEmpty()) showGroups = true
                DataHandler(context).loadMessages()
            }

            //endregion
        }

    }


}


//TODO BORRAR?
suspend fun localInit(context: Context) {

    val localDatabase = WDDatabase.getDatabase(context)

    //region INIT GRUPOS

    val groupListLocal = mutableListOf<Group>()
    val allUserGroupLocal = mutableSetOf<UserGroup>()
    val userGroup = localDatabase.groupWithUsersDao()
    val userDatabase = UserRepository(localDatabase.userDao())
    val groupDatabase =
        com.bupware.wedraw.android.roomData.tables.group.GroupRepository(localDatabase.groupDao())

    userGroup.readAllData()
        .collect { crossRefs ->
            crossRefs.forEach { userGroup ->
                allUserGroupLocal.add(

                    UserGroup(
                        id = null,
                        userID = Converter.convertUserEntityToUser(
                            userDatabase.getUserByID(
                                userGroup.userId
                            ).first()
                        ),
                        groupID = Converter.convertGroupEntityToGroup(
                            groupDatabase.getGroupByGroupId(
                                userGroup.groupId
                            ).first()
                        ),
                        isAdmin = userGroup.isAdmin
                    )
                )
            }

            localDatabase.groupDao().readAllData().collect { groups ->
                groups.forEach { group ->
                    Log.i("wowo", group.toString())

                    //Obtengo userGroups
                    val userGroupList = allUserGroupLocal.filter { it.groupID.id == group.groupId }


                    groupListLocal.add(
                        Group(
                            id = group.groupId,
                            code = group.code,
                            name = group.name,
                            userGroups = userGroupList.toSet()
                        )
                    )
                }
                Log.i("wowo", groupListLocal.toString())

            }

        }
    Log.i("wowo", "INIT GRUPOS")


    //endregion


    /*
    //region INIT Messages
    var messageListLocal = mutableListOf<Message>()
    instance.messageDao().readAllDataMessage().collect {it.forEach {message ->
        messageListLocal.add(Message(
            id = null,
            text = ,
            timeZone = null,
            senderId = ,
            groupId = ,
            date = null
        ))
    }}
    //endregion
     */

}



