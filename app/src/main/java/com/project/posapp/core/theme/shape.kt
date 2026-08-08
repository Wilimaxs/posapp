package com.project.posapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object Radius {
    val Small = 4.dp
    val Default = 8.dp
    val Medium = 12.dp
    val Large = 16.dp
    val ExtraLarge = 24.dp
}

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(Radius.Small),
    small = RoundedCornerShape(Radius.Default),
    medium = RoundedCornerShape(Radius.Medium),
    large = RoundedCornerShape(Radius.Large),
    extraLarge = RoundedCornerShape(Radius.ExtraLarge)
)