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
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.editpixel.ui.theme.EditPixelTheme

class EditingLandingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bitmap:Bitmap= BitmapObject.bitmap
        setContent{
            EditPixelTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    Editor(bitmap = bitmap)
                }
            }
        }
    }
}
@Composable
fun Editor(bitmap: Bitmap){
    Column() {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        {
            Text("Filter")
            Text("Advanced")
            Text("Crop")
            Text("FgChanger")
        }
        Image(
            bitmap = bitmap.asImageBitmap(), contentDescription = "Image", modifier = Modifier
                .size(width = 600.dp, height = 600.dp).padding(6.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Filter")
            Text("Advanced")
            Text("Crop")
            Text("FgChanger")
        }
    }
}
@Composable
fun DrawBitmap(bitmap: Bitmap, modifier: Modifier = Modifier) {
    val imageBitmap = bitmap.asImageBitmap()

    Canvas(modifier=Modifier) {
        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawBitmap(imageBitmap.asAndroidBitmap(), 0f, 0f, null)
        }
    }
}