package com.manta.towitch.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.manta.towitch.R
import com.manta.towitch.common.*
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch


enum class BottomTab(
    @DrawableRes val disabledIcon: Int,
    @DrawableRes val enabledIcon: Int,
    @StringRes val title: Int
) {
    Following(
        R.drawable.ic_baseline_favorite_border_24,
        R.drawable.ic_baseline_favorite_24,
        R.string.bottom_tab_following
    ),
    Finding(
        R.drawable.ic_outline_explore_24,
        R.drawable.ic_baseline_explore_fill_24,
        R.string.bottom_tab_finding
    ),
    Explore(
        R.drawable.ic_baseline_content_copy_24,
        R.drawable.ic_baseline_content_copy_24,
        R.string.bottom_tab_explore
    ),
    Search(
        R.drawable.ic_baseline_search_24,
        R.drawable.ic_baseline_search_24,
        R.string.bottom_tab_search
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HostScreen(mainViewModel: HomeViewModel = hiltViewModel()) {
    val user = mainViewModel.user.collectAsState()
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = 4)

    Scaffold(
        topBar = {
            GlideImage(
                imageModel = user.value.profileImageUrl,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clip(CircleShape)
                    .padding(vertical = 10.dp)
            )
        },
        bottomBar = {
            Column {
                Line(Gray_tag)
                TabRow(
                    selectedTabIndex = 0,
                    backgroundColor = White,
                    indicator = {},
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                ) {
                    BottomTab.values().forEachIndexed { index, tab ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            selectedContentColor = Purple500, unselectedContentColor = Black,
                        ) {
                            BottomTabItem(tab = tab, isEnabled = pagerState.currentPage == index)
                        }
                    }
                }
            }

        },
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            dragEnabled = false,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) { pageIndex ->
            when (pageIndex) {
                0 -> HomeScreen(mainViewModel)
                1 -> HomeScreen(mainViewModel)
                2 -> HomeScreen(mainViewModel)
                3 -> HomeScreen(mainViewModel)
            }
        }
    }

}

@Composable
fun BottomTabItem(tab: BottomTab, isEnabled: Boolean) {
    val painter =
        if (isEnabled) painterResource(id = tab.enabledIcon) else painterResource(id = tab.disabledIcon)
    val color = if (isEnabled) Purple500 else Black
    VCenter {
        Image(
            painter = painter,
            colorFilter = ColorFilter.tint(color),
            contentDescription = "",
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
        )
        Text(stringResource(id = tab.title), fontSize = content2)
    }
}

@Composable
fun HomeScreen(mainViewModel: HomeViewModel) {
    val followedStreams = mainViewModel.followedStream.collectAsState()
    val recommendedStreams = mainViewModel.recommendedStream.collectAsState()
    val offLines = mainViewModel.offlineFollowings.collectAsState()

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
    ) {
        Text("팔로잉", fontSize = title_tab, fontWeight = FontWeight.Bold)
        VSpacer(dp = 50.dp)
        Text("생방송 채널", fontSize = title, fontWeight = FontWeight.Bold)
        VSpacer(dp = 20.dp)
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            followedStreams.value.take(5).forEach { stream ->
                StreamItem(stream = stream)
            }
        }
        VSpacer(dp = 20.dp)
        Text("추천 채널", fontSize = title, fontWeight = FontWeight.Bold)
        VSpacer(dp = 20.dp)
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            recommendedStreams.value.take(5).forEach { stream ->
                StreamItem(stream = stream)
            }
        }
        Text(
            "오프라인",
            fontSize = title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 20.dp)
        )
        Column {
            offLines.value.take(10).forEach { following ->
                Row {
                    GlideImage(
                        imageModel = following.profileImageUrl,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clip(shape = CircleShape)
                    )
                    HSpacer(dp = 10.dp)
                    Column {
                        Text(following.name, fontSize = content1, fontWeight = FontWeight.Bold)
                    }
                }
                VSpacer(dp = 20.dp)
            }
        }
    }
}


@Composable
fun StreamItem(stream: Stream) {
    Row {
        Box {
            GlideImage(
                imageModel = stream.getSizedThumbnailUrl(200, 100),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            )
            HCenter(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 3.dp, vertical = 3.dp)
            ) {
                Canvas(modifier = Modifier.size(10.dp), onDraw = {
                    drawCircle(color = Color.Red)
                })
                HSpacer(dp = 3.dp)
                Text(
                    stream.viewerCount.toString(),
                    fontSize = content1,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }

        }
        HSpacer(dp = 15.dp)
        Column {
            Text(text = stream.userName, fontSize = content1, fontWeight = FontWeight.Bold)
            VSpacer(dp = 5.dp)
            Text(
                text = stream.title,
                fontSize = content1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            VSpacer(dp = 5.dp)
            Text(
                text = stream.gameName,
                fontSize = content1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            VSpacer(dp = 5.dp)
//            stream.tagIds.forEach { tag ->
//                Text(text = tag.slice(0..5), modifier = Modifier.background(gray_tag, shape = RoundedCornerShape(8.dp)))
//            }
        }
    }
}


