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
}