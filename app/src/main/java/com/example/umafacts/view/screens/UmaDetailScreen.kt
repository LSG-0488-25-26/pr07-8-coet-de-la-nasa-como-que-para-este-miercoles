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
import com.example.apicultura.model.CharacterDetail
import com.example.apicultura.R
import com.example.apicultura.viewmodel.UmaViewModel
import androidx.compose.ui.Alignment
import androidx.compose.runtime.livedata.observeAsState

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
                title = { Text(character?.name_en ?: "Loading...") },
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
fun CharacterDetailContent(character: CharacterDetail, paddingValues: PaddingValues) {
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
                val field = R.drawable::class.java.getField(character.name_en_internal)
                field.getInt(null)
            } catch (e: Exception) {
                R.drawable.uma_placeholder
            }

            Image(
                painter = painterResource(id = imageId),
                contentDescription = character.name_en,
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
                    character.name_en?.let {
                        Text(it, style = MaterialTheme.typography.headlineSmall)
                    }
                    character.name_jp?.let {
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
                        text = "Birthday: ${character.birth_month ?: "--"}/${character.birth_day ?: "--"}",
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
                        text = "Category: ${character.category_label ?: "--"} / ${character.category_label_en ?: "--"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Category Value: ${character.category_value ?: "--"}",
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
                    Text("Ears fact: ${character.ears_fact ?: "--"}")
                    Text("Tail fact: ${character.tail_fact ?: "--"}")
                    Text("Family fact: ${character.family_fact ?: "--"}")
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
                    Text("Game ID: ${character.game_id}")
                    Text("Grade: ${character.grade ?: "--"}")
                    Text("Row number: ${character.row_number}")
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

