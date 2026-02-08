package com.example.umafacts.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.umafacts.model.Favourite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favourite: Favourite)

    @Delete
    suspend fun deleteFavourite(favourite: Favourite)

    @Query("SELECT * FROM favourites ORDER BY addedAt DESC")
    fun getAllFavourites(): Flow<List<Favourite>>

    @Query("SELECT * FROM favourites WHERE characterId = :characterId")
    suspend fun getFavourite(characterId: Int): Favourite?

    @Query("SELECT COUNT(*) FROM favourites WHERE characterId = :characterId")
    fun isFavourite(characterId: Int): Flow<Boolean>
}