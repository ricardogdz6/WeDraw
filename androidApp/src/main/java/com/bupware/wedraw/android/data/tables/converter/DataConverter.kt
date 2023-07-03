package com.bupware.wedraw.android.data.tables.converter
import java.sql.Date;
import androidx.room.TypeConverter;

class DataConverter {

    @TypeConverter
    fun fromTimeStamp(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
