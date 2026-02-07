package com.example.umafacts.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AudioPlayerState(
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentUrl: String? = null
)

class AudioPlayerViewModel : ViewModel() {
    private var player: ExoPlayer? = null

    private val _playerState = MutableStateFlow(AudioPlayerState())
    val playerState: StateFlow<AudioPlayerState> = _playerState

    fun initializePlayer(context: Context) {
        player = ExoPlayer.Builder(context)
            .build()
            .apply {
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_BUFFERING -> {
                                _playerState.value = _playerState.value.copy(
                                    isLoading = true,
                                    isPlaying = false
                                )
                            }
                            Player.STATE_READY -> {
                                _playerState.value = _playerState.value.copy(
                                    isLoading = false,
                                    isPlaying = this@apply.isPlaying
                                )
                            }
                            Player.STATE_ENDED -> {
                                _playerState.value = _playerState.value.copy(
                                    isPlaying = false,
                                    isLoading = false
                                )
                            }
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        _playerState.value = _playerState.value.copy(
                            error = "Failed to play audio: ${error.message}",
                            isPlaying = false,
                            isLoading = false
                        )
                    }
                })
            }
    }

    fun playAudio(url: String) {
        viewModelScope.launch {
            try {
                _playerState.value = _playerState.value.copy(
                    isLoading = true,
                    currentUrl = url
                )

                player?.let {
                    // Stop current playback if any
                    if (it.isPlaying) {
                        it.stop()
                    }

                    // Set up new media item
                    val mediaItem = MediaItem.fromUri(Uri.parse(url))
                    it.setMediaItem(mediaItem)
                    it.prepare()
                    it.play()

                    _playerState.value = _playerState.value.copy(
                        isLoading = false,
                        isPlaying = true
                    )
                }
            } catch (e: Exception) {
                _playerState.value = AudioPlayerState(
                    error = "Failed to play audio: ${e.message}",
                    currentUrl = url
                )
            }
        }
    }

    fun stopAudio() {
        player?.stop()
        _playerState.value = AudioPlayerState()
    }

    fun pauseAudio() {
        player?.pause()
        _playerState.value = _playerState.value.copy(isPlaying = false)
    }

    fun resumeAudio() {
        player?.play()
        _playerState.value = _playerState.value.copy(isPlaying = true)
    }

    fun clearError() {
        _playerState.value = _playerState.value.copy(error = null)
    }

    override fun onCleared() {
        super.onCleared()
        player?.release()
        player = null
    }
}