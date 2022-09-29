package com.manta.towitch.ui.page.explore

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.manta.towitch.R
import com.manta.towitch.common.HSpacer
import com.manta.towitch.common.VSpacer
import com.manta.towitch.data.entity.Game
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun ExploreScreen(vm: ExploreViewModel = hiltViewModel()) {
    val pagerState = rememberPagerState(pageCount = 2)
    val scope = rememberCoroutineScope()
    val games = vm.games.collectAsState()
    val streams = vm.streams.collectAsState()

    Box {
        LazyColumn {
            item {
                Text(
                    "탐색",
                    fontSize = title_tab,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                )
                VSpacer(dp = 30.dp)
            }
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = White)
                ) {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier
                            .height(40.dp)
                            .width(180.dp)
                            .padding(horizontal = 15.dp),
                        backgroundColor = White, contentColor = twitch_purple,
                    ) {
                        listOf("카테고리", "생방송 채널").forEachIndexed { index, title ->
                            Tab(selected = index == pagerState.currentPage, onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }, selectedContentColor = twitch_purple, unselectedContentColor = Black) {
                                Text(
                                    title,
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    fontSize = content1,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                    }
                }
            }
            item {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    state = pagerState,
                    verticalAlignment = Alignment.Top
                ) { pageIndex ->
                    when (pageIndex) {
                        0 -> GamePage(games.value)
                        1 -> StreamPage(streams.value.take(10))
                    }
                }
            }
        }
        ExtendedFloatingActionButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(all = 20.dp),
            shape = RectangleShape,
            backgroundColor = White,
            elevation = FloatingActionButtonDefaults.elevation(8.dp),
            text = { Text("필터 및 정렬") },
            icon = { Icon(Icons.Filled.List, "") }
        )
    }
}

@Composable
private fun StreamPage(streams: List<Stream>) {
    Column(
        Modifier
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        streams.forEach {
            StreamItem(stream = it)
        }
    }
}

@Composable
private fun StreamItem(stream: Stream) {
    Column {
        Box {
            GlideImage(
                imageModel = stream.getSizedThumbnailUrl(1024, 512),
                contentScale = ContentScale.FillBounds,
                placeHolder = painterResource(id = R.drawable.placeholder),
                modifier = Modifier
                    .height(220.dp)
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
private fun GamePage(games: List<Game>) {
    Column(
        Modifier
            .padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        VSpacer(dp = 15.dp)
        games.forEach {
            GameItem(game = it)
        }
    }
}


@Composable
private fun GameItem(game: Game) {
    Row(
        Modifier
            .fillMaxWidth()
    ) {
        GlideImage(
            imageModel = game.getSizedThumbnailUrl(128, 256),
            contentScale = ContentScale.FillBounds,
            placeHolder = painterResource(id = R.drawable.placeholder),
            modifier = Modifier
                .width(80.dp)
                .height(100.dp)
        )
        HSpacer(15.dp)
        Text(game.name, fontWeight = FontWeight.Bold, fontSize = title2)
    }
}