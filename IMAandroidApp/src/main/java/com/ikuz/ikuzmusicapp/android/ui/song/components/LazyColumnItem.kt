package com.ikuz.ikuzmusicapp.android.ui.song.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ikuz.ikuzmusicapp.android.data.model.SongModel
import com.ikuz.ikuzmusicapp.android.ui.theme.IMATheme
import com.ikuz.ikuzmusicapp.android.ui.theme.gray900

@Composable
fun AlbumItem(
    albumArt: Int,
    album: String,
    songAmount: String,
    artist: String,
    onClick: () -> Unit
){
    IMATheme() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = albumArt),
                contentDescription = "Artist Art",
                modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp),
            ) {
                Column(

                ){
                    Text(
                        text = album,
                        color = Color.White,
                        style = MaterialTheme.typography.h4,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row() {
                    Text(
                        text = songAmount,
                        color = gray900,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.W300
                    )
                    Text(
                        text = " Songs",
                        color = gray900,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.W300
                    )
                    Text(
                        text = " - ",
                        color = gray900,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.W300
                    )
                    Text(
                        text = artist,
                        color = gray900,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.W300
                    )
                }
            }
        }
    }
}

@Composable
fun ArtistItem(
    artistImage: Int,
    artist: String,
    songAmount: String,
    onClick: () -> Unit
){
    IMATheme() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = artistImage),
                contentDescription = "Artist Art",
                modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(100))
            )
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp),
            ) {
                Column(

                ){
                    Text(
                        text = artist,
                        color = Color.White,
                        style = MaterialTheme.typography.h4,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row() {
                    Text(
                        text = songAmount,
                        color = gray900,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.W300
                    )
                    Text(
                        text = " Songs",
                        color = gray900,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.W300
                    )
                }
            }
        }
    }
}

@Composable
fun SongItem(
    song: SongModel?,
//    onClick: () -> Unit
){
    if (song != null) {
        IMATheme() {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Column(){
                    Text(
                        text = song.title,
                        color = Color.White,
                        style = MaterialTheme.typography.h4,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row() {
                    Text(
                        text = song.artist,
                        color = gray900,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.W300
                    )
                    Text(
                        text = " - ",
                        color = gray900,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.W300
                    )
                    Text(
                        text = song.album,
                        color = gray900,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.W300
                    )
                }
            }
        }
    }
}