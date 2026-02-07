package com.example.umafacts.ui.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.umafacts.viewmodel.AudioPlayerViewModel

@Composable
fun AudioPlayer(
    audioUrl: String,
    modifier: Modifier = Modifier,
    viewModel: AudioPlayerViewModel = viewModel()
) {
    val context = LocalContext.current
    val playerState by viewModel.playerState.collectAsState()

    // Initialize the player once when the composable is first created
    LaunchedEffect(Unit) {
        viewModel.initializePlayer(context)
    }

    // Check if this is the currently playing URL
    val isCurrentAudio = playerState.currentUrl == audioUrl

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (playerState.error != null && isCurrentAudio) {
            Text(
                text = playerState.error ?: "Audio error",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isCurrentAudio && playerState.isPlaying) {
                // Show pause button when playing
                IconButton(
                    onClick = { viewModel.pauseAudio() },
                    enabled = !playerState.isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.Pause, // Changed from PlayArrow to Pause
                        contentDescription = "Pause",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                // Show stop button
                IconButton(
                    onClick = { viewModel.stopAudio() },
                    enabled = !playerState.isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Stop",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            } else if (isCurrentAudio && !playerState.isPlaying && playerState.currentUrl != null) {
                // Show play/resume button for paused/stopped audio
                IconButton(
                    onClick = { viewModel.resumeAudio() },
                    enabled = !playerState.isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                // Show play button for new audio
                Button(
                    onClick = { viewModel.playAudio(audioUrl) },
                    enabled = !playerState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (playerState.isLoading && isCurrentAudio) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Loading...")
                    } else {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Listen to Voice Preview")
                    }
                }
            }
        }

        if (playerState.isLoading && isCurrentAudio) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}