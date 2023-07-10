package com.bupware.wedraw.android.data.tables.user

import androidx.lifecycle.LiveData
import com.bupware.wedraw.android.data.tables.user.User
import com.bupware.wedraw.android.data.tables.user.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    suspend fun insert(user: User) {
        userDao.insertUser(user)
    }

    val readAllData : Flow<List<User>> = userDao.readAllData()

}