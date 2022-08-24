package com.manta.towitch.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VSpacer(dp: Dp) = Spacer(modifier = Modifier.height(dp))

@Composable
fun HSpacer(dp: Dp) = Spacer(modifier = Modifier.width(dp))


@Composable
fun HCenter(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) = Row(
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier,
    content = content
)

@Composable
fun VCenter(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) = Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier,
    content = content
)

@Composable
fun Line(color : Color){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(color))
}