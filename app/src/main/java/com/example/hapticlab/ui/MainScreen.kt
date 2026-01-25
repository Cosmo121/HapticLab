
package com.example.hapticlab.ui

import android.content.Context
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hapticlab.R
import com.example.hapticlab.ui.theme.HapticLabTheme
import kotlinx.coroutines.delay

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues()),
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
                },
                color = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        // Bento-box grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // First Row
            Row(
                modifier = Modifier.height(180.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BentoBoxItem(modifier = Modifier.weight(1f)) {
                    var isSwitchOn by remember { mutableStateOf(false) }
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val context = LocalContext.current
                    val vibratorManager =
                        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    val vibrator = vibratorManager.defaultVibrator

                    LaunchedEffect(isPressed) {
                        if (isPressed) {
                            val vibrationEffect = VibrationEffect.startComposition()
                                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_CLICK, 1.0f)
                                .compose()
                            vibrator.vibrate(vibrationEffect)
                        }
                    }

                    val imageRes = if (isSwitchOn || isPressed) {
                        R.drawable.light_switch_on
                    } else {
                        R.drawable.light_switch_off
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = "Light switch",
                            modifier = Modifier
                                .size(80.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = { isSwitchOn = !isSwitchOn }
                                )
                                .align(Alignment.Center)
                        )
                        Text(
                            text = "CLICK",
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp)
                        )
                    }
                }
                BentoBoxItem(modifier = Modifier.weight(1f)) {
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val context = LocalContext.current
                    val vibratorManager =
                        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    val vibrator = vibratorManager.defaultVibrator

                    LaunchedEffect(isPressed) {
                        if (isPressed) {
                            val vibrationEffect = VibrationEffect.startComposition()
                                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD, 1.0f)
                                .compose()
                            vibrator.vibrate(vibrationEffect)
                        }
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.drum_solid_full),
                            contentDescription = "Drum",
                            modifier = Modifier
                                .size(80.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = { /* No-op */ }
                                )
                                .align(Alignment.Center)
                        )
                        Text(
                            text = "THUD",
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp)
                        )
                    }
                }
                BentoBoxItem(modifier = Modifier.weight(1f)) {
                    val context = LocalContext.current
                    val vibratorManager =
                        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    val vibrator = vibratorManager.defaultVibrator

                    var checkStates by remember { mutableStateOf(listOf(false, false, false, false)) }

                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Image(
                                    painter = painterResource(id = if (checkStates[0]) R.drawable.circle_check_solid_full else R.drawable.circle_solid_full),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp).clickable {
                                        checkStates = checkStates.toMutableList().apply { this[0] = !this[0] }
                                        val vibrationEffect = VibrationEffect.startComposition()
                                            .addPrimitive(VibrationEffect.Composition.PRIMITIVE_LOW_TICK, 1.0f)
                                            .compose()
                                        vibrator.vibrate(vibrationEffect)
                                    }
                                )
                                Image(
                                    painter = painterResource(id = if (checkStates[1]) R.drawable.circle_check_solid_full else R.drawable.circle_solid_full),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp).clickable {
                                        checkStates = checkStates.toMutableList().apply { this[1] = !this[1] }
                                        val vibrationEffect = VibrationEffect.startComposition()
                                            .addPrimitive(VibrationEffect.Composition.PRIMITIVE_LOW_TICK, 1.0f)
                                            .compose()
                                        vibrator.vibrate(vibrationEffect)
                                    }
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Image(
                                    painter = painterResource(id = if (checkStates[2]) R.drawable.circle_check_solid_full else R.drawable.circle_solid_full),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp).clickable {
                                        checkStates = checkStates.toMutableList().apply { this[2] = !this[2] }
                                        val vibrationEffect = VibrationEffect.startComposition()
                                            .addPrimitive(VibrationEffect.Composition.PRIMITIVE_LOW_TICK, 1.0f)
                                            .compose()
                                        vibrator.vibrate(vibrationEffect)
                                    }
                                )
                                Image(
                                    painter = painterResource(id = if (checkStates[3]) R.drawable.circle_check_solid_full else R.drawable.circle_solid_full),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp).clickable {
                                        checkStates = checkStates.toMutableList().apply { this[3] = !this[3] }
                                        val vibrationEffect = VibrationEffect.startComposition()
                                            .addPrimitive(VibrationEffect.Composition.PRIMITIVE_LOW_TICK, 1.0f)
                                            .compose()
                                        vibrator.vibrate(vibrationEffect)
                                    }
                                )
                            }
                        }
                        Text(
                            text = "LOW_TICK",
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp)
                        )
                    }
                }
            }
            // Second Row
            Row(
                modifier = Modifier.height(180.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BentoBoxItem(modifier = Modifier.weight(1f)) {
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val context = LocalContext.current
                    val vibratorManager =
                        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    val vibrator = vibratorManager.defaultVibrator
                    var targetRotation by remember { mutableFloatStateOf(0f) }
                    val rotationAngle by animateFloatAsState(
                        targetValue = targetRotation,
                        label = "gear-rotation"
                    )

                    LaunchedEffect(isPressed) {
                        if (isPressed) {
                            val vibrationEffect = VibrationEffect.startComposition()
                                .addPrimitive(VibrationEffect.Composition.PRIMITIVE_SPIN, 1.0f)
                                .compose()
                            vibrator.vibrate(vibrationEffect)
                        }
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.gear_solid_full),
                            contentDescription = "Gear",
                            modifier = Modifier
                                .size(80.dp)
                                .graphicsLayer {
                                    rotationZ = rotationAngle
                                }
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = { targetRotation += 10f }
                                )
                                .align(Alignment.Center)
                        )
                        Text(
                            text = "SPIN",
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp)
                        )
                    }
                }
                BentoBoxItem(modifier = Modifier.weight(1f)) {
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val context = LocalContext.current
                    val vibratorManager =
                        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    val vibrator = vibratorManager.defaultVibrator
                    var batteryLevel by remember { mutableIntStateOf(0) }

                    LaunchedEffect(isPressed) {
                        if (isPressed) {
                            if (batteryLevel == 0) { // Start animation only if it's not already running
                                val vibrationEffect = VibrationEffect.startComposition()
                                    .addPrimitive(
                                        VibrationEffect.Composition.PRIMITIVE_SLOW_RISE,
                                        1.0f
                                    )
                                    .compose()
                                vibrator.vibrate(vibrationEffect)
                                for (i in 1..4) {
                                    delay(125)
                                    batteryLevel = i
                                }
                            }
                        } else {
                            if (batteryLevel > 0) {
                                val vibrationEffect = VibrationEffect.startComposition()
                                    .addPrimitive(
                                        VibrationEffect.Composition.PRIMITIVE_QUICK_FALL,
                                        1.0f
                                    )
                                    .compose()
                                vibrator.vibrate(vibrationEffect)

                                val initialLevel = batteryLevel
                                val stepDelay = 250L / initialLevel

                                for (i in initialLevel downTo 1) {
                                    delay(stepDelay)
                                    batteryLevel = i - 1
                                }
                            }
                        }
                    }

                    val imageRes = when (batteryLevel) {
                        1 -> R.drawable.battery_quarter_solid_full
                        2 -> R.drawable.battery_half_solid_full
                        3 -> R.drawable.battery_three_quarters_solid_full
                        4 -> R.drawable.battery_full_solid_full
                        else -> R.drawable.battery_empty_solid_full
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = "Battery",
                            modifier = Modifier
                                .size(80.dp)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null,
                                    onClick = { /* No-op */ }
                                )
                                .align(Alignment.Center)
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "RISE/FALL",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "(Press and Hold)",
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
                BentoBoxItem(modifier = Modifier.weight(1f)) {
                    val context = LocalContext.current
                    val vibratorManager =
                        context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    val vibrator = vibratorManager.defaultVibrator

                    var checkStates by remember { mutableStateOf(listOf(false, false, false, false)) }

                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Image(
                                    painter = painterResource(id = if (checkStates[0]) R.drawable.circle_check_solid_full else R.drawable.circle_solid_full),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp).clickable {
                                        checkStates = checkStates.toMutableList().apply { this[0] = !this[0] }
                                        val vibrationEffect = VibrationEffect.startComposition()
                                            .addPrimitive(VibrationEffect.Composition.PRIMITIVE_TICK, 1.0f)
                                            .compose()
                                        vibrator.vibrate(vibrationEffect)
                                    }
                                )
                                Image(
                                    painter = painterResource(id = if (checkStates[1]) R.drawable.circle_check_solid_full else R.drawable.circle_solid_full),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp).clickable {
                                        checkStates = checkStates.toMutableList().apply { this[1] = !this[1] }
                                        val vibrationEffect = VibrationEffect.startComposition()
                                            .addPrimitive(VibrationEffect.Composition.PRIMITIVE_TICK, 1.0f)
                                            .compose()
                                        vibrator.vibrate(vibrationEffect)
                                    }
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Image(
                                    painter = painterResource(id = if (checkStates[2]) R.drawable.circle_check_solid_full else R.drawable.circle_solid_full),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp).clickable {
                                        checkStates = checkStates.toMutableList().apply { this[2] = !this[2] }
                                        val vibrationEffect = VibrationEffect.startComposition()
                                            .addPrimitive(VibrationEffect.Composition.PRIMITIVE_TICK, 1.0f)
                                            .compose()
                                        vibrator.vibrate(vibrationEffect)
                                    }
                                )
                                Image(
                                    painter = painterResource(id = if (checkStates[3]) R.drawable.circle_check_solid_full else R.drawable.circle_solid_full),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp).clickable {
                                        checkStates = checkStates.toMutableList().apply { this[3] = !this[3] }
                                        val vibrationEffect = VibrationEffect.startComposition()
                                            .addPrimitive(VibrationEffect.Composition.PRIMITIVE_TICK, 1.0f)
                                            .compose()
                                        vibrator.vibrate(vibrationEffect)
                                    }
                                )
                            }
                        }
                        Text(
                            text = "TICK",
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp)
                        )
                    }
                }
            }
            
            // New Large Quadrant for information
            BentoBoxItem(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                Text(
                    text = "The above haptic examples use effects that may not be supported on all devices. For example, proper CLICK and RISE/FALL actions require a device to use linear resonant actuators, as opposed to older eccentric rotating mass motors.",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun BentoBoxItem(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = Color.DarkGray
        )
    ) {
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
