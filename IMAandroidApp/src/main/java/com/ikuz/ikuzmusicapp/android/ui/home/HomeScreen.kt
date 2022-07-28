package com.ikuz.ikuzmusicapp.android.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ikuz.ikuzmusicapp.android.ui.theme.teal700
import com.ikuz.ikuzmusicapp.android.R
import com.ikuz.ikuzmusicapp.android.ui.destinations.ComingDestination
import com.ikuz.ikuzmusicapp.android.ui.destinations.LocalSongDestination
import com.ikuz.ikuzmusicapp.android.ui.theme.IMATheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen(
    navigator: DestinationsNavigator
) {
    IMATheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                UserInfo()
                Spacer(modifier = Modifier.height(24.dp))
                SongView(navigator = navigator)
                Spacer(modifier = Modifier.height(24.dp))
                OtherOpt()
            }
        }
    }
}

@Composable
fun UserInfo(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 160.dp)
            .background(color = teal700, shape = RoundedCornerShape(10.dp)),
        verticalArrangement = Arrangement.Center
    ){
        Row(
            modifier = Modifier
                .requiredHeight(IntrinsicSize.Min)
        ){
            Image(
                painter = painterResource(id = R.drawable.ic_cog),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .padding(start = 24.dp),
            )
            Text(
                text = "Name",
                modifier = Modifier
                    .padding(start = 24.dp, top = 24.dp)
                    .requiredHeight(IntrinsicSize.Min),
                color = Color.White,
                style = MaterialTheme.typography.h2
            )
        }
    }
}


@Composable
fun SongView(
    navigator: DestinationsNavigator
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 180.dp)
            .background(color = teal700, shape = RoundedCornerShape(10.dp)),
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { navigator.navigate(LocalSongDestination) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SongPart(
                    id = R.drawable.ic_song,
                    content = "Local Songs",
                    text = "Local Storage",
                )
            }
            Divider(
                color = Color.White,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(0.dp, 28.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { navigator.navigate(ComingDestination) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                SongPart(
                    id = R.drawable.ic_storeage,
                    content = "Cloud Songs",
                    text = "Cloud Storage",
                )
            }
        }
    }
}

@Composable
fun SongPart(id: Int, content: String, text: String){
    Image(
        painter = painterResource(id),
        contentDescription = content,
        modifier = Modifier
            .size(40.dp)
    )
    Spacer(modifier = Modifier.height(28.dp))
    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.h4
    )
}

@Composable
fun OtherOpt(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 190.dp)
            .background(color = teal700, shape = RoundedCornerShape(10.dp)),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            OtherItems(id = R.drawable.ic_note, content = "Update Notes", text = "Update Notes")
        }
        OtherDivider()
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            OtherItems(id = R.drawable.ic_cog, content = "Settings Menu", text = "Settings")
        }
        OtherDivider()
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            OtherItems(id = R.drawable.ic_info, content = "About IMA", text = "About IMA")
        }
    }
}

@Composable
fun OtherItems(id: Int, content: String, text: String){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id),
                contentDescription = content,
                modifier = Modifier
                    .size(30.dp)
            )
            Text(
                text,
                modifier = Modifier
                    .padding(start = 24.dp),
                color = Color.White,
                fontSize = 22.sp,
                style = MaterialTheme.typography.body1
            )
        }
}

@Composable
fun OtherDivider(){
    Divider(
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(start = 62.dp, end = 16.dp)
    )
}