
package com.example.hapticlab.ui

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hapticlab.R
import com.example.hapticlab.ui.theme.HapticLabTheme
import kotlinx.coroutines.delay

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Haptic Engine Status: ")
                    withStyle(style = SpanStyle(color = Color.Green)) {
                        append("Ready")
                    }
                }
            )
        }
        // Bento-box grid
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // First Row
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BentoBoxItem(modifier = Modifier.weight(1f)) {
                    var isSwitchOn by remember { mutableStateOf(false) }
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val view = LocalView.current

                    LaunchedEffect(isPressed) {
                        if (isPressed) {
                            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                        }
                    }

                    val imageRes = if (isSwitchOn || isPressed) {
                        R.drawable.light_switch_on
                    } else {
                        R.drawable.light_switch_off
                    }

                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "Light switch",
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { isSwitchOn = !isSwitchOn }
                            )
                    )
                }
                BentoBoxItem(modifier = Modifier.weight(1f)) {
                    Text(text = "Box")
                }
            }
            // Second Row
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BentoBoxItem(modifier = Modifier.weight(1f)) {
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val view = LocalView.current
                    var targetRotation by remember { mutableFloatStateOf(0f) }
                    val rotationAngle by animateFloatAsState(targetValue = targetRotation, label = "gear-rotation")

                    LaunchedEffect(isPressed) {
                        if (isPressed) {
                            view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.gear_solid_full),
                        contentDescription = "Gear",
                        modifier = Modifier
                            .graphicsLayer {
                                rotationZ = rotationAngle
                            }
                            .fillMaxSize()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { targetRotation += 10f }
                            )
                    )
                }
                BentoBoxItem(modifier = Modifier.weight(1f)) {
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val view = LocalView.current
                    var batteryLevel by remember { mutableStateOf(0) }

                    LaunchedEffect(isPressed) {
                        if (isPressed) {
                            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                            delay(1000)
                            batteryLevel = 1
                            delay(1000)
                            batteryLevel = 2
                            delay(1000)
                            batteryLevel = 3
                            delay(1000)
                            batteryLevel = 4
                        } else {
                            batteryLevel = 0
                        }
                    }

                    val imageRes = when (batteryLevel) {
                        1 -> R.drawable.battery_quarter_solid_full
                        2 -> R.drawable.battery_half_solid_full
                        3 -> R.drawable.battery_three_quarters_solid_full
                        4 -> R.drawable.battery_full_solid_full
                        else -> R.drawable.battery_empty_solid_full
                    }

                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "Battery",
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { /* No-op */ }
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun BentoBoxItem(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    HapticLabTheme {
        MainScreen()
    }
}
