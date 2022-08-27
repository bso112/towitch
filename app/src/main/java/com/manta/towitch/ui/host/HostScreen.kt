

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.manta.towitch.R
import com.manta.towitch.common.Line
import com.manta.towitch.common.VCenter
import com.manta.towitch.ui.page.explore.ExploreScreen
import com.manta.towitch.ui.page.finding.findingScreen
import com.manta.towitch.ui.page.home.HomeScreen
import com.manta.towitch.ui.page.home.MainViewModel
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
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HostScreen(mainViewModel: MainViewModel = hiltViewModel()) {
    val user = mainViewModel.user.collectAsState()
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = 3)

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
                .padding(innerPadding)
                .fillMaxSize()
        ) { pageIndex ->
            when (pageIndex) {
                0 -> HomeScreen(mainViewModel)
                1 -> findingScreen()
                2 -> ExploreScreen()
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
