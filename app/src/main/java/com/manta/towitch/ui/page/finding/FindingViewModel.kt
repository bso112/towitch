package com.manta.towitch.ui.page.finding

import androidx.lifecycle.ViewModel
import com.manta.towitch.data.entity.Clip
import com.manta.towitch.data.entity.Game
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.domain.FetchClipsUseCase
import com.manta.towitch.domain.FetchStreamUseCase
import com.manta.towitch.domain.FetchTopGamesUseCase
import com.manta.towitch.utils.toStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FindingViewModel @Inject constructor(
    fetchClipsUseCase: FetchClipsUseCase,
    fetchStreamUseCase: FetchStreamUseCase,
    fetchTopGamesUseCase: FetchTopGamesUseCase
) : ViewModel() {

    val games: StateFlow<List<Game>> = fetchTopGamesUseCase().toStateFlow(this, emptyList())

    val streams: StateFlow<List<Stream>> = fetchStreamUseCase().toStateFlow(this, emptyList())

    val recommendedStreams = streams.map { it.shuffled().take(10) }

    val smallStreams = streams.map { it.reversed().take(10) }

    val justcChattingStreams =
        streams.map { it.asSequence().filter { it.gameName == "Just Chatting" }.take(10).toList() }

    val clips: StateFlow<List<Clip>> =
        fetchClipsUseCase().toStateFlow(this, emptyList())
}