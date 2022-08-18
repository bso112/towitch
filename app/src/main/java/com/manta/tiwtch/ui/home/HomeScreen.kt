package com.manta.tiwtch.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.manta.tiwtch.common.HCenter
import com.manta.tiwtch.common.HSpacer
import com.manta.tiwtch.common.VCenter
import com.manta.tiwtch.common.VSpacer
import com.manta.tiwtch.data.entity.StreamData
import com.manta.tiwtch.ui.theme.gray_tag
import com.manta.tiwtch.ui.theme.white
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun HomeScreen(mainViewModel: HomeViewModel = hiltViewModel()) {
    val streamList = mainViewModel.streams.collectAsState()

    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(horizontal = 15.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            items(streamList.value) { stream ->
                StreamItem(stream = stream)
            }

        }
    }
}

@Composable
fun StreamItem(stream: StreamData) {
    Row {
        Box {
            GlideImage(
                imageModel = stream.getSizedThumbnailUrl(200, 100),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .height(60.dp)
            )
            HCenter(modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 3.dp, vertical = 3.dp)) {
                Canvas(modifier = Modifier.size(10.dp), onDraw = {
                    drawCircle(color = Color.Red)
                })
                HSpacer(dp = 3.dp)
                Text(
                    stream.viewerCount.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = white
                )
            }

        }
        HSpacer(dp = 15.dp)
        Column {
            Text(text = stream.userName, fontWeight = FontWeight.Bold)
            VSpacer(dp = 5.dp)
            Text(text = stream.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
            VSpacer(dp = 5.dp)
            Text(text = stream.gameName, maxLines = 1, overflow = TextOverflow.Ellipsis)
            VSpacer(dp = 5.dp)
//            stream.tagIds.forEach { tag ->
//                Text(text = tag.slice(0..5), modifier = Modifier.background(gray_tag, shape = RoundedCornerShape(8.dp)))
//            }
        }
    }
}

