package com.ikuz.ikuzmusicapp.android.ui

import android.os.Bundle
import com.ikuz.ikuzmusicapp.Greeting
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ikuz.ikuzmusicapp.android.ui.NavGraphs
import com.ikuz.ikuzmusicapp.android.ui.theme.IMATheme
import com.mxalbert.sharedelements.SharedElementsRoot
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            IMATheme {
                SharedElementsRoot {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }
}
