package com.example.umafacts.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.umafacts.ui.components.UmaItem
import com.example.umafacts.viewmodel.UmaViewModel

@Composable
fun UmaListScreen(viewModel: UmaViewModel = viewModel()) {
    val umamusumeList by viewModel.umamusumeList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val error by viewModel.error.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
            .collect { lastVisibleItem ->
                if (lastVisibleItem != null) {
                    val lastIndex = lastVisibleItem.index
                    val totalItems = listState.layoutInfo.totalItemsCount

                    if (lastIndex >= totalItems - 5 && !isLoadingMore && !isLoading) {
                        viewModel.loadNextPage()
                    }
                }
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading && umamusumeList.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            error != null && umamusumeList.isEmpty() -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Error: $error")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.retry() }) {
                        Text("Retry")
                    }
                }
            }
            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = umamusumeList,
                        key = { it.detail.id }
                    ) { umaWithImage ->
                        UmaItem(
                            umamusume = umaWithImage.detail,
                            uniformImageUrl = umaWithImage.uniformImageUrl,
                            onClick = {
                                println("Clicked ${umaWithImage.detail.nameEn}")
                            }
                        )
                    }

                    if (isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}