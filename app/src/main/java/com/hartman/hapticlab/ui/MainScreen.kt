
package com.hartman.hapticlab.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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
import androidx.compose.foundation.layout.BoxWithConstraints
import com.hartman.hapticlab.ui.theme.HapticLabTheme
import com.hartman.hapticlab.R
import kotlinx.coroutines.delay

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
                ?: (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    // Helper for simple haptics with fallback
    val playHaptic = remember(vibrator) {
        { primitive: Int, fallback: Int, scale: Float ->
            val effect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && vibrator.areAllPrimitivesSupported(primitive)) {
                VibrationEffect.startComposition().addPrimitive(primitive, scale).compose()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                VibrationEffect.createPredefined(fallback)
            } else {
                VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    val attributes = VibrationAttributes.Builder()
                        .setUsage(VibrationAttributes.USAGE_TOUCH)
                        .build()
                    vibrator.vibrate(effect, attributes)
                } catch (_: Exception) {
                    vibrator.vibrate(effect)
                }
            } else {
                vibrator.vibrate(effect)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState()),
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

                    val imageRes = if (isSwitchOn || isPressed) {
                        R.drawable.light_switch_on
                    } else {
                        R.drawable.light_switch_off
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    isSwitchOn = !isSwitchOn
                                    playHaptic(
                                        VibrationEffect.Composition.PRIMITIVE_CLICK,
                                        VibrationEffect.EFFECT_CLICK,
                                        1.0f
                                    )
                                }
                            )
                    ) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = "Light switch",
                            modifier = Modifier
                                .size(80.dp)
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

                    LaunchedEffect(isPressed) {
                        if (isPressed) {
                            playHaptic(
                                VibrationEffect.Composition.PRIMITIVE_THUD,
                                VibrationEffect.EFFECT_HEAVY_CLICK,
                                1.0f
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { /* No-op */ }
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.drum_solid_full),
                            contentDescription = "Drum",
                            modifier = Modifier
                                .size(80.dp)
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
                                        playHaptic(
                                            VibrationEffect.Composition.PRIMITIVE_LOW_TICK,
                                            VibrationEffect.EFFECT_TICK,
                                            1.0f
                                        )
                                    }
                                )
                                Image(
                                    painter = painterResource(id = if (checkStates[1]) R.drawable.circle_check_solid_full else R.drawable.circle_solid_full),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp).clickable {
                                        checkStates = checkStates.toMutableList().apply { this[1] = !this[1] }
                                        playHaptic(
                                            VibrationEffect.Composition.PRIMITIVE_LOW_TICK,
                                            VibrationEffect.EFFECT_TICK,
                                            1.0f
                                        )
                                    }
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Image(
                                    painter = painterResource(id = if (checkStates[2]) R.drawable.circle_check_solid_full else R.drawable.circle_solid_full),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp).clickable {
                                        checkStates = checkStates.toMutableList().apply { this[2] = !this[2] }
                                        playHaptic(
                                            VibrationEffect.Composition.PRIMITIVE_LOW_TICK,
                                            VibrationEffect.EFFECT_TICK,
                                            1.0f
                                        )
                                    }
                                )
                                Image(
                                    painter = painterResource(id = if (checkStates[3]) R.drawable.circle_check_solid_full else R.drawable.circle_solid_full),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp).clickable {
                                        checkStates = checkStates.toMutableList().apply { this[3] = !this[3] }
                                        playHaptic(
                                            VibrationEffect.Composition.PRIMITIVE_LOW_TICK,
                                            VibrationEffect.EFFECT_TICK,
                                            1.0f
                                        )
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
                    var targetRotation by remember { mutableFloatStateOf(0f) }
                    val rotationAngle by animateFloatAsState(
                        targetValue = targetRotation,
                        label = "gear-rotation"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = {
                                    targetRotation += 15f
                                    playHaptic(
                                        VibrationEffect.Composition.PRIMITIVE_SPIN,
                                        VibrationEffect.EFFECT_CLICK,
                                        1.0f
                                    )
                                }
                            )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.gear_solid_full),
                            contentDescription = "Gear",
                            modifier = Modifier
                                .size(80.dp)
                                .graphicsLayer {
                                    rotationZ = rotationAngle
                                }
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
                    var batteryLevel by remember { mutableIntStateOf(0) }

                    LaunchedEffect(isPressed) {
                        if (isPressed) {
                            if (batteryLevel == 0) { // Start animation only if it's not already running
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && vibrator.areAllPrimitivesSupported(
                                        VibrationEffect.Composition.PRIMITIVE_SLOW_RISE
                                    )
                                ) {
                                    vibrator.vibrate(
                                        VibrationEffect.startComposition()
                                            .addPrimitive(
                                                VibrationEffect.Composition.PRIMITIVE_SLOW_RISE,
                                                1.0f
                                            )
                                            .compose()
                                    )
                                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    vibrator.vibrate(
                                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                                    )
                                } else {
                                    vibrator.vibrate(
                                        VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE)
                                    )
                                }
                                for (i in 1..4) {
                                    delay(125)
                                    batteryLevel = i
                                }
                            }
                        } else {
                            if (batteryLevel > 0) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && vibrator.areAllPrimitivesSupported(
                                        VibrationEffect.Composition.PRIMITIVE_QUICK_FALL
                                    )
                                ) {
                                    vibrator.vibrate(
                                        VibrationEffect.startComposition()
                                            .addPrimitive(
                                                VibrationEffect.Composition.PRIMITIVE_QUICK_FALL,
                                                1.0f
                                            )
                                            .compose()
                                    )
                                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    vibrator.vibrate(
                                        VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                                    )
                                } else {
                                    vibrator.vibrate(
                                        VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE)
                                    )
                                }

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

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { /* No-op */ }
                            )
                    ) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = "Battery",
                            modifier = Modifier
                                .size(80.dp)
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
                                        playHaptic(
                                            VibrationEffect.Composition.PRIMITIVE_TICK,
                                            VibrationEffect.EFFECT_TICK,
                                            1.0f
                                        )
                                    }
                                )
                                Image(
                                    painter = painterResource(id = if (checkStates[1]) R.drawable.circle_check_solid_full else R.drawable.circle_solid_full),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp).clickable {
                                        checkStates = checkStates.toMutableList().apply { this[1] = !this[1] }
                                        playHaptic(
                                            VibrationEffect.Composition.PRIMITIVE_TICK,
                                            VibrationEffect.EFFECT_TICK,
                                            1.0f
                                        )
                                    }
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Image(
                                    painter = painterResource(id = if (checkStates[2]) R.drawable.circle_check_solid_full else R.drawable.circle_solid_full),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp).clickable {
                                        checkStates = checkStates.toMutableList().apply { this[2] = !this[2] }
                                        playHaptic(
                                            VibrationEffect.Composition.PRIMITIVE_TICK,
                                            VibrationEffect.EFFECT_TICK,
                                            1.0f
                                        )
                                    }
                                )
                                Image(
                                    painter = painterResource(id = if (checkStates[3]) R.drawable.circle_check_solid_full else R.drawable.circle_solid_full),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp).clickable {
                                        checkStates = checkStates.toMutableList().apply { this[3] = !this[3] }
                                        playHaptic(
                                            VibrationEffect.Composition.PRIMITIVE_TICK,
                                            VibrationEffect.EFFECT_TICK,
                                            1.0f
                                        )
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
            
            // Slider Quadrant
            BentoBoxItem(modifier = Modifier.fillMaxWidth().height(110.dp)) {
                var sliderValue by remember { mutableFloatStateOf(0.5f) }
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "FINE SCRUB",
                        color = Color.LightGray,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Slider(
                        value = sliderValue,
                        onValueChange = { newValue ->
                            // Even more frequent steps (150 vs 100) and softer intensity (0.2f)
                            if ((newValue * 150).toInt() != (sliderValue * 150).toInt()) {
                                playHaptic(
                                    VibrationEffect.Composition.PRIMITIVE_LOW_TICK,
                                    VibrationEffect.EFFECT_TICK,
                                    0.2f
                                )
                            }
                            sliderValue = newValue
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color.White,
                            inactiveTrackColor = Color.Gray
                        )
                    )
                }
            }

            // Pinball Quadrant
            BentoBoxItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                PinballEngine(playHaptic = playHaptic)
            }
        }
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun PinballEngine(playHaptic: (Int, Int, Float) -> Unit) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager }
    val accelerometer = remember { sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    var accelX by remember { mutableFloatStateOf(0f) }
    var accelY by remember { mutableFloatStateOf(0f) }

    if (sensorManager != null && accelerometer != null) {
        DisposableEffect(Unit) {
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                        accelX = -event.values[0]
                        accelY = event.values[1]
                    }
                }
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }
            sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
            onDispose {
                sensorManager.unregisterListener(listener)
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()
        val ballRadius = 40f

        var posX by remember { mutableFloatStateOf(width / 2) }
        var posY by remember { mutableFloatStateOf(height / 2) }
        var velX by remember { mutableFloatStateOf(0f) }
        var velY by remember { mutableFloatStateOf(0f) }

        LaunchedEffect(Unit) {
            while (true) {
                withFrameNanos { _ ->
                    // Physics constants
                    val sensitivity = 0.5f
                    val friction = 0.98f
                    val bounce = -0.6f

                    // Update velocity
                    velX = (velX + accelX * sensitivity) * friction
                    velY = (velY + accelY * sensitivity) * friction

                    // Update position
                    var nextX = posX + velX
                    var nextY = posY + velY

                    // Wall collision detection
                    if (nextX - ballRadius < 0) {
                        if (posX - ballRadius > 1f && velX < -1f) { // Significant hit
                            playHaptic(
                                VibrationEffect.Composition.PRIMITIVE_THUD,
                                VibrationEffect.EFFECT_HEAVY_CLICK,
                                1.0f
                            )
                        }
                        nextX = ballRadius
                        velX = if (Math.abs(velX) < 1.5f) 0f else velX * bounce
                    } else if (nextX + ballRadius > width) {
                        if (posX + ballRadius < width - 1f && velX > 1f) {
                            playHaptic(
                                VibrationEffect.Composition.PRIMITIVE_THUD,
                                VibrationEffect.EFFECT_HEAVY_CLICK,
                                1.0f
                            )
                        }
                        nextX = width - ballRadius
                        velX = if (Math.abs(velX) < 1.5f) 0f else velX * bounce
                    }

                    if (nextY - ballRadius < 0) {
                        if (posY - ballRadius > 1f && velY < -1f) {
                            playHaptic(
                                VibrationEffect.Composition.PRIMITIVE_THUD,
                                VibrationEffect.EFFECT_HEAVY_CLICK,
                                1.0f
                            )
                        }
                        nextY = ballRadius
                        velY = if (Math.abs(velY) < 1.5f) 0f else velY * bounce
                    } else if (nextY + ballRadius > height) {
                        if (posY + ballRadius < height - 1f && velY > 1f) {
                            playHaptic(
                                VibrationEffect.Composition.PRIMITIVE_THUD,
                                VibrationEffect.EFFECT_HEAVY_CLICK,
                                1.0f
                            )
                        }
                        nextY = height - ballRadius
                        velY = if (Math.abs(velY) < 1.5f) 0f else velY * bounce
                    }

                    posX = nextX
                    posY = nextY
                }
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White,
                radius = ballRadius,
                center = Offset(posX, posY)
            )
        }
        
        Text(
            text = "TILT TO ROLL",
            color = Color.LightGray.copy(alpha = 0.5f),
            fontSize = 10.sp,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp)
        )
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
