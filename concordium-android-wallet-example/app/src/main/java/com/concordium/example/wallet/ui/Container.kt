package com.concordium.example.wallet.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Simple component that adds the example theme
 */
@Composable
fun Container(content: @Composable () -> Unit) {
    ExampleTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            color = MaterialTheme.colorScheme.background,
        ) {
            content()
        }
    }
}
