package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun PulsingFloatingActionButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsingFloatingActionButton"
    )

    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .size(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .testTag("create_diary_entry_btn")

    ) {
        icon()
    }
}