package com.bupware.wedraw.android.data.tables.user

import androidx.lifecycle.LiveData
import com.bupware.wedraw.android.data.tables.user.User
import com.bupware.wedraw.android.data.tables.user.UserDao

class UserRepository(private val userDao: UserDao) {
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    val readAllData : LiveData<List<User>> = userDao.readAllData()

}