package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

@Composable
fun PulsingIconButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (enabled) 1.35f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsingIconButton"
    )

    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.then(
            if (enabled) Modifier.scale(scale) else Modifier
        )
    ) {
        icon()
    }
}