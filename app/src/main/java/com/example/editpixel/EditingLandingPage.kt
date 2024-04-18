package com.example.editpixel

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.example.editpixel.ui.theme.EditPixelTheme

class EditingLandingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bitmap:Bitmap=  intent.getParcelableExtra<Bitmap>("bitmap")!!
        setContent{
            EditPixelTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    DrawBitmap(bitmap)
                }
            }
        }
    }
}
@Composable
fun DrawBitmap(bitmap: Bitmap, modifier: Modifier = Modifier) {
    val imageBitmap = bitmap.asImageBitmap()

    Canvas(modifier = modifier.fillMaxSize()) {
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawBitmap(imageBitmap.asAndroidBitmap(), 0f, 0f, null)
        }
    }
}