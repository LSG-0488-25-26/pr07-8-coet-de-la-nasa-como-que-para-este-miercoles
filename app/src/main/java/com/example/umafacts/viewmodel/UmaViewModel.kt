package com.example.umafacts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umafacts.api.APIInterface
import com.example.umafacts.model.UmamusumeDetail
import com.example.umafacts.repository.Repository
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

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingMore = MutableLiveData(false)
    val isLoadingMore: LiveData<Boolean> = _isLoadingMore

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    // Search
    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    // Pagination
    private var allCharacters: List<UmamusumeDetail> = emptyList()
    private var filteredCharacters: List<UmamusumeDetail> = emptyList()
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
                .onSuccess { list ->
                    allCharacters = list
                    filteredCharacters = list
                    resetPagination()
                    loadNextPage()
                }
                .onFailure {
                    _error.value = it.message ?: "Error loading characters"
                }

            _isLoading.value = false
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query

        filteredCharacters =
            if (query.isBlank()) {
                allCharacters
            } else {
                allCharacters.filter {
                    it.toString().contains(query, ignoreCase = true)
                }
            }

        resetPagination()
        loadNextPage()
    }

    private fun resetPagination() {
        currentPage = 0
        hasMorePages = true
        _umamusumeList.value = emptyList()
    }

    fun loadNextPage() {
        if (_isLoadingMore.value == true || !hasMorePages) return

        viewModelScope.launch {
            _isLoadingMore.value = true

            val source = filteredCharacters
            val start = currentPage * pageSize
            val end = minOf(start + pageSize, source.size)

            if (start >= source.size) {
                hasMorePages = false
                _isLoadingMore.value = false
                return@launch
            }

            val page = source.subList(start, end)
            val withImages = fetchImagesForList(page)

            val current = _umamusumeList.value ?: emptyList()
            _umamusumeList.value = current + withImages

            currentPage++
            hasMorePages = end < source.size
            _isLoadingMore.value = false
        }
    }

    private suspend fun fetchImagesForList(
        list: List<UmamusumeDetail>
    ): List<UmamusumeWithImage> =
        withContext(Dispatchers.IO) {
            list.map { uma ->
                async {
                    val imageUrl = try {
                        repository.getUniformImage(uma.id).getOrNull()
                    } catch (_: Exception) {
                        null
                    }
                    UmamusumeWithImage(uma, imageUrl)
                }
            }.awaitAll()
        }

    fun refreshData() {
        allCharacters = emptyList()
        filteredCharacters = emptyList()
        resetPagination()
        fetchAllUmamusume()
    }

    fun clearError() {
        _error.value = null
    }
}
