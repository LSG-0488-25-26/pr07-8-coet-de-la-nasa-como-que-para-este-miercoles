package com.example.umafacts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umafacts.api.APIInterface
import com.example.umafacts.repository.Repository
import com.example.umafacts.model.UmamusumeDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class UmamusumeWithImage(
    val detail: UmamusumeDetail,
    val uniformImageUrl: String?
)

class UmaViewModel : ViewModel() {
    private val repository = Repository(APIInterface.create())

    private val _umamusumeList = MutableLiveData<List<UmamusumeWithImage>>(emptyList())
    val umamusumeList: LiveData<List<UmamusumeWithImage>> = _umamusumeList

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingMore = MutableLiveData<Boolean>(false)
    val isLoadingMore: LiveData<Boolean> = _isLoadingMore

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    // Pagination state
    private var allCharacters: List<UmamusumeDetail> = emptyList()
    private var currentPage = 0
    private val pageSize = 20
    private var hasMorePages = true

    init {
        fetchAllUmamusume()
    }

    private fun fetchAllUmamusume() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getUmamusumeList()
                .onSuccess { fullList ->
                    if (fullList.isNotEmpty()) {
                        allCharacters = fullList
                        currentPage = 0
                        _umamusumeList.value = emptyList()
                        loadNextPage()
                    } else {
                        _error.value = "No characters found"
                    }
                }
                .onFailure { error ->
                    _error.value = error.message ?: "Failed to load characters"
                }

            _isLoading.value = false
        }
    }

    fun loadNextPage() {
        if (_isLoadingMore.value == true || !hasMorePages) return

        viewModelScope.launch {
            _isLoadingMore.value = true

            val startIndex = currentPage * pageSize
            val endIndex = minOf(startIndex + pageSize, allCharacters.size)

            if (startIndex >= allCharacters.size) {
                hasMorePages = false
                _isLoadingMore.value = false
                return@launch
            }

            val pageCharacters = allCharacters.subList(startIndex, endIndex)

            // Fetch images for the current page
            val pageWithImages = fetchImagesForList(pageCharacters)

            // Append to existing list
            val currentList = _umamusumeList.value ?: emptyList()
            _umamusumeList.value = currentList + pageWithImages

            currentPage++
            hasMorePages = endIndex < allCharacters.size

            _isLoadingMore.value = false
        }
    }

    private suspend fun fetchImagesForList(list: List<UmamusumeDetail>): List<UmamusumeWithImage> {
        return withContext(Dispatchers.IO) {
            list.map { uma ->
                async {
                    val imageUrl = try {
                        repository.getUniformImage(uma.id).getOrNull()
                    } catch (e: Exception) {
                        null
                    }
                    UmamusumeWithImage(detail = uma, uniformImageUrl = imageUrl)
                }
            }.awaitAll()
        }
    }

    fun refreshData() {
        allCharacters = emptyList()
        currentPage = 0
        hasMorePages = true
        _umamusumeList.value = emptyList()
        _error.value = null
        fetchAllUmamusume()
    }

    fun clearError() {
        _error.value = null
    }
}