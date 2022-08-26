package com.manta.towitch.ui.page.explore

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import com.google.android.material.math.MathUtils.lerp
import com.manta.towitch.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import com.manta.towitch.common.HCenter
import com.manta.towitch.common.HSpacer
import com.manta.towitch.common.VSpacer
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.ui.page.home.MainViewModel
import com.manta.towitch.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun ExploreScreen(mainViewModel: MainViewModel) {
    val streams = mainViewModel.recommendedStream.collectAsState()
    val recommendStream = mainViewModel.randomStream.collectAsState(emptyList())
    val pagerState = rememberPagerState(streams.value.size * 5)
    val scope = rememberCoroutineScope()


    if (streams.value.isNotEmpty()) {
        val currentStream = streams.value[pagerState.currentPage % streams.value.size]

        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    Text(
                        "찾기",
                        fontSize = title_tab,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(all = 15.dp)
                    )
                    VSpacer(dp = 50.dp)
                    if (streams.value.isNotEmpty()) {
                        DiscreteScrollView(streams.value.take(8), pagerState, scope)
                    }
                    VSpacer(dp = 20.dp)
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Black)) {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(currentStream.userName)
                                }
                                append(" 방송 중 ")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(currentStream.gameName)
                                }
                            }
                        },
                        modifier = Modifier.padding(horizontal = 15.dp)
                    )
                    VSpacer(dp = 10.dp)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        contentPadding = PaddingValues(horizontal = 15.dp)
                    ) {
                        items(listOf("게임", "리얼라이프", "e스포츠", "음악", "크리에이티브")) {
                            Button(
                                onClick = { /*TODO*/ },
                                modifier = Modifier
                                    .background(
                                        color = Purple500,
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .width(150.dp)
                                    .height(40.dp),
                                contentPadding = PaddingValues(horizontal = 20.dp)
                            ) {
                                HCenter {
                                    Text(
                                        it,
                                        color = White,
                                        fontSize = title2,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.weight(1f))
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_baseline_search_24),
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(White),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                    VSpacer(dp = 20.dp)
                }
                stickyHeader {
                    Header("취향 저격 생방송 채널")
                }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp), contentPadding = PaddingValues(horizontal = 15.dp)) {
                        items(recommendStream.value) { stream ->
                            StreamItem(stream)
                        }
                    }
                }

            }
        }

    }
}

@Composable
private fun StreamItem(stream: Stream) {
    Column(modifier = Modifier.width(250.dp)) {
        Box {
            GlideImage(
                imageModel = stream.getSizedThumbnailUrl(1024, 512),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )
            Text(
                text = "생방송",
                modifier = Modifier
                    .background(Red, shape = RoundedCornerShape(3.dp))
                    .padding(all = 2.dp)
                    .align(Alignment.TopStart),
                fontSize = content3,
                color = White
            )
            Text(
                text = "시청자 ${stream.viewerCount}명",
                modifier = Modifier
                    .background(Black_Transparent, shape = RoundedCornerShape(3.dp))
                    .padding(all = 2.dp)
                    .align(Alignment.BottomStart),
                fontSize = content3,
                color = White
            )
        }

        Row {
            GlideImage(
                imageModel = stream.userProfileImageUrl,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clip(CircleShape)
            )
            HSpacer(dp = 5.dp)
            Column {
                Text(stream.userName, fontSize = content1, fontWeight = FontWeight.Bold)
                Text(
                    stream.title,
                    fontSize = content2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(stream.gameName, fontSize = content2)
            }
        }

    }
}


@Composable
private fun Header(text: String) {
    Text(
        text,
        fontSize = content1,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(White)
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun DiscreteScrollView(
    streams: List<Stream>,
    pagerState: PagerState,
    scope: CoroutineScope
) {
    LaunchedEffect(pagerState) {
        scope.launch {
            pagerState.scrollToPage(3)
        }
    }

    HorizontalPager(
        state = pagerState,
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