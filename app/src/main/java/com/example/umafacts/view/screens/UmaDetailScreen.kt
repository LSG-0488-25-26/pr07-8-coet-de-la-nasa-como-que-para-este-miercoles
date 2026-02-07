package com.example.umafacts.view.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState // ✅ Necessary import
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
import com.example.umafacts.ui.components.FactsSection
import com.example.umafacts.ui.components.InfoCard
import com.example.umafacts.ui.components.OutfitGallery
import com.example.umafacts.utils.formatBirthday
import com.example.umafacts.utils.parseColor
import com.example.umafacts.utils.textColorFor
import com.example.umafacts.viewmodel.UmaDetailState
import com.example.umafacts.viewmodel.UmaDetailViewModel
import com.example.umafacts.viewmodel.UmaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UmaDetailScreen(
    viewModel: UmaViewModel,
    characterId: Int,
    onBackClick: () -> Unit,
    detailViewModel: UmaDetailViewModel = viewModel()
) {
    val umamusumeList by viewModel.umamusumeList.observeAsState(initial = emptyList())
    val state by detailViewModel.state.observeAsState(initial = UmaDetailState())

    val context = LocalContext.current

    // Find the character from the observed list
    val character = remember(umamusumeList, characterId) {
        umamusumeList.find { it.detail.id == characterId }?.detail
    }

    LaunchedEffect(character) {
        character?.let {
            detailViewModel.loadCharacterDetails(it)
        }
    }

    if (character == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // FIX: Use safe defaults for colors
    val safeColorMain = character.colorMain.takeIf { !it.isNullOrBlank() } ?: "#808080"
    val safeColorSub = character.colorSub.takeIf { !it.isNullOrBlank() } ?: "#A0A0A0"

    val mainColor = parseColor(safeColorMain)
    val subColor = parseColor(safeColorSub)
    val onMainColor = textColorFor(mainColor)
    val onSubColor = textColorFor(subColor)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(character.nameEn) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = Color.Unspecified,
                            modifier = Modifier.scale(1.1f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = mainColor,
                    titleContentColor = onMainColor,
                    navigationIconContentColor = onMainColor
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
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = onMainColor
                    )
                }
                state.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Error loading images", color = onMainColor)
                        Text(
                            text = state.error ?: "",
                            color = onMainColor.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                else -> {
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
                            InfoCard(
                                title = "Birthday",
                                content = formatBirthday(character.birthDay, character.birthMonth),
                                backgroundColor = subColor
                            )

                            PhysicalInfoCard(
                                character = character,
                                backgroundColor = subColor,
                                contentColor = onSubColor
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (character.grade.isNotBlank()) {
                                    InfoCard(
                                        title = "Grade",
                                        content = character.grade,
                                        backgroundColor = subColor,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (character.residence.isNotBlank()) {
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
                                InfoCard(
                                    title = "Strengths",
                                    content = character.strengths as? String,
                                    backgroundColor = subColor,
                                    modifier = Modifier.weight(1f)
                                )
                                InfoCard(
                                    title = "Weaknesses",
                                    content = character.weaknesses as? String,
                                    backgroundColor = subColor,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            if (character.profile.isNotBlank()) {
                                InfoCard(title = "Profile", content = character.profile, backgroundColor = subColor)
                            }

                            if (character.slogan.isNotBlank()) {
                                InfoCard(title = "Slogan", content = character.slogan, backgroundColor = subColor)
                            }
                        }

                        if (character.voice.isNotBlank()) {
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(character.voice))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = subColor,
                                    contentColor = onSubColor
                                )
                            ) {
                                Text("Listen to Voice Preview")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
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
            contentColor = contentColor // Apply content color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = character.nameEn,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = character.nameJp,
                style = MaterialTheme.typography.titleLarge,
                // Use the content color with slight alpha for secondary text
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
            contentColor = contentColor // Apply content color
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

            if (character.height > 0) {
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