package com.ikuz.ikuzmusicapp.android.ui.extension

import android.util.Log
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Original source: https://github.com/Tlaster/NestedScrollView
 */

/**
 * Define a [VerticalNestedScrollView].
 *
 * @param state the state object to be used to observe the [VerticalNestedScrollView] state.
 * @param modifier the modifier to apply to this layout.
 * @param content a block which describes the header.
 * @param content a block which describes the content.
 */
@Composable
fun VerticalNestedScrollView(
    modifier: Modifier = Modifier,
    state: NestedScrollViewState,
    header: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    NestedScrollView(
        modifier = modifier,
        state = state,
        orientation = Orientation.Vertical,
        header = header,
        content = content,
    )
}

/**
 * Define a [HorizontalNestedScrollView].
 *
 * @param state the state object to be used to observe the [HorizontalNestedScrollView] state.
 * @param modifier the modifier to apply to this layout.
 * @param content a block which describes the header.
 * @param content a block which describes the content.
 */
@Composable
fun HorizontalNestedScrollView(
    modifier: Modifier = Modifier,
    state: NestedScrollViewState,
    header: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    NestedScrollView(
        modifier = modifier,
        state = state,
        orientation = Orientation.Horizontal,
        header = header,
        content = content,
    )
}

@Composable
private fun NestedScrollView(
    modifier: Modifier = Modifier,
    state: NestedScrollViewState,
    orientation: Orientation,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier.nestedScroll(state.nestedScrollConnectionHolder),
        content = {
            Box {
                header.invoke()
            }
            Box {
                content.invoke()
            }
        },
    ) { measurables, constraints ->
        layout(constraints.maxWidth, constraints.maxHeight) {
            when (orientation) {
                Orientation.Vertical -> {
                    val headerPlaceable =
                        measurables[0].measure(constraints.copy())
                    headerPlaceable.place(0, state.offset.roundToInt())
                    state.updateBounds(-(headerPlaceable.height.toFloat()))
                    state.headerHeight = headerPlaceable.height
                    val contentPlaceable =
                        measurables[1].measure(constraints.copy(maxHeight = constraints.maxHeight))
                    contentPlaceable.place(
                        0,
                        state.offset.roundToInt() + headerPlaceable.height
                    )
                }
                Orientation.Horizontal -> {
                    val headerPlaceable =
                        measurables[0].measure(constraints.copy(maxWidth = Constraints.Infinity))
                    headerPlaceable.place(state.offset.roundToInt(), 0)
                    state.updateBounds(-(headerPlaceable.width.toFloat()))
                    val contentPlaceable =
                        measurables[1].measure(constraints.copy(maxWidth = constraints.maxWidth))
                    contentPlaceable.place(
                        state.offset.roundToInt() + headerPlaceable.width,
                        0,
                    )
                }
            }
        }
    }
}

/**
 * Original source: https://github.com/Tlaster/NestedScrollView
 */