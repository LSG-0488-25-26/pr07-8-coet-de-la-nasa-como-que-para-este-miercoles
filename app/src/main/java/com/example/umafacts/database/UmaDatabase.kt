// UmaDatabase.kt
package com.example.umafacts.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.umafacts.model.Favourite

@Database(
    entities = [Favourite::class],
    version = 5,
    exportSchema = false
)
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