package com.example.umafacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umafacts.api.APIInterface
import com.example.umafacts.api.Repository
import com.example.umafacts.model.UmamusumeDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UmamusumeWithImage(
    val detail: UmamusumeDetail,
    val uniformImageUrl: String?
)

class UmaViewModel : ViewModel() {
    private val repository = Repository(APIInterface.create())

    private val _umamusumeList = MutableStateFlow<List<UmamusumeWithImage>>(emptyList())
    val umamusumeList: StateFlow<List<UmamusumeWithImage>> = _umamusumeList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Pagination state
    private var allCharacters: List<UmamusumeDetail> = emptyList()
    private var currentPage = 0
    private val pageSize = 20
    private var hasMorePages = true

    init {
        fetchUmamusume()
    }

    private fun fetchUmamusume() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getUmamusumeList()
                .onSuccess { fullList ->
                    allCharacters = fullList
                    currentPage = 0
                    _umamusumeList.value = emptyList()
                    loadNextPage()
                }
                .onFailure { error ->
                    _error.value = error.message
                    println("Error: ${error.message}")
                }

            _isLoading.value = false
        }
    }

    fun loadNextPage() {
        if (_isLoadingMore.value || !hasMorePages) return

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


            val pageWithImages = pageCharacters.map { uma ->
                val imageResult = repository.getUniformImage(uma.id)
                UmamusumeWithImage(
                    detail = uma,
                    uniformImageUrl = imageResult.getOrNull()
                )
            }


            _umamusumeList.value += pageWithImages
            currentPage++
            hasMorePages = endIndex < allCharacters.size

            println("Loaded page $currentPage: ${pageWithImages.size} characters")

            _isLoadingMore.value = false
        }
    }

    fun retry() {
        allCharacters = emptyList()
        currentPage = 0
        hasMorePages = true
        _umamusumeList.value = emptyList()
        fetchUmamusume()
    }
}