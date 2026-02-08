package com.example.umafacts.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.umafacts.R
import com.example.umafacts.ui.components.UmaItem
import com.example.umafacts.viewmodel.FavouritesViewModel
import com.example.umafacts.viewmodel.UmaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UmaListScreen(
    viewModel: UmaViewModel,
    favouritesViewModel: FavouritesViewModel,
    onCharacterClick: (Int) -> Unit
) {
    val umamusumeList by viewModel.umamusumeList.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isLoadingMore by viewModel.isLoadingMore.observeAsState(false)
    val error by viewModel.error.observeAsState(null)

    // Observe favorites from Room (Contains cached data now)
    val favourites by favouritesViewModel.getAllFavourites().observeAsState(emptyList())

    // 1. Get Set of IDs for filtering the main list
    val favouriteIds = remember(favourites) { favourites.map { it.characterId }.toSet() }

    // 2. Filter main list to exclude favourites (avoid duplicates)
    val nonFavouriteCharacters = remember(umamusumeList, favouriteIds) {
        umamusumeList.filter { it.detail.id !in favouriteIds }
    }

    val listState = rememberLazyListState()

    // Pagination Logic
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

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(top = 40.dp)
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Umamusume Facts",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                // Show loading only if we have NO data at all (neither favorites nor network list)
                isLoading && umamusumeList.isEmpty() && favourites.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text("Loading characters...", modifier = Modifier.padding(top = 16.dp))
                    }
                }

                error != null && umamusumeList.isEmpty() && favourites.isEmpty() -> {
                    // Error state (only if we have absolutely nothing to show)
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(painter = painterResource(R.drawable.uma_placeholder), contentDescription = null, modifier = Modifier.size(120.dp))
                        Text(text = error ?: "Error", color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.refreshData() }) { Text("Retry") }
                    }
                }

                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        // --- FAVOURITES SECTION (Powered by Local DB) ---
                        if (favourites.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Favourites ★",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                            // Directly iterate over DB objects
                            items(
                                items = favourites,
                                key = { it.characterId }
                            ) { favEntity ->
                                // Reconstruct detail object from cache
                                UmaItem(
                                    umamusume = favEntity.toUmamusumeDetail(),
                                    uniformImageUrl = favEntity.thumbImg,
                                    onClick = { onCharacterClick(favEntity.characterId) }
                                )
                            }

                            item {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                            }
                        }

                        // --- ALL CHARACTERS SECTION (Powered by Network) ---
                        items(
                            items = nonFavouriteCharacters,
                            key = { it.detail.id }
                        ) { umaWithImage ->
                            UmaItem(
                                umamusume = umaWithImage.detail,
                                uniformImageUrl = umaWithImage.uniformImageUrl,
                                onClick = { onCharacterClick(umaWithImage.detail.id) }
                            )
                        }

                        if (isLoadingMore) {
                            item {
                                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}