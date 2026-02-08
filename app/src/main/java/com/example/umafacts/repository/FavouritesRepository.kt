package com.example.umafacts.repository

import com.example.umafacts.database.FavouriteDao
import com.example.umafacts.model.Favourite
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouritesRepository @Inject constructor(
    private val favouriteDao: FavouriteDao
) {
    suspend fun addFavourite(characterId: Int) {
        favouriteDao.insertFavourite(Favourite(characterId))
    }

    suspend fun removeFavourite(characterId: Int) {
        favouriteDao.getFavourite(characterId)?.let {
            favouriteDao.deleteFavourite(it)
        }
    }

    fun isFavourite(characterId: Int): Flow<Boolean> {
        return favouriteDao.isFavourite(characterId)
    }

    fun getAllFavourites(): Flow<List<Favourite>> {
        return favouriteDao.getAllFavourites()
    }

    suspend fun toggleFavourite(characterId: Int): Boolean {
        val isCurrentlyFavourite = favouriteDao.getFavourite(characterId) != null
        if (isCurrentlyFavourite) {
            removeFavourite(characterId)
            return false
        } else {
            addFavourite(characterId)
            return true
        }
    }
}