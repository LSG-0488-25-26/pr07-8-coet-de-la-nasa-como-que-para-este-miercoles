package com.example.umafacts.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class Favourite(
    @PrimaryKey val characterId: Int,
    val nameEn: String,
    val nameJp: String,
    val colorMain: String,
    val colorSub: String,
    val thumbImg: String,
    val grade: String,
    val height: Int
) {
    // Helper to convert DB entity back to a basic Detail object for the UI
    fun toUmamusumeDetail(): UmamusumeDetail {
        return UmamusumeDetail(
            id = characterId,
            nameEn = nameEn,
            nameJp = nameJp,
            colorMain = colorMain,
            colorSub = colorSub,
            thumbImg = thumbImg,
            grade = grade,
            height = height,
            // Provide default/empty values for fields not stored in DB
            birthDay = 0, birthMonth = 0, categoryLabel = "", enCategoryLabel = "",
            categoryValue = "", dateGmt = "", detailImgPc = "", detailImgSp = "",
            earsFact = "", familyFact = "", gameId = 0, link = "", modifiedGmt = "",
            nameInternal = "", preferredUrl = "", profile = "", residence = "",
            rowNumber = 0, shoeSize = "", slogan = "", snsHeader = "", snsIcon = "",
            strengths = "", tailFact = "", voice = "", weaknesses = "", weight = ""
        )
    }
}