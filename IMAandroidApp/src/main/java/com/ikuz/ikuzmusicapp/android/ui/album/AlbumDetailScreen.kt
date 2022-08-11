package com.ikuz.ikuzmusicapp.android.ui.album

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ikuz.ikuzmusicapp.android.R
import com.ikuz.ikuzmusicapp.android.ui.components.SongItem
import com.ikuz.ikuzmusicapp.android.ui.components.playButtons
import com.ikuz.ikuzmusicapp.android.ui.player.bottomSheetContent
import com.ikuz.ikuzmusicapp.android.ui.theme.gray300
import com.ikuz.ikuzmusicapp.android.ui.theme.gray600
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator

data class AlbumDetailNavArgs(val albumId: Long)

@Destination(navArgsDelegate = AlbumDetailNavArgs::class)
@Composable
fun albumDetail(
    resultNavigator: ResultBackNavigator<Boolean>,
) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val albumDetailViewModel: AlbumDetailViewModel = hiltViewModel()
    val albumDetailUiState by albumDetailViewModel.state.collectAsState()
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(Color.Transparent)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        albumContent(albumDetailUiState, statusBarHeight)
        topAppBar(resultNavigator, statusBarHeight)
        bottomSheetContent(
            modifier = Modifier,
            enable = true
        )
    }
}

@Composable
fun topAppBar(
    resultNavigator: ResultBackNavigator<Boolean>,
    statusBarHeight: Dp
) {
    Column(){
        Spacer(modifier = Modifier.height(statusBarHeight))
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Album",
                    style = MaterialTheme.typography.h4,
                    maxLines = 1,
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    resultNavigator.navigateBack(result = true)
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colors.background,
                navigationIconContentColor = Color.White,
                titleContentColor = Color.White,
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun albumContent(
    albumDetailUiState: AlbumDetailUiState,
    statusBarHeight: Dp
) {
        LazyColumn(
            modifier = Modifier
                .padding(top = (64.dp + statusBarHeight), bottom = 75.dp)
                .fillMaxSize()
        ){
            item { albumInfo(albumDetailUiState) }
            item {
                playButtons(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 12.dp)
                )
            }
            items(items = albumDetailUiState.albumSongList){
                SongItem(it)
            }
        }
}

@Composable
fun albumInfo(
    albumDetailUiState: AlbumDetailUiState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val albumArtUrl = "content://media/external/audio/albumart/" + albumDetailUiState.albumId
        val localDensity = LocalDensity.current
        var heightIs by remember { mutableStateOf(0.dp) }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(albumArtUrl)
                .crossfade(true)
                .build(),
            contentDescription = null, // Todo : add content description for
            placeholder = painterResource(R.drawable.album_placeholder),
            fallback = painterResource(R.drawable.album_placeholder),
            error = painterResource(R.drawable.album_placeholder),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(start = 16.dp, end = 8.dp)
                .clip(RoundedCornerShape(15.dp))
                .onGloballyPositioned { coordinates ->
                    heightIs = with(localDensity) { coordinates.size.height.toDp() }
                }
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp, end = 16.dp)
                .fillMaxWidth(1f)
                .height(heightIs)
        ) {
            Text(
                text = albumDetailUiState.albumName,
                style = MaterialTheme.typography.h3,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 12.dp)
            )
            Row(
                modifier = Modifier
                    .padding( top = 4.dp ),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.artist_placeholder),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(30.dp)
//                        .padding(horizontal = 8.dp)
//                        .clip(RoundedCornerShape(100))
//                )
                Text(
                    text = "by " + albumDetailUiState.albumArtist,
                    style = MaterialTheme.typography.h5,
                    color = gray300,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = albumDetailUiState.albumNumSong.toString() + " songs",
                style = MaterialTheme.typography.h5,
                color = gray300,
                modifier = Modifier
                    .padding(bottom = 12.dp)
            )
        }
    }
}