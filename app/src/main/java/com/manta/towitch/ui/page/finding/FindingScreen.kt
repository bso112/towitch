package com.manta.towitch.ui.page.finding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.*
import com.google.android.material.math.MathUtils.lerp
import com.manta.towitch.R
import com.manta.towitch.common.HCenter
import com.manta.towitch.common.HSpacer
import com.manta.towitch.common.VSpacer
import com.manta.towitch.data.entity.Clip
import com.manta.towitch.data.entity.Game
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.ui.theme.*
import com.manta.towitch.utils.minuteToTimeString
import com.manta.towitch.utils.toDateString
import com.manta.towitch.utils.toSafe
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun FindingScreen(vm: FindingViewModel = hiltViewModel()) {
    val streams = vm.streams.collectAsState()
    val recommendStream = vm.recommendedStreams.collectAsState(emptyList())
    val smallStreams = vm.smallStreams.collectAsState(emptyList())
    val games = vm.games.collectAsState()
    val justChattingStreams = vm.justcChattingStreams.collectAsState(emptyList())
    val clips = vm.clips.collectAsState()

    val pagerState = rememberPagerState(streams.value.size * 5)
    val scope = rememberCoroutineScope()


    if (streams.value.isNotEmpty()) {
        val currentStream = streams.value[pagerState.currentPage % streams.value.size]

        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                item {
                    Text(
                        "찾기", fontSize = title_tab,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(all = 15.dp)
                    )
                    VSpacer(dp = 30.dp)
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
                    StreamRow {
                        items(recommendStream.value) { stream ->
                            StreamItem(stream)
                        }
                    }
                    VSpacer(dp = 20.dp)
                }
                stickyHeader {
                    Header(
                        buildAnnotatedString {
                            append("취향 저격 ")
                            withStyle(SpanStyle(color = Purple500)) {
                                append("카테고리")
                            }
                        }
                    )
                }
                item {
                    StreamRow {
                        items(games.value) {
                            GameItem(it)
                        }
                    }
                    VSpacer(dp = 20.dp)
                }
                stickyHeader {
                    Header(text = "추천 소규모 채널")
                }
                item {
                    StreamRow {
                        items(smallStreams.value) {
                            StreamItem(stream = it)
                        }
                    }
                    VSpacer(dp = 20.dp)
                }
                stickyHeader {
                    Header(
                        buildAnnotatedString {
                            append("추천")
                            withStyle(SpanStyle(color = Purple500)) {
                                append(" JUST CHATTING ")
                            }
                            append("채널")
                        }
                    )
                }
                item {
                    StreamRow {
                        items(justChattingStreams.value) {
                            StreamItem(stream = it)
                        }
                    }
                }
                stickyHeader {
                    Header("취향 저격 클립")
                }
                item {
                    StreamRow {
                        items(clips.value.take(12)) {
                            ClipItem(clip = it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StreamRow(content: LazyListScope.() -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        content = content
    )
}

@Composable
private fun GameItem(game: Game) {
    Column(
        Modifier
            .width(100.dp)
    ) {
        GlideImage(
            imageModel = game.getSizedThumbnailUrl(256, 512),
            placeHolder = painterResource(id = R.drawable.placeholder),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(150.dp)
        )
        VSpacer(dp = 5.dp)
        Text(game.name, fontSize = content1, maxLines = 1, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun ClipItem(clip: Clip) {
    Column(modifier = Modifier.width(250.dp)) {
        Box {
            GlideImage(
                imageModel = clip.thumbnailUrl,
                contentScale = ContentScale.FillWidth,
                placeHolder = painterResource(id = R.drawable.placeholder),
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = clip.duration.toInt().minuteToTimeString(),
                    modifier = Modifier
                        .background(Black_Transparent, shape = RoundedCornerShape(3.dp))
                        .padding(horizontal = 3.dp, vertical = 2.dp),
                    fontSize = content3,
                    color = White
                )
            }
            Box(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = "조회수 ${clip.viewCount}회",
                    modifier = Modifier
                        .background(Black_Transparent, shape = RoundedCornerShape(3.dp))
                        .padding(horizontal = 3.dp, vertical = 2.dp),
                    fontSize = content3,
                    color = White
                )
            }
            Box(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Text(
                    clip.createdAt.toDateString(), modifier = Modifier
                        .background(Black_Transparent, shape = RoundedCornerShape(3.dp))
                        .padding(horizontal = 3.dp, vertical = 2.dp),
                    fontSize = content3,
                    color = White
                )
            }
        }
        VSpacer(dp = 10.dp)
        Row(Modifier.padding(horizontal = 5.dp)) {
            GlideImage(
                imageModel = clip.broadCasterProfileImageUrl,
                placeHolder = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .clip(CircleShape)
            )
            HSpacer(dp = 5.dp)
            Column {
                Text(clip.broadCasterName, fontSize = content1, fontWeight = FontWeight.Bold)
                Text(
                    clip.title,
                    fontSize = content2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(clip.gameName.toSafe(), fontSize = content2)
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
                placeHolder = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .padding(all = 5.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = "생방송",
                    modifier = Modifier
                        .background(Red, shape = RoundedCornerShape(3.dp))
                        .padding(horizontal = 3.dp, vertical = 2.dp),
                    fontSize = content3,
                    color = White
                )
            }
            Box(
                modifier = Modifier
                    .padding(all = 5.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = "시청자 ${stream.viewerCount}명",
                    modifier = Modifier
                        .background(Black_Transparent, shape = RoundedCornerShape(3.dp))
                        .padding(horizontal = 3.dp, vertical = 2.dp),
                    fontSize = content3,
                    color = White
                )
            }
        }
        VSpacer(dp = 10.dp)
        Row(Modifier.padding(horizontal = 5.dp)) {
            GlideImage(
                imageModel = stream.userProfileImageUrl,
                contentScale = ContentScale.Inside,
                placeHolder = painterResource(id = R.drawable.placeholder),
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
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
private fun Header(string: AnnotatedString) {
    Text(
        string,
        fontSize = content1,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(White)
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 10.dp),
    )
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
            .padding(horizontal = 15.dp, vertical = 10.dp),
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
                contentScale = ContentScale.FillWidth,
            ) {

            }
        }
    }


}