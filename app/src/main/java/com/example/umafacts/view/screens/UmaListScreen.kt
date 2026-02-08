package com.example.umafacts.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
    val searchQuery by viewModel.searchQuery.observeAsState("")

    val favourites by favouritesViewModel.getAllFavourites().observeAsState(emptyList())
    val favouriteIds = remember(favourites) { favourites.map { it.characterId }.toSet() }

    val nonFavouriteCharacters = remember(umamusumeList, favouriteIds, searchQuery) {
        if (searchQuery.isBlank()) {
            umamusumeList.filter { it.detail.id !in favouriteIds }
        } else {
            umamusumeList
        }
    }

    val listState = rememberLazyListState()

    LaunchedEffect(searchQuery) {
        listState.scrollToItem(0)
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
            .collect { last ->
                val index = last?.index ?: return@collect
                if (index >= listState.layoutInfo.totalItemsCount - 5 &&
                    !isLoadingMore && !isLoading
                ) {
                    viewModel.loadNextPage()
                }
            }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Umamusume Facts",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::updateSearchQuery,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search characters...") },
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(nonFavouriteCharacters, key = { it.detail.id }) { uma ->
                UmaItem(
                    umamusume = uma.detail,
                    uniformImageUrl = uma.uniformImageUrl,
                    onClick = { onCharacterClick(uma.detail.id) }
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
