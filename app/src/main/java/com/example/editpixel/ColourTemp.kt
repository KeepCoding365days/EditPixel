package com.example.editpixel 

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap




class ColourTempActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bitmap = BitmapObject.bitmap
        // Ensure that the bitmap is not null
        bitmap?.let {
            setContent {
                ColourTempUI(it)
            }
        }
    }

    @Composable
    fun ColourTempUI(bitmap: Bitmap) {
        var tempBitmap by remember { mutableStateOf(bitmap) }
        var temp by remember { mutableStateOf(0f) }
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    bitmap = tempBitmap.asImageBitmap(),
                    contentDescription = "Editable Image",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                Slider(value = temp, onValueChange = { newTemp ->
                    temp = newTemp
                    tempBitmap = changeBitmapTemperature(tempBitmap, temp)
                }, valueRange = 0f..100f)
            }
        }
    }

    private fun changeBitmapTemperature(source: Bitmap, temperature: Float): Bitmap {
        val mutableBitmap = source.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix().apply {
            setTemperature(temperature)
        }
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(mutableBitmap, 0f, 0f, paint)
        return mutableBitmap
    }

    private fun ColorMatrix.setTemperature(value: Float) {
        
        val scale = value / 100f

       
        val r = 1f + scale
        val g = 1f + scale * 0.8f // green less affected by temperature
        val b = 1f - scale // blue becomes less prominent with warmth

        set(floatArrayOf(
            r, 0f, 0f, 0f, 0f,
            0f, g, 0f, 0f, 0f,
            0f, 0f, b, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        ))
    }

}
