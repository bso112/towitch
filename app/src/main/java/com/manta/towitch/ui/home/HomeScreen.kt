package com.manta.towitch.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.manta.towitch.common.HCenter
import com.manta.towitch.common.HSpacer
import com.manta.towitch.common.VSpacer
import com.manta.towitch.data.entity.Stream
import com.manta.towitch.ui.theme.*
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun HomeScreen(mainViewModel: HomeViewModel = hiltViewModel()) {
    val followedStreams = mainViewModel.followedStream.collectAsState()
    val recommendedStreams = mainViewModel.recommendedStream.collectAsState()
    val user = mainViewModel.user.collectAsState()
    val offLines = mainViewModel.offlineFollowings.collectAsState()

    Column(
        modifier = Modifier
            .background(color = white)
            .fillMaxSize()
            .padding(horizontal = 15.dp)
            .verticalScroll(rememberScrollState())
    ) {
        GlideImage(
            imageModel = user.value.profileImageUrl,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .padding(vertical = 10.dp)
                .clip(shape = CircleShape)
        )
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
        Text("오프라인", fontSize = title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 20.dp))
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
                    color = white
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


