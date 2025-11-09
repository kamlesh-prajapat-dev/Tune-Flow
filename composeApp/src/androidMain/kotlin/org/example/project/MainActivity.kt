package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import org.example.project.player.AudioPlayer

class MainActivity : ComponentActivity() {
    private val player by lazy { AudioPlayer() }


    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(player)
        }
    }
}
