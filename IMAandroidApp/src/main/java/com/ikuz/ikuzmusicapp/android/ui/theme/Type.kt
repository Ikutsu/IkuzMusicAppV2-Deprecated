package com.ikuz.ikuzmusicapp.android.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ikuz.ikuzmusicapp.android.R

val ubuntu = FontFamily(
    Font(R.font.ubuntu_light, FontWeight.W300),
    Font(R.font.ubuntu_regular, FontWeight.W400),
    Font(R.font.ubuntu_medium, FontWeight.W500),
    Font(R.font.ubuntu_bold, FontWeight.W700),
    Font(R.font.ubuntu_lightitalic, FontWeight.W300),
    Font(R.font.ubuntu_italic, FontWeight.W400),
    Font(R.font.ubuntu_mediumitalic, FontWeight.W500),
    Font(R.font.ubuntu_bolditalic, FontWeight.W700)
)

val Typography = Typography(
    defaultFontFamily = ubuntu,
    h1 = TextStyle(
        fontFamily = ubuntu,
        fontWeight = FontWeight.W400,
        fontSize = 30.sp,
    ),
    h2 = TextStyle(
        fontFamily = ubuntu,
        fontWeight = FontWeight.W400,
        fontSize = 24.sp,
    ),
    h3 = TextStyle(
        fontFamily = ubuntu,
        fontWeight = FontWeight.W400,
        fontSize = 20.sp,
    ),
    h4 = TextStyle(
        fontFamily = ubuntu,
        fontWeight = FontWeight.W400,
        fontSize = 18.sp,
    ),
    h5 = TextStyle(
        fontFamily = ubuntu,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
    ),
    h6 = TextStyle(
        fontFamily = ubuntu,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
    ),
    subtitle1 = TextStyle(
        fontFamily = ubuntu,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
    ),
    subtitle2 = TextStyle(
        fontFamily = ubuntu,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
    ),
    body1 = TextStyle(
        fontFamily = ubuntu,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = ubuntu,
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = ubuntu,
        fontWeight = FontWeight.W400,
        fontSize = 15.sp,
    ),
    caption = TextStyle(
        fontFamily = ubuntu,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        fontFamily = ubuntu,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp
    )
)