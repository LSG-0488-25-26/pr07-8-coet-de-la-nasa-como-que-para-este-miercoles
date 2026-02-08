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
            height = detail.height,
            weight = detail.weight,
            shoeSize = detail.shoeSize,
            earsFact = detail.earsFact,
            tailFact = detail.tailFact,
            strengths = detail.strengths,
            weaknesses = detail.weaknesses,
            voice = detail.voice,
            profile = detail.profile,
            slogan = detail.slogan,
            categoryLabel = detail.categoryLabel,
            enCategoryLabel = detail.enCategoryLabel,
            categoryValue = detail.categoryValue,
            snsHeader = detail.snsHeader,
            snsIcon = detail.snsIcon,
            familyFact = detail.familyFact,
            gameId = detail.gameId,
            link = detail.link,
            preferredUrl = detail.preferredUrl,
            detailImgPc = detail.detailImgPc,
            detailImgSp = detail.detailImgSp,
            dateGmt = detail.dateGmt,
            modifiedGmt = detail.modifiedGmt,
            residence = detail.residence,
            rowNumber = detail.rowNumber,
            birthDay = detail.birthDay,
            birthMonth = detail.birthMonth,
            internalName = detail.nameInternal
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