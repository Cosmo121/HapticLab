
package com.hartman.hapticlab.ui

import android.content.Context
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hartman.hapticlab.ui.theme.HapticLabTheme

@Composable
fun WelcomeScreen(
    onBeginClicked: () -> Unit,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit
) {
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

    val playHaptic = remember(vibrator) {
        { primitive: Int, fallback: Int ->
            val effect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && vibrator.areAllPrimitivesSupported(primitive)) {
                VibrationEffect.startComposition().addPrimitive(primitive, 1.0f).compose()
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

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "HapticNerd",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 48.sp
                )
                Button(
                    onClick = onBeginClicked,
                    modifier = Modifier.padding(horizontal = 64.dp, vertical = 32.dp)
                ) {
                    Text("Begin", fontSize = 30.sp)
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isDarkMode) "Dark Mode" else "Light Mode",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { 
                        onThemeToggle()
                        playHaptic(
                            VibrationEffect.Composition.PRIMITIVE_CLICK,
                            VibrationEffect.EFFECT_CLICK
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    HapticLabTheme(darkTheme = true) {
        WelcomeScreen(
            onBeginClicked = {},
            isDarkMode = true,
            onThemeToggle = {}
        )
    }
}
