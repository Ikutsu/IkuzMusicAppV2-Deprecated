package com.ikuz.ikuzmusicapp.android.ui.song.components

import android.content.ContentUris
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.DefaultRequestOptions
import coil.request.ImageRequest
import com.ikuz.ikuzmusicapp.android.R
import com.ikuz.ikuzmusicapp.android.data.model.AlbumModel
import com.ikuz.ikuzmusicapp.android.data.model.ArtistModel
import com.ikuz.ikuzmusicapp.android.data.model.SongModel
import com.ikuz.ikuzmusicapp.android.ui.theme.IMATheme
import com.ikuz.ikuzmusicapp.android.ui.theme.gray900

@Composable
fun AlbumItem(
//    albumArt: Int,
    album: AlbumModel?,
//    onClick: () -> Unit
){
    if (album != null) {
        IMATheme() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                val albumArtUrl = "content://media/external/audio/albumart/" + album.albumId
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
                        .size(55.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, top = 8.dp),
                ) {
                    Column(

                    ) {
                        Text(
                            text = album.album,
                            color = Color.White,
                            style = MaterialTheme.typography.h4,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row() {
                        Text(
                            text = album.numsongs.toString(),
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
                            text = album.album,
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
}

@Composable
fun ArtistItem(
    artist: ArtistModel?,
//    onClick: () -> Unit
){
    if (artist != null) {
        IMATheme() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(R.drawable.artist_placeholder)
                        .build(), // Placeholder, replace with artistArt: fetch from api
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

                    ) {
                        Text(
                            text = artist.artist,
                            color = Color.White,
                            style = MaterialTheme.typography.h4,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row() {
                        Text(
                            text = artist.num_of_songs.toString(),
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