package com.example.editpixel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class EditingUI : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EditorUI()
        }
    }
}

@Composable
fun EditorUI() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            EditorToolBar()

            Image(
                painter = painterResource(id = R.drawable.editpixel),
                contentDescription = "Selected Image",
                modifier = Modifier.weight(1f).fillMaxWidth()
            )

            EditorBottomBar()
        }
    }
}

@Composable
fun EditorToolBar() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        IconButton(onClick = { /* Handle filter logic */ }) {
            Image(painter = painterResource(R.drawable.filter), contentDescription = "Filter")
        }
        IconButton(onClick = { /* Handle crop logic */ }) {
            Image(painter = painterResource(R.drawable.crop), contentDescription = "Crop")
        }
        IconButton(onClick = { /* Handle foreground and background logic */ }) {
            Image(painter = painterResource(R.drawable.foregroundandbackground), contentDescription = "Foreground and Background")
        }
        IconButton(onClick = { /* Handle bring to front logic */ }) {
            Image(painter = painterResource(R.drawable.bringtofront), contentDescription = "Bring to Front")
        }
    }
}

@Composable
fun EditorBottomBar() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        IconButton(onClick = { /* Handle saturation adjustment logic */ }) {
            Image(painter = painterResource(R.drawable.saturation), contentDescription = "Saturation")
        }
        IconButton(onClick = { /* Handle color palette logic */ }) {
            Image(painter = painterResource(R.drawable.colorpallete), contentDescription = "Color Palette")
        }
        IconButton(onClick = { /* Handle hue adjustment logic */ }) {
            Image(painter = painterResource(R.drawable.drop), contentDescription = "Hue")
        }
        IconButton(onClick = { /* Handle brush logic */ }) {
            Image(painter = painterResource(R.drawable.brush), contentDescription = "Brush")
        }
    }
}

@Preview
@Composable
fun PreviewEditorUI() {
    EditorUI()
}
