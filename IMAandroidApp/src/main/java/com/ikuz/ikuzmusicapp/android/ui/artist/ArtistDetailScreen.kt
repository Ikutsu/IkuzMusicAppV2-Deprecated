package com.ikuz.ikuzmusicapp.android.ui.artist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ikuz.ikuzmusicapp.android.R
import com.ikuz.ikuzmusicapp.android.data.model.AlbumListModel
import com.ikuz.ikuzmusicapp.android.ui.extension.VerticalNestedScrollView
import com.ikuz.ikuzmusicapp.android.ui.extension.noRippleClickable
import com.ikuz.ikuzmusicapp.android.ui.extension.rememberNestedScrollViewState
import com.ikuz.ikuzmusicapp.android.ui.local.circularLoodingScreen
import com.ikuz.ikuzmusicapp.android.ui.components.SongItem
import com.ikuz.ikuzmusicapp.android.ui.components.playButtons
import com.ikuz.ikuzmusicapp.android.ui.destinations.albumDetailDestination
import com.ikuz.ikuzmusicapp.android.ui.player.bottomSheetContent
import com.ikuz.ikuzmusicapp.android.ui.theme.cyan600
import com.ikuz.ikuzmusicapp.android.ui.theme.gray600
import com.ikuz.ikuzmusicapp.android.ui.theme.gray900
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.launch
import java.lang.Float.min

enum class TabPage() {
    Song,
    Album
}

data class ArtistDetailNavArgs(val artist: String?)

@Destination(navArgsDelegate = ArtistDetailNavArgs::class)
@Composable
fun artistDetail(
    resultNavigator: ResultBackNavigator<Boolean>,
    navigator: DestinationsNavigator
) {
    val viewmodel: ArtistDetailViewModel = hiltViewModel()
    val artistDetailUiState by viewmodel.state.collectAsState()
    val nestedScrollViewState = rememberNestedScrollViewState()
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(Color.Transparent)
    }
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.artist_placeholder),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .graphicsLayer {
                    alpha = min(
                        1f,
                        1 - (nestedScrollViewState.offset / nestedScrollViewState.maxOffset)
                    )
                }
        )
        Column() {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Spacer(Modifier
                    .height(statusBarHeight)
                )
                SmallTopAppBar(
                    title = {
                        Text(
                            text = artistDetailUiState.artistName,
                            style = MaterialTheme.typography.h5,
                            color = Color.White,
                            maxLines = 1,
                            modifier = Modifier
                                .alpha(((nestedScrollViewState.offset - nestedScrollViewState.maxOffset * 0.7f) / (nestedScrollViewState.maxOffset - nestedScrollViewState.maxOffset * 0.7f)).coerceIn(0f, 1f))
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            resultNavigator.navigateBack(result = true)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                modifier = Modifier
                                    .size(30.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.Transparent,
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.White,
                        scrolledContainerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

            }
            VerticalNestedScrollView(
                state = nestedScrollViewState,
                header = {
                    Column{
                        Spacer(modifier = Modifier
                            .height(172.dp)
                            .fillMaxWidth())
                        Text(
                            text = artistDetailUiState.artistName,
                            style = MaterialTheme.typography.h2,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(start = 16.dp, bottom = 20.dp)
                                .alpha(
                                    (1 - ((nestedScrollViewState.offset) / (nestedScrollViewState.maxOffset + 200f))).coerceIn(
                                        0f,
                                        1f
                                    )
                                )
                        )
                    }
                },
                content = {
                    content(viewmodel, artistDetailUiState, navigator)
                }
            )
        }
        bottomSheetContent(
            modifier = Modifier,
            enable = true
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun content(
    viewmodel: ArtistDetailViewModel,
    artistDetailUiState : ArtistDetailUiState,
    navigator: DestinationsNavigator
) {
    val pageState = rememberPagerState()
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colors.background,
                shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp)
            )
            .verticalScroll(
                rememberScrollState()
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabBar(
            selectedIndex = pageState.currentPage,
            onSelectedTab = {
                scope.launch {
                    pageState.animateScrollToPage(it.ordinal)
                }
            },
            pagerState = pageState
        )
        HorizontalPager(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 75.dp),
            state = pageState,
            count = TabPage.values().size,
        ) { index ->
            when (index) {
                0 ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        playButtons(Modifier.fillMaxWidth().padding(top = 18.dp, bottom = 8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Top,
                            ){
                                items( items = artistDetailUiState.artistSongList) {
                                    SongItem(it)
                                }
                            }
                            circularLoodingScreen(viewmodel.loading.value)
                        }

                    }
                1 ->
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(top = 10.dp, start = 8.dp, end = 8.dp),
                        verticalArrangement = Arrangement.Top,
                        content = {
                            items(count = viewmodel.state.value.artistAlbumList.size) { index ->
                                albumCards(
                                    artistDetailUiState.artistAlbumList[index],
                                    onClick = {
                                        navigator.navigate(albumDetailDestination(albumId = artistDetailUiState.artistAlbumList[index].albumId))
                                    }
                                )
                            }
                        }
                    )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabBar (selectedIndex: Int, onSelectedTab: (TabPage) -> Unit, pagerState: PagerState){
    androidx.compose.material.TabRow(
        backgroundColor = Color.Transparent,
        contentColor = Color.Transparent,
        selectedTabIndex = selectedIndex,
        divider = {},
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .pagerTabIndicatorOffset(pagerState, tabPositions)
                    .height(4.dp)
                    .padding(horizontal = 52.dp)
                    .background(color = cyan600, shape = RoundedCornerShape(100))
            )
        },
        modifier = Modifier
            .width(280.dp)
    ){

        TabPage.values().forEachIndexed {
                index, tabPage ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onSelectedTab(tabPage)},
                text = {
                    Text(
                        text = tabPage.name,
                        style = MaterialTheme.typography.h2,
                        fontSize = 22.sp,
                        modifier = Modifier
                            .padding(top = 6.dp, bottom = 4.dp)
                    )},
                selectedContentColor = Color.White,
                unselectedContentColor = gray600,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun albumCards(
    albumInfo: AlbumListModel,
    onClick: () -> Unit
) {
    val albumArtUrl = "content://media/external/audio/albumart/" + albumInfo.albumId
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .noRippleClickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
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
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp))
            )
            Text(
                text = albumInfo.album,
                style = MaterialTheme.typography.h6,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            Text(
                text = albumInfo.numsongs.toString() + " songs",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.W300,
                color = gray900,
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 8.dp)
            )
        }
    }
}
