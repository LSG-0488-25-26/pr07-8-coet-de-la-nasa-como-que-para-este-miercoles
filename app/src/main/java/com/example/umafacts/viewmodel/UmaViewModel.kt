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

class UmaViewModel : ViewModel() {
    private val repository = Repository(APIInterface.create())

    private val _umamusumeList = MutableStateFlow<List<UmamusumeDetail>>(emptyList())
    val umamusumeList: StateFlow<List<UmamusumeDetail>> = _umamusumeList.asStateFlow()

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
                    _umamusumeList.value = umamusumeList
                    println("Fetched ${umamusumeList.size} characters")
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