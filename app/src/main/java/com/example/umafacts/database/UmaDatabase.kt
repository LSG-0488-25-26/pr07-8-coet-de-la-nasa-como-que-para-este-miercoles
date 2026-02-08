// UmaDatabase.kt
package com.example.umafacts.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.TypeConverters
import com.example.umafacts.model.Favourite
import com.example.umafacts.utils.Converters

@Database(
    entities = [Favourite::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class UmaDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao

    companion object {
        @Volatile
        private var INSTANCE: UmaDatabase? = null

        fun getDatabase(context: Context): UmaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UmaDatabase::class.java,
                    "uma_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}