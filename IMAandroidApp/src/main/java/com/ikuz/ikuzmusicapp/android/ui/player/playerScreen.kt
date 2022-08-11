package com.ikuz.ikuzmusicapp.android.ui.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.ikuz.ikuzmusicapp.android.R
import com.ikuz.ikuzmusicapp.android.ui.extension.noRippleClickable
import com.ikuz.ikuzmusicapp.android.ui.theme.blue800
import com.ikuz.ikuzmusicapp.android.ui.theme.cyan300
import com.ikuz.ikuzmusicapp.android.ui.theme.cyan600
import com.ikuz.ikuzmusicapp.android.ui.theme.gray900
import com.smarttoolfactory.slider.ColorfulSlider
import com.smarttoolfactory.slider.MaterialSliderDefaults
import com.smarttoolfactory.slider.SliderBrushColor
import kotlinx.coroutines.launch


@OptIn(ExperimentalMotionApi::class, ExperimentalMaterialApi::class)
@Composable
fun bottomSheetContent(
    modifier: Modifier,
    enable: Boolean
) {
    val context = LocalContext.current
    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.motion_scene)
            .readBytes()
            .decodeToString()
    }
    val componentHeight by remember { mutableStateOf(2000f) }
    val swipeableState = rememberSwipeableState("Bottom")
        val anchors = mapOf(0f to "Bottom", componentHeight to "Top")

    var songProgress by remember { mutableStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()
    val progress = (swipeableState.offset.value / componentHeight)
    MotionLayout(
        motionScene = MotionScene(content = motionScene),
        progress = progress,
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .background(blue800)
                .layoutId("box")
        )
        LinearProgressIndicator(
            progress = 0.5f,
            color = cyan600,
            trackColor = cyan600.copy(alpha = 0.3f),
            modifier = Modifier
                .layoutId("progress_bar")
        )
        Text(
            text = "Song",
            color = Color.White,
            style = MaterialTheme.typography.h4,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis, // TODO: rolling text for overlapping text
            modifier = Modifier
                .layoutId("collapse_song_title")
        )
        Text(
            text = "Artist",
            color = gray900,
            style = MaterialTheme.typography.h6,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis, // TODO: rolling text for overlapping text
            modifier = Modifier
                .layoutId("collapse_song_artist")
        )

        Image(
            painter = painterResource(id = R.drawable.album_placeholder), // TODO: update with album cover
            contentDescription = null,
            modifier = Modifier
                .clip(RoundedCornerShape(10))
                .aspectRatio(1f)
                .layoutId("player_art")
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .layoutId("bg")
                .noRippleClickable {
                    coroutineScope.launch {
                        when (swipeableState.currentValue) {
                            "Bottom" -> swipeableState.animateTo("Top")
                        }
                    }
                }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    reverseDirection = true,
                    orientation = Orientation.Vertical,
                    resistance = null,
                    thresholds = {_, _ -> FractionalThreshold(0.15f) },
                )

        )
        IconButton(
            onClick = {
                coroutineScope.launch {
                    when (swipeableState.currentValue) {
                        "Top" -> swipeableState.animateTo("Bottom")
                    }
                }
            },
            modifier = Modifier
                .layoutId("expand_back")
                .statusBarsPadding()
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .rotate(-90f)
                    .size(28.dp)
            )
        }
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .layoutId("collapse_prev")
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_skip_back),
                contentDescription = null,
                tint = Color.White,
            )
        }
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .layoutId("collapse_pause")
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_pause),
                contentDescription = null,
                tint = Color.White,
            )
        }
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .layoutId("collapse_next")
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_skip_fwd),
                contentDescription = null,
                tint = Color.White,
            )
        }
        Text(
            text = "Song",
            color = Color.White,
            style = MaterialTheme.typography.h2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis, // TODO: rolling text for overlapping text
            modifier = Modifier
                .layoutId("expand_song_title")
        )
        Text(
            text = "Artist",
            color = Color.White,
            style = MaterialTheme.typography.h4,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis, // TODO: rolling text for overlapping text
            modifier = Modifier
                .layoutId("expand_song_artist")
        )
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .layoutId("expand_more")
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(32.dp)
            )
        }
        Text(
            text = "00:00",
            color = Color.White,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .layoutId("expand_duration")
        )
        ColorfulSlider(
            value = songProgress,
            onValueChange = { it ->
                songProgress = it
            },
            trackHeight = 3.dp,
            thumbRadius = 8.dp,
            colors = MaterialSliderDefaults.defaultColors(
                inactiveTrackColor = SliderBrushColor(Color.White.copy(alpha = 0.4f)),
                activeTrackColor = SliderBrushColor(cyan600),
                thumbColor = SliderBrushColor(cyan300),
                ),
            modifier = Modifier
                .layoutId("expand_progress")
                .height(18.dp)
                .width(245.dp)
        )
        Text(
            text = "04:00",
            color = Color.White,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .layoutId("expand_total")
        )
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .layoutId("expand_prev")
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_skip_back),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
            )
        }
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .layoutId("expand_pause")
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_pause),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
            )
        }
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .layoutId("expand_next")
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_skip_fwd),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
            )
        }
    }
    BackHandler(enabled = (swipeableState.currentValue == "Top")) {
        coroutineScope.launch {
            swipeableState.animateTo("Bottom")
        }
    }
}