package com.manta.towitch.ui.page.explore

import androidx.lifecycle.ViewModel
import com.manta.towitch.data.entity.Game
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.domain.FetchStreamUseCase
import com.manta.towitch.domain.FetchTopGamesUseCase
import com.manta.towitch.utils.toStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    fetchStreamUseCase: FetchStreamUseCase,
    fetchTopGamesUseCase: FetchTopGamesUseCase
) : ViewModel() {
    val games: StateFlow<List<Game>> = fetchTopGamesUseCase().toStateFlow(this, emptyList())
    val streams: StateFlow<List<Stream>> = fetchStreamUseCase().toStateFlow(this, emptyList())

}