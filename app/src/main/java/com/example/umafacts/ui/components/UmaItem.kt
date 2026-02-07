package com.example.umafacts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import com.example.umafacts.R
import com.example.umafacts.model.UmamusumeDetail

@Composable
fun UmaItem(
    umamusume: UmamusumeDetail,
    uniformImageUrl: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val safeNameEn = umamusume.nameEn.takeIf { !it.isNullOrBlank() } ?: "Unknown Umamusume"
    val safeNameJp = umamusume.nameJp.takeIf { !it.isNullOrBlank() } ?: "名前不明"
    val safeGrade = umamusume.grade.takeIf { !it.isNullOrBlank() } ?: "—"

    val safeHeightText = if (umamusume.height != null && umamusume.height > 0) {
        "${umamusume.height} cm"
    } else {
        "Height N/A"
    }

    // FIX: Add null safety for colorMain
    val safeColor = remember(umamusume.colorMain) {
        try {
            umamusume.colorMain?.takeIf { it.isNotBlank() }?.let {
                Color(it.toColorInt())
            } ?: Color(0xFFB0BEC5) // neutral fallback color
        } catch (_: Exception) {
            Color(0xFFB0BEC5) // neutral fallback color
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image with Placeholder and Error handling
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = uniformImageUrl,
                    contentDescription = "$safeNameEn uniform",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.uma_placeholder),
                    error = painterResource(R.drawable.uma_placeholder),
                    fallback = painterResource(R.drawable.uma_placeholder)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info Section
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = safeNameEn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = safeNameJp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Badge(containerColor = safeColor) {
                        Text(
                            text = safeGrade,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Black
                        )
                    }

                    Text(
                        text = safeHeightText,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun Badge(
    containerColor: Color,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = containerColor,
        modifier = Modifier.padding(2.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            content()
        }
    }
}

@Composable
fun EmptySearchState(
    query: String,
    modifier: Modifier = Modifier,
    onClearSearch: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // You can use a specific icon or your uma_placeholder here
        Icon(
            painter = painterResource(id = R.drawable.uma_placeholder), // Replace with a valid icon ID
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.Gray.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No results for \"$query\"",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )

        Text(
            text = "Check the spelling or try a different name.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onClearSearch,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Clear Search")
        }
    }
}