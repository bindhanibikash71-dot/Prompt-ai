package com.aipromptgenerater.aitricker.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BlueSoft = Color(0xFF3B82F6)
val BlueDark = Color(0xFF0F172A)
val BackgroundLight = Color(0xFFF8FAFC)

private val LightColors = lightColorScheme(
    primary = BlueSoft,
    background = BackgroundLight,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = BlueDark
)

@Composable
fun AiPromptTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
