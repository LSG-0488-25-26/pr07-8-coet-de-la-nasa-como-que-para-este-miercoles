package com.example.umafacts.view.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.umafacts.ui.components.UmaItem
import com.example.umafacts.viewmodel.UmaViewModel
import kotlin.collections.emptyList

@Composable
fun UmamusumeListScreen(viewModel: UmaViewModel = viewModel()) {
    val umamusumeList by viewModel.umamusumeList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            error != null -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Error: $error")
                    Button(onClick = { viewModel.retry() }) {
                        Text("Retry")
                    }
                }
            }
            else -> {
                LazyColumn {
                    items(umamusumeList) { umaWithImage ->
                        UmaItem(
                            umamusume = umaWithImage.detail,
                            uniformImageUrl = umaWithImage.uniformImageUrl,
                            onClick = {
                                // Handle card click
                                println("Clicked ${umaWithImage.detail.nameEn}")
                            }
                        )
                    }
                }
            }
        }
    }
}
