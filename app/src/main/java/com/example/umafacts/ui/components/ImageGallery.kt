package com.example.umafacts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.umafacts.model.CharacterImageResponse

@Composable
fun OutfitGallery(
    images: List<CharacterImageResponse>,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    // Find the first occurrence of each outfit type
    val uniform = images.firstOrNull { it.labelEn == "Uniform" }
    val racewear = images.firstOrNull { it.labelEn == "Racewear" }

    if (uniform != null || racewear != null) {
        Column(modifier = modifier) {
            Text(
                text = "Outfits",
                style = MaterialTheme.typography.titleLarge,
                color = contentColor, // Apply dynamic content color
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Display Uniform if it exists
                uniform?.let {
                    SingleOutfitCard(
                        outfit = it,
                        backgroundColor = backgroundColor,
                        contentColor = contentColor,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Display Racewear if it exists
                racewear?.let {
                    SingleOutfitCard(
                        outfit = it,
                        backgroundColor = backgroundColor,
                        contentColor = contentColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun SingleOutfitCard(
    outfit: CharacterImageResponse,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = outfit.labelEn,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Show only the first image from the list
            val displayImage = outfit.images.firstOrNull()?.image

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp) // Adjusted height for side-by-side view
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                AsyncImage(
                    model = displayImage,
                    contentDescription = outfit.labelEn,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}