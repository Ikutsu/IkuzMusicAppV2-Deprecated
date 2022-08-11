package com.ikuz.ikuzmusicapp.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ikuz.ikuzmusicapp.android.R
import com.ikuz.ikuzmusicapp.android.ui.theme.cyan600

@Composable
fun playButtons(
    modifier : Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        playAllButton()
        shuffleButton()
    }
}

@Composable
fun playAllButton() {
    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = cyan600,
            contentColor = Color.White
        ),
        modifier = Modifier
            .width(152.dp)
            .height(48.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_public_play),
            contentDescription = null,
            modifier = Modifier
                .size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Play all",
            style = MaterialTheme.typography.h4,
        )
    }
}

@Composable
fun shuffleButton() {
    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = cyan600
        ),
        modifier = Modifier
            .width(152.dp)
            .height(48.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_shuffle),
            contentDescription = null,
            modifier = Modifier
                .size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = "Shuffle",
            style = MaterialTheme.typography.h4,
        )
    }
}