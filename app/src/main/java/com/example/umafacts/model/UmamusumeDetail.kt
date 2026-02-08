package com.example.umafacts.model

import com.google.gson.annotations.SerializedName

data class UmamusumeDetail(
    @SerializedName("birth_day")
    val birthDay: Int,
    @SerializedName("birth_month")
    val birthMonth: Int,
    @SerializedName("category_label")
    val categoryLabel: String,
    @SerializedName("category_label_en")
    val enCategoryLabel: String,
    @SerializedName("category_value")
    val categoryValue: String,
    @SerializedName("color_main")
    val colorMain: String,
    @SerializedName("color_sub")
    val colorSub: String,
    @SerializedName("date_gmt")
    val dateGmt: String,
    @SerializedName("detail_img_pc")
    val detailImgPc: Any,
    @SerializedName("detail_img_sp")
    val detailImgSp: Any,
    @SerializedName("ears_fact")
    val earsFact: Any,
    @SerializedName("family_fact")
    val familyFact: Any,
    @SerializedName("game_id")
    val gameId: Int,
    val grade: String,
    val height: Int,
    val id: Int,
    val link: String,
    @SerializedName("modified_gmt")
    val modifiedGmt: String,
    @SerializedName("name_en")
    val nameEn: String,
    @SerializedName("name_en_internal")
    val nameInternal: String,
    @SerializedName("name_jp")
    val nameJp: String,
    @SerializedName("preferred_url")
    val preferredUrl: String,
    val profile: String,
    val residence: String,
    @SerializedName("row_number")
    val rowNumber: Int,
    @SerializedName("shoe_size")
    val shoeSize: Any,
    val slogan: String,
    @SerializedName("sns_header")
    val snsHeader: String,
    @SerializedName("sns_icon")
    val snsIcon: String,
    val strengths: Any,
    @SerializedName("tail_fact")
    val tailFact: Any,
    @SerializedName("thumb_img")
    val thumbImg: String,
    val voice: String,
    val weaknesses: Any,
    val weight: Any
)


fun UmamusumeDetail.toFavourite(): Favourite {
    return Favourite(
        characterId = this.id,
        nameEn = this.nameEn,
        nameJp = this.nameJp,
        colorMain = this.colorMain,
        colorSub = this.colorSub,
        thumbImg = this.thumbImg,
        grade = this.grade,
        height = this.height
    )
}