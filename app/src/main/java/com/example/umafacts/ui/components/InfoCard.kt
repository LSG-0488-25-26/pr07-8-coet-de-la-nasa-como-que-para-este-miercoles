package com.example.umafacts.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun InfoCard(
    title: String,
    content: String?,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    if (!content.isNullOrBlank()) {
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun FactsSection(
    earsFact: String?,
    tailFact: String?,
    familyFact: String?,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    val hasFacts = !earsFact.isNullOrBlank() ||
            !tailFact.isNullOrBlank() ||
            !familyFact.isNullOrBlank()

    if (hasFacts) {
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Fun Facts",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (!earsFact.isNullOrBlank()) {
                    FactItem(label = "Ears", fact = earsFact)
                }

                if (!tailFact.isNullOrBlank()) {
                    FactItem(label = "Tail", fact = tailFact)
                }

                if (!familyFact.isNullOrBlank()) {
                    FactItem(label = "Family", fact = familyFact)
                }
            }
        }
    }
}

@Composable
private fun FactItem(label: String, fact: String) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = fact,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
        )
    }
}