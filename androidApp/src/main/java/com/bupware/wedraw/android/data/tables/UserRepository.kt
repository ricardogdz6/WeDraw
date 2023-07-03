package com.bupware.wedraw.android.data.tables

import androidx.lifecycle.LiveData
import com.bupware.wedraw.android.data.tables.relationTables.user.User
import com.bupware.wedraw.android.data.tables.relationTables.user.UserDao

class UserRepository(private val userDao: UserDao) {
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    val readAllData : LiveData<List<User>> = userDao.readAllData()

}