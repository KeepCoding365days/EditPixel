package com.example.editpixel

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.editpixel.ui.theme.EditPixelTheme

class Foreground : AppCompatActivity() {
    private var imgBitmap: Bitmap = BitmapObject.bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)

        setContent {
            EditPixelTheme {
                Surface(color = Color.Black) {
                    ImagePickerAndAnalyzer(imgBitmap)
                }
            }
        }
    }

    @Composable
    fun ImagePickerAndAnalyzer(imageBitmap: Bitmap) {
        var selectedColor by remember { mutableStateOf<Color?>(null) }
        var previousBitmaps by remember { mutableStateOf<List<Bitmap>>(listOf(imageBitmap)) }
        var currentIndex by remember { mutableStateOf(0) }
        var showColorOptions by remember { mutableStateOf(false) }
        var tintedBitmap by remember(selectedColor, imageBitmap) {
            mutableStateOf(
                selectedColor?.let { color ->
                    applyColorToForegroundOnly(previousBitmaps[currentIndex], color)
                } ?: previousBitmaps[currentIndex]
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = tintedBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (currentIndex > 0) {
                            currentIndex--
                            tintedBitmap = previousBitmaps[currentIndex]
                        }
                    },
                    enabled = currentIndex > 0
                ) {
                    Text(text = "Undo")
                }
                Button(
                    onClick = {
                        // Go back to the previous activity or editor
                        goToEditor()
                    }
                ) {
                    Text(text = "Back")
                }
                Button(
                    onClick = {
                        if (currentIndex < previousBitmaps.size - 1) {
                            currentIndex++
                            tintedBitmap = previousBitmaps[currentIndex]
                        }
                    },
                    enabled = currentIndex < previousBitmaps.size - 1
                ) {
                    Text(text = "Redo")
                }
                Button(
                    onClick = {
                        // Save the currently edited bitmap to the global bitmap store
                        BitmapObject.bitmap = tintedBitmap
                    }
                ) {
                    Text(text = "Save")
                }
            }

            Button(
                onClick = {
                    showColorOptions = !showColorOptions
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Text(text = "Select Color", fontSize = 18.sp)
            }

            if (showColorOptions) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 60.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    itemsIndexed(
                        listOf(
                            Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan,
                            Color.Magenta, Color.Gray, Color.Black, Color.White, Color.DarkGray
                        )
                    ) { index, color ->
                        Button(
                            onClick = {
                                selectedColor = color
                                val newBitmap = applyColorToForegroundOnly(tintedBitmap, color)
                                tintedBitmap = newBitmap
                                currentIndex++
                                previousBitmaps = previousBitmaps.take(currentIndex) + newBitmap
                                showColorOptions = false
                            },
                            modifier = Modifier
                                .background(color = Color.Transparent, shape = CircleShape)
                                .padding(4.dp)
                                .size(50.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(color = color, shape = CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun applyColorToForegroundOnly(bitmap: Bitmap, color: Color): Bitmap {
        // Create a mutable copy of the bitmap to apply color
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        // Convert the Jetpack Compose Color to Android Color
        val androidColor = android.graphics.Color.argb(
            (color.alpha * 255).toInt(),
            (color.red * 255).toInt(),
            (color.green * 255).toInt(),
            (color.blue * 255).toInt()
        )

        // Create a paint object for color manipulation
        val paint = Paint().apply {
            colorFilter = PorterDuffColorFilter(androidColor, PorterDuff.Mode.SRC_IN)
        }

        // Create a Canvas to draw on the mutable bitmap
        val canvas = Canvas(mutableBitmap)

        // Apply a simple threshold to detect foreground pixels (adjust threshold value as needed)
        val threshold = 128 // Example threshold value
        for (y in 0 until mutableBitmap.height) {
            for (x in 0 until mutableBitmap.width) {
                val pixel = mutableBitmap.getPixel(x, y)
                val alpha = pixel shr 24 and 0xff
                val red = pixel shr 16 and 0xff
                val green = pixel shr 8 and 0xff
                val blue = pixel and 0xff
                val luminance = (red + green + blue) / 3
                if (luminance < threshold) {
                    canvas.drawPoint(x.toFloat(), y.toFloat(), paint)
                }
            }
        }

        // Return the modified bitmap
        return mutableBitmap
    }





    private fun goToEditor() {
        val intent = Intent(applicationContext, Editor::class.java)
        startActivity(intent)
        finish()
    }
}