package com.bupware.wedraw.android.roomData.tables.user

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    suspend fun insert(user: User) {
        userDao.insertUser(user)
    }

    val readAllData : Flow<List<User>> = userDao.readAllData()

    fun getUserByID(userID : String) : Flow<User> = userDao.getUserByID(userID)

}