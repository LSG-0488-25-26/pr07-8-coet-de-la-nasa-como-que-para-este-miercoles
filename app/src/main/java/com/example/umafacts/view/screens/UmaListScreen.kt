package com.example.umafacts.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.umafacts.ui.components.UmaItem
import com.example.umafacts.viewmodel.FavouritesViewModel
import com.example.umafacts.viewmodel.UmamusumeWithImage
import com.example.umafacts.viewmodel.UmaViewModel

enum class BottomBarScreen(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    All("All", Icons.Default.List),
    Favorites("Favorites", Icons.Default.Favorite)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UmaListScreen(
    viewModel: UmaViewModel,
    favouritesViewModel: FavouritesViewModel,
    onCharacterClick: (Int) -> Unit
) {
    var selectedScreen by remember { mutableStateOf(BottomBarScreen.All) }

    val umamusumeList by viewModel.umamusumeList.observeAsState(emptyList())
    val searchQuery by viewModel.searchQuery.observeAsState("")
    val isLoadingMore by viewModel.isLoadingMore.observeAsState(false)

    val favourites by favouritesViewModel.getAllFavourites().observeAsState(emptyList())
    val favouriteIds = remember(favourites) { favourites.map { it.characterId }.toSet() }

    val listState = rememberLazyListState()

    // Scroll to top on search or tab change
    LaunchedEffect(searchQuery, selectedScreen) {
        listState.scrollToItem(0)
    }

    // Infinite scroll only on All tab
    LaunchedEffect(listState, selectedScreen) {
        if (selectedScreen == BottomBarScreen.All) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
                .collect { lastVisible ->
                    val index = lastVisible?.index ?: return@collect
                    val totalItems = listState.layoutInfo.totalItemsCount
                    if (index >= totalItems - 5 && !isLoadingMore) {
                        viewModel.loadNextPage()
                    }
                }
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .statusBarsPadding()
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
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search characters...") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                )
            }
        },
        bottomBar = {
            NavigationBar {
                BottomBarScreen.values().forEach { screen ->
                    NavigationBarItem(
                        selected = selectedScreen == screen,
                        onClick = { selectedScreen = screen },
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) }
                    )
                }
            }
        }
    ) { paddingValues ->

        val displayList: List<UmamusumeWithImage> = when (selectedScreen) {
            BottomBarScreen.All -> {
                // Mostrar todas, sin filtrar por favoritos
                if (searchQuery.isBlank()) {
                    umamusumeList
                } else {
                    umamusumeList.filter {
                        it.detail.nameEn.contains(searchQuery, ignoreCase = true)
                                || it.detail.nameJp.contains(searchQuery, ignoreCase = true)
                    }
                }
            }
            BottomBarScreen.Favorites -> favourites.map { fav ->
                UmamusumeWithImage(fav.toUmamusumeDetail(), fav.thumbImg)
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(displayList, key = { it.detail.id }) { uma ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Item click area
                    Box(modifier = Modifier.weight(1f)) {
                        UmaItem(
                            umamusume = uma.detail,
                            uniformImageUrl = uma.uniformImageUrl,
                            onClick = { onCharacterClick(uma.detail.id) }
                        )
                    }

                    // Botón de favorito
                    IconButton(
                        onClick = {
                            favouritesViewModel.toggleFavourite(
                                uma.detail,
                                uma.uniformImageUrl ?: ""
                            )
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        val isFav = favourites.any { it.characterId == uma.detail.id }
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = if (isFav) "Remove favorite" else "Add favorite",
                            tint = if (isFav) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            if (selectedScreen == BottomBarScreen.All && isLoadingMore) {
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
