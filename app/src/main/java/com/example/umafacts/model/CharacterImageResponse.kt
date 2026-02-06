package com.example.umafacts.model

import com.google.gson.annotations.SerializedName

data class CharacterImageResponse(
    val images: List<ImageDetail>,
    val label: String,
    @SerializedName("label_en")
    val labelEn: String
)

data class ImageDetail(
    val image: String,
    val uploaded: String
)