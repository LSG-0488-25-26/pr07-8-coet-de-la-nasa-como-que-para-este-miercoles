package com.example.umafacts.api

import com.example.umafacts.model.UmamusumeDetail

class Repository(private val api: APIInterface) {

    suspend fun getUmamusumeList(): Result<List<UmamusumeDetail>> {
        return try {
            val response = api.getUmamusumeInfo()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUniformImage(characterId: Int): Result<String?> {
        return try {
            val response = api.getCharacterImages(characterId)
            if (response.isSuccessful && response.body() != null) {
                // Find the uniform category and get the latest image
                val uniformCategory = response.body()!!.find {
                    it.labelEn == "Uniform"
                }
                val latestImage = uniformCategory?.images?.firstOrNull()?.image
                Result.success(latestImage)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}