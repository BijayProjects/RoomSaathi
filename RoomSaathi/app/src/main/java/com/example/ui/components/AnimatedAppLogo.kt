package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RoomSaathiVectorSymbol(
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeWidth = (w * 0.085f).coerceAtLeast(3f)

        // Draw Outer Hexagon/House Frame
        val housePath = Path().apply {
            moveTo(w * 0.50f, h * 0.15f) // Peak
            lineTo(w * 0.82f, h * 0.38f) // Top Right
            lineTo(w * 0.82f, h * 0.78f) // Bottom Right
            lineTo(w * 0.50f, h * 0.88f) // Bottom Point
            lineTo(w * 0.18f, h * 0.78f) // Bottom Left
            lineTo(w * 0.18f, h * 0.38f) // Top Left
            close()
        }
        drawPath(
            path = housePath,
            color = tint,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Draw Stylized "R" Monogram inside
        val rPath = Path().apply {
            // Vertical Spine of R
            moveTo(w * 0.36f, h * 0.33f)
            lineTo(w * 0.36f, h * 0.70f)

            // Upper Loop of R
            moveTo(w * 0.36f, h * 0.33f)
            cubicTo(
                w * 0.68f, h * 0.33f,
                w * 0.68f, h * 0.52f,
                w * 0.36f, h * 0.52f
            )

            // Diagonal Leg of R
            moveTo(w * 0.46f, h * 0.52f)
            lineTo(w * 0.64f, h * 0.70f)
        }
        drawPath(
            path = rPath,
            color = tint,
            style = Stroke(width = strokeWidth * 0.95f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

@Composable
fun AnimatedAppLogo(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    showLoadingProgress: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    accentColor: Color = MaterialTheme.colorScheme.primary
) {
    // Smooth infinite rotation for outer border loading progress animation
    val infiniteTransition = rememberInfiniteTransition(label = "logo_border_rotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_angle"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Outer Circular Border with Animated Rotating Loading Progress Bar
        if (showLoadingProgress) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokePx = (size.toPx() * 0.08f).coerceAtLeast(3.5f)
                val inset = strokePx / 2f
                val arcSize = Size(this.size.width - strokePx, this.size.height - strokePx)

                // Background track ring
                drawCircle(
                    color = accentColor.copy(alpha = 0.22f),
                    radius = (this.size.minDimension - strokePx) / 2f,
                    style = Stroke(width = strokePx)
                )

                // Rotating animated progress arc
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.10f),
                            accentColor,
                            accentColor.copy(alpha = 0.90f)
                        )
                    ),
                    startAngle = rotationAngle,
                    sweepAngle = 270f,
                    useCenter = false,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round),
                    topLeft = Offset(inset, inset),
                    size = arcSize
                )
            }
        }

        // Inner Circle Logo Container - 100% transparent vector logo (No white/grey background square!)
        Box(
            modifier = Modifier
                .fillMaxSize(if (showLoadingProgress) 0.78f else 0.92f)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            RoomSaathiVectorSymbol(
                modifier = Modifier
                    .fillMaxSize(0.68f)
                    .padding(2.dp),
                tint = accentColor
            )
        }
    }
}
