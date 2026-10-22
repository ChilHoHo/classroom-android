package cc.samlab.classroom.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ClassroomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colors = if (darkTheme) {
        val dynamic = dynamicDarkColorScheme(context)
        dynamic.copy(
            background = lerp(Color(0xFF101114), dynamic.primary, 0.035f),
            surface = lerp(Color(0xFF101114), dynamic.primary, 0.035f),
            surfaceContainerLow = lerp(Color(0xFF202125), dynamic.primary, 0.04f),
            surfaceContainerHigh = lerp(Color(0xFF27282D), dynamic.primary, 0.05f),
        )
    } else {
        dynamicLightColorScheme(context)
    }
    MaterialExpressiveTheme(colorScheme = colors, content = content)
}
