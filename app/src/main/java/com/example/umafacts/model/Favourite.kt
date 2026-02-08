package com.example.umafacts.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class Favourite(
    @PrimaryKey
    val characterId: Int,
    val addedAt: Long = System.currentTimeMillis()
)