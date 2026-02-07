package com.example.umafacts.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.umafacts.ui.components.UmaItem
import com.example.umafacts.view.components.EmptyListState
import com.example.umafacts.viewmodel.UmaViewModel

@Composable
fun UmaListScreen(
    viewModel: UmaViewModel,
    onCharacterClick: (Int) -> Unit
) {
    val umamusumeList by viewModel.umamusumeList.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState(null)

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading && umamusumeList.isEmpty() -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            error != null && umamusumeList.isEmpty() -> {
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Error: $error")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.refreshData() }) {
                        Text("Retry")
                    }
                }
            }

            umamusumeList.isEmpty() -> {
                EmptyListState(
                    modifier = Modifier.align(Alignment.Center),
                    onRefresh = { viewModel.refreshData() }
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        items = umamusumeList,
                        key = { it.detail.id }
                    ) { umaWithImage ->
                        UmaItem(
                            umamusume = umaWithImage.detail,
                            uniformImageUrl = umaWithImage.uniformImageUrl,
                            onClick = { onCharacterClick(umaWithImage.detail.id) }
                        )
                    }
                }
            }
        }
    }
}
