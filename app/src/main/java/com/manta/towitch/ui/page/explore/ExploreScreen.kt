package com.manta.towitch.ui.page.explore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.android.material.math.MathUtils.lerp
import com.manta.towitch.common.VSpacer
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.ui.page.home.MainViewModel
import com.manta.towitch.ui.theme.title_tab
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun ExploreScreen(mainViewModel: MainViewModel) {
    val streams = mainViewModel.recommendedStream.collectAsState()
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Text(
                "찾기",
                fontSize = title_tab,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp)
            )
            VSpacer(dp = 50.dp)
            if (streams.value.isNotEmpty()) {
                DiscreteScrollView(streams.value.take(8), scope)
            }

        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DiscreteScrollView(streams: List<Stream>, scope: CoroutineScope) {
    val state = rememberPagerState(streams.size * 5)
    LaunchedEffect(state) {
        scope.launch {
            state.scrollToPage(3)
        }
    }

    HorizontalPager(
        state = state,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        Box(
            Modifier
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                    lerp(0.85f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                        .also { scale ->
                            scaleX = scale
                            scaleY = scale

                        }

                    alpha = lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                }
        ) {
            GlideImage(
                imageModel = streams[page % streams.size].getSizedThumbnailUrl(1024, 512),
                modifier = Modifier
                    .width(300.dp)
                    .height(150.dp),
                contentScale = ContentScale.FillWidth
            ) {

            }
        }
    }


}