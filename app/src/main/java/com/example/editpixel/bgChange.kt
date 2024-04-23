package com.example.editpixel
import kotlinx.coroutines.withContext
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import androidx.core.graphics.ColorUtils
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.*
import com.example.editpixel.ui.theme.EditPixelTheme

// import package from your file


class bgChange : AppCompatActivity() {

    //remove pickImageLauncher functio
    private var ImgBitmap:Bitmap=BitmapObject.bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //and uncomment following
        setContent {
            EditPixelTheme {
                ImagePickerAndAnalyzer(ImgBitmap)
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
                    applyColorToBackgroundOnly(imageBitmap, color)
                } ?: imageBitmap
            )
        }
        var savedBtn by remember {
            mutableStateOf(false)
        }
        var backBtn by remember {
            mutableStateOf(false)
        }
        if(savedBtn){
            ImgBitmap=tintedBitmap
            savedBtn=false
        }
        if(backBtn){
            goToEditor()
            backBtn=false
        }


        val isUniformBackground = analyzeBackground(imageBitmap)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Display the image
            Image(
                bitmap = tintedBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            // Undo and Redo buttons
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
                        backBtn=true
                    }
                ) {
                    Text(text = "Back")
                }
                Button(
                    onClick = {
                        savedBtn=true
                        },
                    enabled = currentIndex > 0
                ) {
                    Text(text = "Save")
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
            }

            // Color picker button
            Button(
                onClick = {
                    // Toggle to show/hide color options
                    showColorOptions = !showColorOptions
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Text(text = "Select Color", fontSize = 18.sp)
            }

            // Color picker row
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
                                // Set the selected color
                                selectedColor = color
                                // Apply color to background only
                                val newBitmap = applyColorToBackgroundOnly(imageBitmap, color)
                                tintedBitmap = newBitmap
                                // Add the bitmap to the previous bitmaps list
                                previousBitmaps = previousBitmaps.take(currentIndex + 1) + newBitmap
                                currentIndex++
                                // Hide color options after selecting
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
                            ) {
                                // Empty content
                            }
                        }
                    }
                }
            }

            if (!isUniformBackground) {
                // Display a message indicating that the background color cannot be changed
                Text(
                    text = "Cannot change background color",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        //return tintedBitmap here
    }


    private fun applyColorToBackgroundOnly(bitmap: Bitmap, color: Color): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        val colorInt = color.toArgb()

        // Get pixel data from the bitmap
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        mutableBitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        // Check if the background is uniform
        val isUniformBackground = analyzeBackground(bitmap)

        if (isUniformBackground) {
            val dominantColor = Palette.from(mutableBitmap).generate().getDominantColor(-16777216)

            // Define a threshold to determine similarity with the dominant color
            val threshold = 50

            for (i in pixels.indices) {
                val pixelColor = pixels[i]

                // Check if the pixel color is similar to the dominant color
                if (isColorSimilar(pixelColor, dominantColor, threshold)) {
                    pixels[i] = colorInt
                }
            }
        }

        // Create a new bitmap with modified pixels
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }

    private fun isCloseToBackground(
        pixelColor: Int,
        backgroundColor: Int,
        threshold: Int
    ): Boolean {
        val backgroundColorObj = Color(backgroundColor)

        val redDiff = Math.abs(Color(pixelColor).red - backgroundColorObj.red)
        val greenDiff = Math.abs(Color(pixelColor).green - backgroundColorObj.green)
        val blueDiff = Math.abs(Color(pixelColor).blue - backgroundColorObj.blue)

        return redDiff <= threshold && greenDiff <= threshold && blueDiff <= threshold
    }


    private fun analyzeBackground(bitmap: Bitmap): Boolean {
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val palette = Palette.from(mutableBitmap).generate()

        // Get dominant color
        val dominantColor = palette.getDominantColor(-16777216)

        // Check if all colors in the palette are close to the dominant color
        val backgroundThreshold = 50 // Adjust this threshold as needed
        return palette.swatches.all { swatch ->
            ColorUtils.calculateLuminance(swatch.rgb) < backgroundThreshold
        }
    }
    private fun goToEditor(){
        val i=Intent(applicationContext,Editor::class.java)

        BitmapObject.bitmap=ImgBitmap
        startActivity(i)
        finish()
    }



    private fun isColorSimilar(color1: Int, color2: Int, threshold: Int): Boolean {
        val r1 = (color1 shr 16) and 0xFF
        val g1 = (color1 shr 8) and 0xFF
        val b1 = color1 and 0xFF

        val r2 = (color2 shr 16) and 0xFF
        val g2 = (color2 shr 8) and 0xFF
        val b2 = color2 and 0xFF

        return (Math.abs(r1 - r2) < threshold &&
                Math.abs(g1 - g2) < threshold &&
                Math.abs(b1 - b2) < threshold)
    }
}


