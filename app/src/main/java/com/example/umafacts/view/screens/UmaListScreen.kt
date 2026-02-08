package com.example.umafacts.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    val error by viewModel.error.observeAsState(null)

    // Observe favorites from Room database (LiveData)
    val favourites by favouritesViewModel.getAllFavourites().observeAsState(emptyList())

    // Separate favorites from non-favorites
    val favouriteIds = favourites.map { it.characterId }.toSet()
    val favouriteCharacters = umamusumeList.filter { it.detail.id in favouriteIds }
    val nonFavouriteCharacters = umamusumeList.filter { it.detail.id !in favouriteIds }

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
                isLoading && umamusumeList.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading characters...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                error != null && umamusumeList.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.uma_placeholder),
                            contentDescription = "Error",
                            modifier = Modifier.size(120.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = error ?: "Unknown error occurred",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "The server may be temporarily unavailable. Please check your connection and try again.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(onClick = { viewModel.refreshData() }) {
                                Text("Retry")
                            }
                            OutlinedButton(onClick = { viewModel.clearError() }) {
                                Text("Dismiss")
                            }
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
                        // Show favorites section if there are any
                        if (favouriteCharacters.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Favourites ★",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                            items(
                                items = favouriteCharacters,
                                key = { it.detail.id }
                            ) { umaWithImage ->
                                UmaItem(
                                    umamusume = umaWithImage.detail,
                                    uniformImageUrl = umaWithImage.uniformImageUrl,
                                    onClick = { onCharacterClick(umaWithImage.detail.id) }
                                )
                            }

                            item {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                            }
                        }

                        // Show all characters
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
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyListState(
    modifier: Modifier = Modifier,
    onRefresh: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.uma_placeholder),
            contentDescription = "No characters available",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Characters Found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "There are no Umamusume characters to display.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (onRefresh != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRefresh,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Refresh List")
            }
        }
    }
}