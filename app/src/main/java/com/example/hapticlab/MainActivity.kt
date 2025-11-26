package com.example.hapticlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.hapticlab.ui.MainScreen
import com.example.hapticlab.ui.WelcomeScreen
import com.example.hapticlab.ui.theme.HapticLabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HapticLabTheme {
                HapticLabApp()
            }
        }
    }
}

@Composable
fun HapticLabApp() {
    var showWelcomeScreen by remember { mutableStateOf(true) }

    if (showWelcomeScreen) {
        WelcomeScreen(onBeginClicked = { showWelcomeScreen = false })
    } else {
        MainScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HapticLabTheme {
        HapticLabApp()
    }
}
