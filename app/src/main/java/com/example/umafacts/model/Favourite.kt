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
    val height: Int,
    val weight: Any,
    val shoeSize: Any,
    val earsFact: Any,
    val tailFact: Any,
    val strengths: Any,
    val weaknesses: Any,
    val voice: String,
    val profile: String,
    val slogan: String,
    val categoryLabel: String,
    val enCategoryLabel: String,
    val categoryValue: String,
    val snsHeader: String,
    val snsIcon: String,
    val familyFact: Any,
    val gameId: Int,
    val link: String,
    val preferredUrl: String,
    val detailImgPc: Any,
    val detailImgSp: Any,
    val dateGmt: String,
    val modifiedGmt: String,
    val residence: String,
    val rowNumber: Int,
    val birthDay: Int,
    val birthMonth: Int,
    val internalName: String
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
            weight = weight,
            shoeSize = shoeSize,
            earsFact = earsFact,
            tailFact = tailFact,
            strengths = strengths,
            weaknesses = weaknesses,
            voice = voice,
            profile = profile,
            slogan = slogan,
            categoryLabel = categoryLabel,
            enCategoryLabel = enCategoryLabel,
            categoryValue = categoryValue,
            snsHeader = snsHeader,
            snsIcon = snsIcon,
            familyFact = familyFact,
            gameId = gameId,
            link = link,
            preferredUrl = preferredUrl,
            detailImgPc = detailImgPc,
            detailImgSp = detailImgSp,
            dateGmt = dateGmt,
            modifiedGmt = modifiedGmt,
            residence = residence,
            rowNumber = rowNumber,
            birthDay = birthDay,
            birthMonth = birthMonth,
            nameInternal = internalName
        )
    }
}