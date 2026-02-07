
package com.hartman.hapticlab.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hartman.hapticlab.ui.theme.HapticLabTheme

@Composable
fun WelcomeScreen(onBeginClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "HapticNerd",
            color = Color.White,
            fontSize = 48.sp
        )
        Button(
            onClick = onBeginClicked,
            modifier = Modifier.padding(horizontal = 64.dp, vertical = 32.dp)
        ) {
            Text("Begin", fontSize = 30.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    HapticLabTheme {
        WelcomeScreen(onBeginClicked = {})
    }
}
