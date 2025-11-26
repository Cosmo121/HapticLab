
package com.example.hapticlab.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.hapticlab.ui.theme.HapticLabTheme

@Composable
fun WelcomeScreen(onBeginClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "HapticLab")
        Button(onClick = onBeginClicked) {
            Text("Begin")
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
