package com.ikuz.ikuzmusicapp.android.ui.local

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ikuz.ikuzmusicapp.android.R
import com.ikuz.ikuzmusicapp.android.ui.artist.ArtistDetailViewModel
import com.ikuz.ikuzmusicapp.android.ui.destinations.artistDetailDestination
import com.ikuz.ikuzmusicapp.android.ui.player.bottomSheetContent
import com.ikuz.ikuzmusicapp.android.ui.components.AlbumItem
import com.ikuz.ikuzmusicapp.android.ui.components.ArtistItem
import com.ikuz.ikuzmusicapp.android.ui.components.SongItem
import com.ikuz.ikuzmusicapp.android.ui.destinations.albumDetailDestination
import com.ikuz.ikuzmusicapp.android.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.launch

// TODO: CLEAN UP!
enum class TabPage(val icon: Int, val tab: String) {
    Song(R.drawable.ic_baseline_music_note_24, "song"),
    Artist(R.drawable.ic_account_music, "artist"),
    Album(R.drawable.ic_baseline_album_24, "album")
}

// TODO: rememberScrollPosition???
// TODO: List live updates, LiveData [DONE]
@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalSong(
    resultNavigator: ResultBackNavigator<Boolean>,
    navigator: DestinationsNavigator
){
    val snackbarHostState = remember { SnackbarHostState() }
    val localViewModel : LocalViewModel =  hiltViewModel()
    val systemUiController = rememberSystemUiController()

        SideEffect {
            systemUiController.setStatusBarColor(blue800)
        }
        Scaffold(
            containerColor = MaterialTheme.colors.background,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ){
            Box(modifier = Modifier.fillMaxSize()) {
                TopBar(
                    resultNavigator = resultNavigator,
                    localViewModel = localViewModel,
                )
                checkPermissionState(snackbarHostState, localViewModel)
                Content(localViewModel, navigator)
                bottomSheetContent(
                    modifier = Modifier,
                    enable = true
                )
            }
        }
}


@Composable
fun TopBar(
    resultNavigator: ResultBackNavigator<Boolean>,
    localViewModel: LocalViewModel
){
        SmallTopAppBar(
            title = {
                SearchBar(localViewModel)
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
            ),
            modifier = Modifier.statusBarsPadding()
        )
}

// TODO: Implement Search functionality. [DONE]-[REVIEW]-[NEED IMPROVE]-[DONE]
@Composable
fun SearchBar(
    localViewModel: LocalViewModel
){
    var query : String by rememberSaveable { mutableStateOf("") }
    var showClearIcon by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    if (query.isEmpty()) {
        showClearIcon = false
    } else if (query.isNotEmpty()) {
        showClearIcon = true
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(end = 6.dp)
            .background(color = halfCyan600, shape = RoundedCornerShape(100))
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = query,
            onValueChange = { onQueryChange ->
                query = onQueryChange
                localViewModel.searchSong(onQueryChange) },
            singleLine = true,
            maxLines = 1,
            textStyle = MaterialTheme.typography.h4,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    localViewModel.searchSong(query)
                }
            ),
            modifier = Modifier
                .weight(1f)
                .height(28.dp)
                .padding(start = 16.dp, top = 3.dp)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .width(48.dp)
        ) {
            IconButton(onClick = {
                if (showClearIcon) {
                    query = ""
                    localViewModel.searchSong(query)
                    focusManager.moveFocus(FocusDirection.Previous)
                } else {
                    focusManager.clearFocus()
                    localViewModel.searchSong(query)
                }
            }) {
                val traillingIcon = if (showClearIcon) {
                    Icons.Filled.Clear
                } else {
                    Icons.Filled.Search
                }
                Icon(
                    imageVector = traillingIcon,
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
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
                    .height(3.dp)
                    .padding(horizontal = 30.dp)
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

// TODO: Add loading screen while fetching data to improve user experience. [REVIEW]-[NEED TO IMPROVE] Reason: Add loading screen for each tab.
// TODO: Add the screen when no songs/artist/album are found. [DONE]
// TODO: loading screen while LazyColumn is loading.
@OptIn(ExperimentalPagerApi::class)
@Composable
fun Content(
    localViewModel: LocalViewModel,
    navigator: DestinationsNavigator
){
    val songViewModelState by localViewModel.state.collectAsState()
    val showNotFoundState by localViewModel.showNotFound.collectAsState()
    val pageState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    println(songViewModelState.songList)
    println(songViewModelState.albumList)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp)
            .statusBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        focusManager.clearFocus()
                    }
                )
            },
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
            modifier = Modifier.padding(bottom = 75.dp),
            state = pageState,
            count = TabPage.values().size,
            itemSpacing = 8.dp,
        ) { index ->
                when (index) {
                    0 ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(top = 10.dp),
                                verticalArrangement = Arrangement.Top,

                            ){
                                items( items = songViewModelState.songList) {
                                    SongItem(it)
                                }
                            }
                            if (showNotFoundState){
                                noFound( icon = TabPage.values()[index].icon, tabCap = TabPage.values()[index].name, tab = TabPage.values()[index].tab)
                            }
                            circularLoodingScreen(localViewModel.loading.value)
                        }

                    1 ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(top = 10.dp),
                                verticalArrangement = Arrangement.Top
                            ){
                                items( items = songViewModelState.artistList) {
                                    ArtistItem(
                                        it,
                                        onClick = {
                                            navigator.navigate(artistDetailDestination(artist = it.artist))
                                        }
                                    )
                                }
                            }
                            if (showNotFoundState){
                                noFound( icon = TabPage.values()[index].icon, tabCap = TabPage.values()[index].name, tab = TabPage.values()[index].tab)
                            }
                            circularLoodingScreen(localViewModel.loading.value)
                        }
                    2 ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(top = 10.dp),
                                verticalArrangement = Arrangement.Top
                            ) {
                                items(items = songViewModelState.albumList) {
                                    AlbumItem(
                                        it,
                                        onClick = {
                                            navigator.navigate(albumDetailDestination(albumId = it.albumId))
                                        }
                                    )
                                }
                            }
                            if (showNotFoundState) {
                                noFound( icon = TabPage.values()[index].icon, tabCap = TabPage.values()[index].name, tab = TabPage.values()[index].tab)
                            }
                            circularLoodingScreen(localViewModel.loading.value)
                        }
                }
        }
    }
}

// TODO: Add function while denied permission. Add like "chocobar" to show permission denied. [DONE]
// TODO: !Issue! When you denied permission, the app will crash. [DONE]
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun permissionRequest(
    permissionState: PermissionState,
    showDialog: Boolean,
    onDisableDialog: () -> Unit,
){
    if (showDialog){
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
                        onClick = {
                            permissionState.launchPermissionRequest()
                            onDisableDialog()
                        },
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
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun checkPermissionState(
    snackbarHostState: SnackbarHostState,
    localViewModel: LocalViewModel,
){
    val showDialogState: Boolean by localViewModel.showDialog.collectAsState()
    val scope = rememberCoroutineScope()
    val storagePermissionState = rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    when (storagePermissionState.status){
        PermissionStatus.Granted -> {
        }
        is PermissionStatus.Denied -> {
            if ((storagePermissionState.status as PermissionStatus.Denied).shouldShowRationale){
                scope.launch {
                snackbarHostState.showSnackbar(
                    "Permission is denied, please enable it in settings",
                    duration = SnackbarDuration.Short,
                )
                }
            }
            else{
                localViewModel.openDialog()
                permissionRequest(
                    permissionState = storagePermissionState,
                    showDialog = showDialogState,
                    onDisableDialog = {localViewModel.disableDialog()}
                )
            }
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
                // TODO: May be possible to apply this to all pages instead of creating multiple of this - [Don't think is possible]
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

@Composable
fun circularLoodingScreen(
    isDisplayed: Boolean
) {
    if (isDisplayed) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp),
                    color = cyan600
                )
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.h6,
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }

        }
    }
}