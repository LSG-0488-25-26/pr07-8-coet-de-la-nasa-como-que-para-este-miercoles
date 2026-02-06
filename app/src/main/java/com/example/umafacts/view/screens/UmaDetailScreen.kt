package com.example.umafacts.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.umafacts.model.UmamusumeDetail
import com.example.umafacts.R
import com.example.umafacts.viewmodel.UmaViewModel
import androidx.compose.ui.Alignment
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UmaDetailScreen(
    viewModel: UmaViewModel = viewModel(),
    characterId: Int,
    onBack: () -> Unit
) {
    // Load character detail when this screen appears
    LaunchedEffect(characterId) {
        viewModel.loadCharacterDetail(characterId)
    }

    // Observe LiveData instead of mutableStateOf
    val character by viewModel.selectedCharacter.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(character.nameEn ?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (character == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            CharacterDetailContent(character!!, paddingValues)
        }
    }
}


@Composable
fun CharacterDetailContent(character: UmamusumeDetail, paddingValues: PaddingValues) {
    // Scrollable layout
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ----- Character Image -----
        item {
            val imageId = try {
                val field = R.drawable::class.java.getField(character.nameInternal)
                field.getInt(null)
            } catch (e: Exception) {
                R.drawable.uma_placeholder
            }

            Image(
                painter = painterResource(id = imageId),
                contentDescription = character.nameEn,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        // ----- Name & Profile -----
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    character.nameEn?.let {
                        Text(it, style = MaterialTheme.typography.headlineSmall)
                    }
                    character.nameJp?.let {
                        Text(it, style = MaterialTheme.typography.bodyMedium)
                    }
                    Text(character.profile ?: "No description available.")
                }
            }
        }

        // ----- Birthday -----
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Birthday: ${character.birthDay ?: "--"}/${character.birthMonth ?: "--"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // ----- Category -----
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Category: ${character.categoryLabel ?: "--"} / ${character.enCategoryLabel ?: "--"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Category Value: ${character.categoryValue ?: "--"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // ----- Physical Stats -----
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Height: ${character.height}", style = MaterialTheme.typography.bodyMedium)
                    Text("Weight: ${character.weight ?: "--"}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // ----- Facts -----
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Ears fact: ${character.earsFact ?: "--"}")
                    Text("Tail fact: ${character.tailFact ?: "--"}")
                    Text("Family fact: ${character.familyFact ?: "--"}")
                }
            }
        }

        // ----- Abilities -----
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Strengths: ${character.strengths ?: "--"}")
                    Text("Weaknesses: ${character.weaknesses ?: "--"}")
                }
            }
        }

        // ----- Game Info -----
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Game ID: ${character.gameId}")
                    Text("Grade: ${character.grade ?: "--"}")
                    Text("Row number: ${character.rowNumber}")
                }
            }
        }

        // ----- Slogan & Voice -----
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Slogan: ${character.slogan ?: "--"}")
                }
            }
        }

        // ----- Residence -----
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Residence: ${character.residence ?: "--"}")
                }
            }
        }
    }
}

