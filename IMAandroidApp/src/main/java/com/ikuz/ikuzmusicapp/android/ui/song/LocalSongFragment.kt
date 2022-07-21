package com.ikuz.ikuzmusicapp.android.ui.song

import android.Manifest
import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.ikuz.ikuzmusicapp.android.R
import com.ikuz.ikuzmusicapp.android.ui.song.components.SongItem
import com.ikuz.ikuzmusicapp.android.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import kotlinx.coroutines.launch

// TODO: CLEAN UP!
enum class TabPage(val icon: Int, val tab: String) {
    Song(R.drawable.ic_baseline_music_note_24, "song"),
    Artist(R.drawable.ic_account_music, "artist"),
    Album(R.drawable.ic_baseline_album_24, "album")
}

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalSong(
    resultNavigator: ResultBackNavigator<Boolean>,
){
    val songViewModel : SongViewModel =  hiltViewModel()
    IMATheme() {
        Scaffold(
            topBar = { TopBar(
                resultNavigator = resultNavigator
            ) },
            containerColor = MaterialTheme.colors.background
        ){
            checkPermissionState()
            Content(songViewModel = songViewModel)
        }
    }
}


@Composable
fun TopBar(
    resultNavigator: ResultBackNavigator<Boolean>
){
    IMATheme() {
        SmallTopAppBar(
            title = {
                SearchBar()
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
                containerColor = MaterialTheme.colors.primaryVariant
            )
        )
    }
}

@Composable
fun SearchBar(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(end = 6.dp)
            .background(color = halvCyan600, shape = RoundedCornerShape(100))
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = "",
            onValueChange = { it },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .height(28.dp)
                .padding(start = 16.dp)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .width(48.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
            )
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
                    .height(3.dp)
                    .padding(horizontal = 30.dp)
                    .background(color = cyan600, shape = RoundedCornerShape(100))
            )
        },
        modifier = Modifier
            .padding(top = 58.dp)
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
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )},
                selectedContentColor = Color.White,
                unselectedContentColor = gray600,
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun Content(
    songViewModel: SongViewModel,
){
    val songViewModelState by songViewModel.state.collectAsState()
    val pageState = rememberPagerState()
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize(),
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
            state = pageState,
            count = TabPage.values().size,
            flingBehavior = PagerDefaults.flingBehavior(state = pageState, endContentPadding = 20.dp),
        ) { index ->
                when (index) {
                    0 ->
                        Surface(
                            color = Color.Transparent
                        ){
//                            noFound( icon = TabPage.values()[index].icon, tabCap = TabPage.values()[index].name, tab = TabPage.values()[index].tab)
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .padding(top = 10.dp),
                                    verticalArrangement = Arrangement.Bottom
                                ){
                                    items( items = songViewModelState.songList) {
                                        SongItem(it)
                                    }
                                }
                            }

                        }
                    1 ->
                        noFound( icon = TabPage.values()[index].icon, tabCap = TabPage.values()[index].name, tab = TabPage.values()[index].tab)
                    2 ->
                        noFound( icon = TabPage.values()[index].icon, tabCap = TabPage.values()[index].name, tab = TabPage.values()[index].tab)
                }
        }
    }
}


@Composable
fun artistView() {

}

@Composable
fun albumView() {

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun permissionRequest(
    permissionState: PermissionState
){
    AlertDialog(
        onDismissRequest = {/* Undismissable */},
        containerColor = darkGreen900,
        icon = {
            Icon(
                imageVector = Icons.Filled.FolderOpen,
                contentDescription = "",
                modifier = Modifier.
                        size(128.dp)
            )
        },
        text = {
               Text(
                   text = "In order to get locally stored songs we need this permission to access",
                   color = Color.White,
                   style = MaterialTheme.typography.h2,
                   fontSize = 20.sp,
                   textAlign = TextAlign.Center
               )
        },
        confirmButton = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(
                    onClick = { permissionState.launchPermissionRequest() },
                    colors = ButtonDefaults.buttonColors(teal700),
                    shape = RoundedCornerShape(15.dp),
                ) {
                    Text(
                        text = "OK, UNDERSTOOD!",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.W300,
                        fontSize = 12.sp
                        // TODO: Change this to variable for localization. Apply to all Text
                    )
                }
            }
        },
        iconContentColor = cyan600,
    )
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun checkPermissionState(){
    val storagePermissionState = rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    when (storagePermissionState.status){
        PermissionStatus.Granted -> {
            Text(text = "Permission is granted")
        }
        is PermissionStatus.Denied -> {
            permissionRequest(permissionState = storagePermissionState)
        }
    }
}

@Composable
fun noFound(
    icon: Int,
    tabCap: String,
    tab: String
){
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Transparent
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier
                    .size(48.dp),
                tint = cyan300
            )
            Text(
                // TODO: May be possible to apply this to all pages instead of creating multiple of this
                text = "No "+ tabCap +"s Found",
                color = cyan600,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h2
            )
            Text(
                text = "There is no "+ tab +"s or you have not grant the permission",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .width(280.dp)
            )
        }

    }
}