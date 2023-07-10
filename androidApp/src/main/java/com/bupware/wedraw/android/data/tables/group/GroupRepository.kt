package com.bupware.wedraw.android.data.tables.group

import kotlinx.coroutines.flow.Flow

class GroupRepository (private val groupDao: GroupDao) {

    suspend fun insert(group: Group) = groupDao.insert(group)


    val readAllData : Flow<List<Group>> = groupDao.readAllData()



}