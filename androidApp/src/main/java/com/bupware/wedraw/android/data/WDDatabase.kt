package com.bupware.wedraw.android.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bupware.wedraw.android.data.tables.message.Message
import com.bupware.wedraw.android.data.tables.message.MessageDao
import com.bupware.wedraw.android.data.tables.converter.DataConverter
import com.bupware.wedraw.android.data.tables.group.Group
import com.bupware.wedraw.android.data.tables.group.GroupDao
import com.bupware.wedraw.android.data.tables.image.Image
import com.bupware.wedraw.android.data.tables.image.ImageDao
import com.bupware.wedraw.android.data.tables.user.User
import com.bupware.wedraw.android.data.tables.relationTables.Users_groups
import com.bupware.wedraw.android.data.tables.user.UserDao

@Database(entities = [User::class, Group::class, Users_groups::class, Image::class, Message::class], version = 2)
@TypeConverters(DataConverter::class)
abstract class WDDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun groupDao(): GroupDao
    abstract fun imageDao(): ImageDao
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: WDDatabase? = null

        fun getDatabase(context: Context): WDDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                // Create database here
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    WDDatabase::class.java,
                    "user_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}