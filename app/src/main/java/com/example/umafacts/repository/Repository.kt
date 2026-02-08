package com.example.umafacts.repository

import com.example.umafacts.api.APIInterface
import com.example.umafacts.model.CharacterImageResponse
import com.example.umafacts.model.UmamusumeDetail

class Repository(private val api: APIInterface) {

    suspend fun getUmamusumeList(): Result<List<UmamusumeDetail>> {
        return try {
            val response = api.getUmamusumeInfo()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = when (response.code()) {
                    502 -> "Server is temporarily unavailable. Please try again later."
                    500 -> "Internal server error"
                    404 -> "Data not found"
                    else -> "Error: ${response.code()} - ${response.message()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }

    suspend fun getUniformImage(characterId: Int): Result<String?> {
        return try {
            val response = api.getCharacterImages(characterId)
            if (response.isSuccessful && response.body() != null) {
                val uniformCategory = response.body()!!.find {
                    it.labelEn == "Uniform"
                }
                val latestImage = uniformCategory?.images?.firstOrNull()?.image
                Result.success(latestImage)
            } else {
                val errorMessage = when (response.code()) {
                    502 -> "Server is temporarily unavailable"
                    500 -> "Internal server error"
                    404 -> "Character images not found"
                    else -> "Error: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }

    suspend fun getCharacterImages(characterId: Int): Result<List<CharacterImageResponse>> {
        return try {
            val response = api.getCharacterImages(characterId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMessage = when (response.code()) {
                    502 -> "Server is temporarily unavailable. Images cannot be loaded."
                    500 -> "Internal server error"
                    404 -> "Character images not found"
                    else -> "Error: ${response.code()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }
}