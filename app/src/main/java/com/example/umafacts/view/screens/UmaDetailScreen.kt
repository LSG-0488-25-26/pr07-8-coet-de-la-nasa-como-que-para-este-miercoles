package com.example.umafacts.view.screens

import androidx.core.net.toUri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.umafacts.R
import com.example.umafacts.model.UmamusumeDetail
import com.example.umafacts.ui.components.AudioPlayer
import com.example.umafacts.ui.components.FactsSection
import com.example.umafacts.ui.components.InfoCard
import com.example.umafacts.ui.components.OutfitGallery
import com.example.umafacts.utils.formatBirthday
import com.example.umafacts.utils.parseColor
import com.example.umafacts.utils.textColorFor
import com.example.umafacts.viewmodel.UmaDetailState
import com.example.umafacts.viewmodel.UmaDetailViewModel
import com.example.umafacts.viewmodel.UmaViewModel
import com.example.umafacts.viewmodel.FavouritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UmaDetailScreen(
    viewModel: UmaViewModel,
    favouritesViewModel: FavouritesViewModel,
    characterId: Int,
    onBackClick: () -> Unit,
    detailViewModel: UmaDetailViewModel = viewModel()
) {
    val umamusumeList by viewModel.umamusumeList.observeAsState(initial = emptyList())
    // 1. Observe favourites so we can load data even if network list is empty
    val favourites by favouritesViewModel.getAllFavourites().observeAsState(initial = emptyList())

    val state by detailViewModel.state.observeAsState(initial = UmaDetailState())
    val isFavourite by favouritesViewModel.isFavourite(characterId).observeAsState(false)

    // 2. Find character: Check Network List first, then check Local Database
    val character: UmamusumeDetail? = remember(umamusumeList, favourites, characterId) {
        umamusumeList.find { it.detail.id == characterId }?.detail
            ?: favourites.find { it.characterId == characterId }?.toUmamusumeDetail()
    }

    LaunchedEffect(character) {
        if (character != null) {
            detailViewModel.loadCharacterDetails(character)
        }
    }

    // Handle null character (Loading/Error)
    if (character == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Safe Color Parsing
    val safeColorMain = character.colorMain.takeIf { !it.isNullOrBlank() } ?: "#808080"
    val safeColorSub = character.colorSub.takeIf { !it.isNullOrBlank() } ?: "#A0A0A0"
    val mainColor = parseColor(safeColorMain)
    val subColor = parseColor(safeColorSub)
    val onMainColor = textColorFor(mainColor)
    val onSubColor = textColorFor(subColor)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = character.nameEn.takeIf { !it.isNullOrBlank() } ?: "Unknown Character",
                        color = onMainColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            modifier = Modifier.scale(1.1f),
                            tint = onMainColor
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // 3. CORRECTED CALL: Use the 'character' object and its thumbnail
                            favouritesViewModel.toggleFavourite(
                                detail = character,
                                uniformImageUrl = character.thumbImg // Use thumbnail as the cached image
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavourite) "Remove from favourites" else "Add to favourites",
                            tint = if (isFavourite) Color.Red else onMainColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = mainColor,
                    titleContentColor = onMainColor,
                    navigationIconContentColor = onMainColor,
                    actionIconContentColor = onMainColor
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(mainColor)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = onMainColor
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HeaderSection(
                        character = character,
                        backgroundColor = mainColor,
                        contentColor = onMainColor
                    )

                    OutfitGallery(
                        images = state.images,
                        backgroundColor = mainColor,
                        contentColor = onMainColor
                    )

                    CompositionLocalProvider(LocalContentColor provides onSubColor) {
                        // Safe birthday formatting
                        val birthdayText = try {
                            formatBirthday(character.birthDay, character.birthMonth)
                        } catch (e: Exception) {
                            "Unknown"
                        }

                        InfoCard(
                            title = "Birthday",
                            content = birthdayText,
                            backgroundColor = subColor
                        )

                        // (Assuming PhysicalInfoCard is defined in this file or imported)
                        PhysicalInfoCard(
                            character = character,
                            backgroundColor = subColor,
                            contentColor = onSubColor
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (!character.grade.isNullOrBlank()) {
                                InfoCard(
                                    title = "Grade",
                                    content = character.grade,
                                    backgroundColor = subColor,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (!character.residence.isNullOrBlank()) {
                                InfoCard(
                                    title = "Residence",
                                    content = character.residence,
                                    backgroundColor = subColor,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                            FactsSection(
                                earsFact = character.earsFact as? String,
                                tailFact = character.tailFact as? String,
                                familyFact = character.familyFact as? String,
                                backgroundColor = subColor
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val strengths = character.strengths?.toString()?.takeIf { it.isNotBlank() }
                                val weaknesses = character.weaknesses?.toString()?.takeIf { it.isNotBlank() }

                                if (!strengths.isNullOrBlank()) {
                                    InfoCard(
                                        title = "Strengths",
                                        content = strengths,
                                        backgroundColor = subColor,
                                        modifier = Modifier.weight(1f)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }

                                if (!weaknesses.isNullOrBlank()) {
                                    InfoCard(
                                        title = "Weaknesses",
                                        content = weaknesses,
                                        backgroundColor = subColor,
                                        modifier = Modifier.weight(1f)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

                            if (!character.profile.isNullOrBlank()) {
                                InfoCard(
                                    title = "Profile",
                                    content = character.profile,
                                    backgroundColor = subColor
                                )
                            }

                            if (!character.slogan.isNullOrBlank()) {
                                InfoCard(
                                    title = "Slogan",
                                    content = character.slogan,
                                    backgroundColor = subColor
                                )
                            }
                        }

                        // Audio player for voice preview
                        if (!character.voice.isNullOrBlank() && isValidUrl(character.voice)) {
                            AudioPlayer(
                                audioUrl = character.voice,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

// Helper function to check URL validity
private fun isValidUrl(url: String?): Boolean {
    return try {
        url?.toUri() != null
    } catch (e: Exception) {
        false
    }
}

@Composable
private fun HeaderSection(
    character: com.example.umafacts.model.UmamusumeDetail,
    backgroundColor: Color,
    contentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = character.nameEn.takeIf { !it.isNullOrBlank() } ?: "Unknown Umamusume",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = character.nameJp.takeIf { !it.isNullOrBlank() } ?: "名前不明",
                style = MaterialTheme.typography.titleLarge,
                color = contentColor.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun PhysicalInfoCard(
    character: com.example.umafacts.model.UmamusumeDetail,
    backgroundColor: Color,
    contentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Physical Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (character.height != null && character.height > 0) {
                Text("Height: ${character.height} cm")
            }

            val weight = character.weight
            if (weight != null && weight.toString().isNotBlank()) {
                Text("Weight: $weight")
            }

            val shoeSize = character.shoeSize
            if (shoeSize != null && shoeSize.toString().isNotBlank()) {
                Text("Shoe Size: $shoeSize")
            }
        }
    }
}