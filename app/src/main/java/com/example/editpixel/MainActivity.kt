package com.example.testinghue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import com.example.editpixel.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Load the image from drawable
            val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.test)

            // Display the HueSlider with the image
            HueSlider(imageBitmap)
        }
    }
}

@Composable
fun HueSlider(image: Bitmap) {
    var hue by remember { mutableStateOf(0f) }
    var adjustedImage by remember { mutableStateOf<ImageBitmap?>(null) }

    Column(
        modifier = Modifier.padding(30.dp)
    ) {
        Slider(
            value = hue,
            onValueChange = { newHue: Float ->
                hue = newHue
                adjustedImage = adjustHue(image, hue).asImageBitmap()
            },
            valueRange = 0f..360f,
            steps = 360,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display the adjusted image
        adjustedImage?.let { bitmap ->
            Image(
                bitmap = bitmap,
                contentDescription = null,
                modifier = Modifier
                    .width(500.dp) // Set a specific width
                    .height(500.dp), // Set a specific height
                contentScale = ContentScale.Fit
            )
        }
    }
}

// Extension function to convert a Bitmap to a Painter
//fun Bitmap.toPainter(): Painter {
//  return BitmapPainter(this)
//}

// Adjust hue of a Bitmap
fun adjustHue(bitmap: Bitmap, hue: Float): Bitmap {
    val cm = ColorMatrix().apply {
        setRotate(0, hue)
        setRotate(1, hue)
        setRotate(2, hue)
    }

    val filter = ColorMatrixColorFilter(cm)

    val paint = android.graphics.Paint().apply {
        colorFilter = filter
    }

    val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(resultBitmap)
    canvas.drawBitmap(bitmap, 0f, 0f, paint)

    return resultBitmap
}
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//TestinghueTheme {
//    Greeting("Android")
// }
//}
