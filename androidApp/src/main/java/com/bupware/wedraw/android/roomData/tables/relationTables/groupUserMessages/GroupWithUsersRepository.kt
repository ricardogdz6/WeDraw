package com.bupware.wedraw.android.roomData.tables.relationTables.groupUserMessages

import kotlinx.coroutines.flow.Flow

class GroupWithUsersRepository(private val groupWithUsersDao: GroupWithUsersDao)
{
    suspend fun insert(groupUserCrossRef: GroupUserCrossRef) {
        groupWithUsersDao.insertGroupWithUser(groupUserCrossRef)
    }

    suspend fun crossGroupWithLeader(group: Long, leader: String) {
        groupWithUsersDao.crossGroupWithLeader(group, leader)
    }

    val readAllData : Flow<List<GroupUserCrossRef>> = groupWithUsersDao.readAllData()




}