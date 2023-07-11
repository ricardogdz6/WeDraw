package com.bupware.wedraw.android.ui.mainscreen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import com.bupware.wedraw.android.R
import com.bupware.wedraw.android.components.composables.SnackbarManager
import com.bupware.wedraw.android.logic.dataHandler.DataHandler
import com.bupware.wedraw.android.logic.dataHandler.DataUtils.Companion.gestionLogin
import com.bupware.wedraw.android.logic.dataHandler.DataUtils.Companion.updateUsername
import com.bupware.wedraw.android.logic.models.Group
import com.bupware.wedraw.android.logic.models.Message
import com.bupware.wedraw.android.logic.models.User
import com.bupware.wedraw.android.logic.retrofit.repository.GroupRepository
import com.bupware.wedraw.android.logic.retrofit.repository.MessageRepository
import com.bupware.wedraw.android.logic.retrofit.repository.UserRepository
import com.bupware.wedraw.android.theme.greenAchieve
import com.bupware.wedraw.android.theme.redWrong
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    init {
        viewModelScope.launch {
            gestionLogin { askForUsername = !askForUsername }
        }

        viewModelScope.launch {
            withContext(Dispatchers.Default) {getUserGroups()}
            showGroups = true

            //TODO MOVER ESTO?

            groupList.forEach {
                var messageList = listOf<Message>()

                it.userGroups?.first()?.id?.let { it1 -> messageList =
                    MessageRepository.getMessageByUserGroupId(it1)!!
                }

                DataHandler.messageList[it.id!!] = messageList.toMutableList()

            }
            //

        }


    }
    fun expandButton(index:Int){
        when(index) {
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

    fun launchUpdateUsername(context: Context){
        viewModelScope.launch {
            if (updateUsername(username)) {
                askForUsername = !askForUsername
                SnackbarManager.newSnackbar(context.getString(R.string.usuario_elegido_con_xito), greenAchieve)
            }
            else {SnackbarManager.newSnackbar(context.getString(R.string.este_usuario_ya_est_cogido), redWrong)}
        }
    }

    fun createGroupButton(context: Context){
        //TODO METER RESTRICCION EN LA API
        if (groupName.isBlank()){
            SnackbarManager.newSnackbar(context.getString(R.string.no_dejes_el_nombre_vac_o), redWrong)
        }
        else {

            if(DataHandler.user.premium){

                if (groupList.size==10){
                    SnackbarManager.newSnackbar(context.getString(R.string.ya_est_s_en_el_m_ximo_de_grupos_permitido), redWrong)
                } else createGroupAction()

            } else {
                if (groupList.size==3){
                    SnackbarManager.newSnackbar(context.getString(R.string.ya_est_s_en_el_m_ximo_de_grupos_permitido), redWrong)
                } else createGroupAction()
            }

        }
    }

    fun createGroupAction(){
        viewModelScope.launch {
            val returningCode = withContext(Dispatchers.Default) {GroupRepository.createGroup(groupName,Firebase.auth.currentUser!!.uid)}
            getUserGroups()
            targetNavigation = groupList.first { it.code == returningCode }.id!!
            moreOptionsEnabled = !moreOptionsEnabled
            expandCreateGroup = false
            groupName = ""
            navigateToChat = true
        }
    }

    fun joinGroupButton(context: Context){

        if (joinCode.isBlank()){
            SnackbarManager.newSnackbar(context.getString(R.string.no_dejes_el_c_digo_vac_o), redWrong)
        }
        else {
            viewModelScope.launch {


                val groupId = withContext(Dispatchers.Default) { GroupRepository.getGroupByCode(joinCode)?.id }

                if (groupId == null){
                    SnackbarManager.newSnackbar(context.getString(R.string.codigo_invalido), redWrong)
                }
                else {

                    //Compruebo que el grupo no esté lleno
                    val isGroupFull = withContext(Dispatchers.Default) {GroupRepository.isGroupFull(groupId!!)}
                    
                    if (isGroupFull){
                        SnackbarManager.newSnackbar(context.getString(R.string.el_grupo_est_lleno), redWrong)
                    }
                    else {
                        withContext(Dispatchers.Default) {
                            GroupRepository.insertUsertoUserGroup(
                                userId = Firebase.auth.currentUser?.uid.toString(),
                                groupId = groupId!!
                            )
                        }
                        getUserGroups()
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

    suspend fun getUserGroups(){
        val userId = Firebase.auth.currentUser?.uid.toString()
        val group = withContext(Dispatchers.Default) { GroupRepository.getGroupByUserId(userId) } ?: emptyList()
        groupList = group
        DataHandler.groupList = group
        //TODO DATAHANDLER SAVE LOCAL GROUP
    }


}



