package com.ikuz.ikuzmusicapp.android.ui

import android.os.Bundle
import com.ikuz.ikuzmusicapp.Greeting
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ikuz.ikuzmusicapp.android.ui.NavGraphs
import com.ikuz.ikuzmusicapp.android.ui.theme.IMATheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

fun greet(): String {
    return Greeting().greeting()
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IMATheme() {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}
