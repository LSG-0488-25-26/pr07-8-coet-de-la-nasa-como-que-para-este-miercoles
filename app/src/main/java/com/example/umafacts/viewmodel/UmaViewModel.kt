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

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchUmamusume()
    }

    private fun fetchUmamusume() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getUmamusumeList()
                .onSuccess { umamusumeList ->
                    // Fetch images for each character
                    val listWithImages = umamusumeList.map { uma ->
                        val imageResult = repository.getUniformImage(uma.id)
                        UmamusumeWithImage(
                            detail = uma,
                            uniformImageUrl = imageResult.getOrNull()
                        )
                    }
                    _umamusumeList.value = listWithImages
                    println("Fetched ${listWithImages.size} characters with images")
                }
                .onFailure { error ->
                    _error.value = error.message
                    println("Error: ${error.message}")
                }

            _isLoading.value = false
        }
    }

    fun retry() {
        fetchUmamusume()
    }
}