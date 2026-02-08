package com.example.umafacts.view.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.umafacts.view.screens.UmaDetailScreen
import com.example.umafacts.view.screens.UmaListScreen
import com.example.umafacts.viewmodel.FavouritesViewModel
import com.example.umafacts.viewmodel.UmaViewModel

@Composable
fun NavGraph(
    viewModel: UmaViewModel,
    favouritesViewModel: FavouritesViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {

        composable("list") {
            UmaListScreen(
                viewModel = viewModel,
                onCharacterClick = { characterId ->
                    navController.navigate("detail/$characterId")
                },
                favouritesViewModel = favouritesViewModel
            )
        }

        composable(
            route = "detail/{characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.IntType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt("characterId") ?: 0
            UmaDetailScreen(
                viewModel = viewModel,
                characterId = characterId,
                onBackClick = { navController.popBackStack() },
                favouritesViewModel = favouritesViewModel
            )
        }
    }
}