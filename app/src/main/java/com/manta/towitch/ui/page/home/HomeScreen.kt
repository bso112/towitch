package com.manta.towitch.ui.page.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.manta.towitch.R
import com.manta.towitch.common.HCenter
import com.manta.towitch.common.HSpacer
import com.manta.towitch.common.VSpacer
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.ui.theme.White
import com.manta.towitch.ui.theme.content1
import com.manta.towitch.ui.theme.title_tab
import com.skydoves.landscapist.glide.GlideImage


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(mainViewModel: MainViewModel) {
    val followedStreams = mainViewModel.followedStream.collectAsState()
    val recommendedStreams = mainViewModel.recommendedStream.collectAsState()
    val offLines = mainViewModel.offlineFollowings.collectAsState()


    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 15.dp)
            .fillMaxSize()
    ) {
        item {
            Text("팔로잉", fontSize = title_tab, fontWeight = FontWeight.Bold)
            VSpacer(dp = 30.dp)
        }
        stickyHeader {
            Header("생방송 채널")
        }
        item {
            VSpacer(dp = 20.dp)
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                followedStreams.value.take(5).forEach { stream ->
                    StreamItem(stream = stream)
                }
            }
            VSpacer(dp = 20.dp)
        }
        stickyHeader {
            Header("추천 채널")
        }
        item {
            VSpacer(dp = 20.dp)
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                recommendedStreams.value.take(5).forEach { stream ->
                    StreamItem(stream = stream)
                }
            }
        }
        stickyHeader {
            Header("오프라인")
        }
        item {
            VSpacer(dp = 20.dp)
            Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                offLines.value.take(6).forEach { following ->
                    Row {
                        GlideImage(
                            imageModel = following.profileImageUrl,
                            contentScale = ContentScale.Crop,
                            placeHolder = painterResource(id = R.drawable.placeholder),
                            modifier = Modifier
                                .width(50.dp)
                                .height(50.dp)
                                .clip(shape = CircleShape)
                        )
                        HSpacer(dp = 10.dp)
                        Text(following.name, fontSize = content1, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

    }
}

@Composable
fun Header(title: String) {
    Text(
        title,
        fontSize = com.manta.towitch.ui.theme.title,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(White)
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    )
}

@Composable
fun StreamItem(stream: Stream) {
    Row {
        Box {
            GlideImage(
                imageModel = stream.getSizedThumbnailUrl(200, 100),
                contentScale = ContentScale.FillWidth,
                placeHolder = painterResource(id = R.drawable.placeholder),
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
            Row {
                GlideImage(
                    imageModel = stream.userProfileImageUrl,
                    contentScale = ContentScale.Crop,
                    placeHolder = painterResource(id = R.drawable.placeholder),
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .clip(shape = CircleShape)
                )
                HSpacer(dp = 5.dp)
                Text(text = stream.userName, fontSize = content1, fontWeight = FontWeight.Bold)
            }

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

