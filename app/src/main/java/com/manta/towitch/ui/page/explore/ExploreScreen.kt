package com.manta.towitch.ui.page.explore

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.manta.towitch.R
import com.manta.towitch.common.HSpacer
import com.manta.towitch.common.VSpacer
import com.manta.towitch.data.entity.Game
import com.manta.towitch.ui.page.finding.findingScreen
import com.manta.towitch.ui.page.home.HomeScreen
import com.manta.towitch.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

/**
 * TabRow(
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
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun ExploreScreen(vm: ExploreViewModel = hiltViewModel()) {
    val pagerState = rememberPagerState(pageCount = 2)
    val scope = rememberCoroutineScope()
    val games = vm.games.collectAsState()
    Box {
        Column {
            LazyColumn(modifier = Modifier.padding(all = 15.dp)) {
                item {
                    Text(
                        "탐색",
                        fontSize = title_tab,
                        fontWeight = FontWeight.Bold,
                    )
                    VSpacer(dp = 30.dp)
                }
                stickyHeader {
                    TabRow(
                        selectedTabIndex = 0,
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth(),
                        backgroundColor = White, contentColor = Purple500,
                    ) {
                        listOf("카테고리", "생방송 채널").forEachIndexed { index, title ->
                            Tab(selected = index == pagerState.currentPage, onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }, selectedContentColor = Purple500, unselectedContentColor = Black) {
                                Text(title, modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = content1)
                            }
                        }

                    }
                }
                item {
                    VSpacer(dp = 15.dp)
                }
            }
            HorizontalPager(
                state = pagerState,
                dragEnabled = false,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp)
            ) { pageIndex ->
                when (pageIndex) {
                    0 -> GamePage(games.value)
                    1 -> GamePage(games.value)
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
private fun GamePage(games: List<Game>) {
    LazyColumn(Modifier.fillMaxWidth()) {
        items(games) {
            GameItem(game = it)
        }
    }
}


@Composable
private fun GameItem(game: Game) {
    Row {
        GlideImage(
            imageModel = game.getSizedThumbnailUrl(128, 256),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .width(80.dp)
                .height(100.dp)
        )
        HSpacer(15.dp)
        Text(game.name, fontWeight = FontWeight.Bold, fontSize = title2)
    }
}