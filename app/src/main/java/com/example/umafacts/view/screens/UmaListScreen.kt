package com.example.umafacts.view.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.example.umafacts.viewmodel.UmaViewModel
import com.example.umafacts.view.components.UmaItem
import kotlin.collections.emptyList

@Composable
fun UmaListScreen(viewModel: UmaViewModel, navController: NavController) {
    // Observe LiveData
    val characterList by viewModel.characterList.observeAsState(emptyList())

    LazyColumn {
        items(characterList) { character ->
            UmaItem(character = character) {
                navController.navigate("detail/${character.id}")
            }
        }
    }
}
