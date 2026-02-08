package com.example.umafacts.repository

import com.example.umafacts.database.FavouriteDao
import com.example.umafacts.model.Favourite
import com.example.umafacts.model.UmamusumeDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouritesRepository @Inject constructor(
    private val favouriteDao: FavouriteDao
) {
    // Now requires detail and image URL to cache the data
    suspend fun addFavourite(detail: UmamusumeDetail, uniformImageUrl: String) {
        val favourite = Favourite(
            characterId = detail.id,
            nameEn = detail.nameEn,
            nameJp = detail.nameJp,
            thumbImg = detail.thumbImg,
            colorMain = detail.colorMain,
            colorSub = detail.colorSub,
            grade = detail.grade,
            height = detail.height

        )
        favouriteDao.insertFavourite(favourite)
    }

    suspend fun removeFavourite(characterId: Int) {
        favouriteDao.getFavourite(characterId)?.let {
            favouriteDao.deleteFavourite(it)
        }
    }

    fun isFavourite(characterId: Int): Flow<Boolean> = favouriteDao.isFavourite(characterId)

    fun getAllFavourites(): Flow<List<Favourite>> = favouriteDao.getAllFavourites()

    // Update toggle to accept the necessary data
    suspend fun toggleFavourite(detail: UmamusumeDetail, uniformImageUrl: String) {
        val isCurrentlyFavourite = favouriteDao.getFavourite(detail.id) != null
        if (isCurrentlyFavourite) {
            removeFavourite(detail.id)
        } else {
            addFavourite(detail, uniformImageUrl)
        }
    }
}