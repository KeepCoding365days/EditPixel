package com.example.editpixel
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.editpixel.R
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.ui.res.painterResource
import com.example.editpixel.Editor
import com.example.editpixel.ui.theme.EditPixelTheme


var selectedColorIndex by mutableStateOf(0)
class Foreground : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EditPixelTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MyAppContent(context = this)
                }
            }
        }
    }
    fun goBack(){
        val i= Intent(applicationContext,Editor::class.java)
        startActivity(i)
        finish()
    }
}
//u can access image by bitmap=BitmapObject.bitmap
//call goBack function for returning to Editor activity


@Composable
fun MyAppContent(context: Context) {
    var selectedDrawable by remember { mutableStateOf(R.drawable.images) }
    var selectedColorIndex by remember { mutableStateOf(0) }
    var currentBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var undoStack = remember { mutableListOf<Bitmap?>() }
    var redoStack = remember { mutableListOf<Bitmap?>() }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(1.dp)) {
        ForegroundObjectPicker(selectedDrawable, context) { drawable ->
            selectedDrawable = drawable
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons Row directly below the image
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { currentBitmap = loadImage(context) }) {
                Text("Load Image")
            }
            Button(onClick = {
                if (undoStack.isNotEmpty()) {
                    redoStack.add(0, currentBitmap)
                    currentBitmap = undoStack.removeFirst()
                }
            }) {
                Text("Undo")
            }
            Button(onClick = {
                if (redoStack.isNotEmpty()) {
                    undoStack.add(0, currentBitmap)
                    currentBitmap = redoStack.removeFirst()
                }
            }) {
                Text("Redo")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Color picker UI components
        ColorPicker(selectedColorIndex) { index ->
            selectedColorIndex = index
        }
    }
}

fun loadImage(context: Context): Bitmap? {
    return BitmapFactory.decodeResource(context.resources, android.R.drawable.ic_menu_gallery)

}


@Composable
fun ForegroundObjectPicker(selectedDrawable: Int, context: Context, onSelectionChange: (Int) -> Unit) {
    var detectedEdgesBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Column {
        ImageWithForeground(selectedDrawable, context) { drawable ->
            onSelectionChange(drawable)

            detectedEdgesBitmap = detectEdgesOfObject(context, selectedDrawable, selectedColorIndex)
        }

        // Display only the detected edges
        detectedEdgesBitmap?.let { edgesBitmap ->
            androidx.compose.foundation.Image(
                bitmap = edgesBitmap.asImageBitmap(),
                contentDescription = "Detected Edges",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Adjust height as needed
            )
        }
    }
}

@Composable
fun ImageWithForeground(drawableId: Int, context: Context, onObjectClicked: (Int) -> Unit) {
    val imageBitmap = BitmapFactory.decodeResource(context.resources, drawableId).asImageBitmap()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(700.dp) // Adjust the height as needed
            .clickable { onObjectClicked(drawableId) } // Handle click to detect object
    ) {
        androidx.compose.foundation.Image(
            bitmap = imageBitmap,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

fun detectEdgesOfObject(context: Context, drawableId: Int, selectedColorIndex: Int): Bitmap? {
    // Load the selected image from resources
    val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)
    val imageBitmap = bitmap.asImageBitmap()

    // Convert ImageBitmap to OpenCV Mat
    val mat = Mat()
    Utils.bitmapToMat(imageBitmap.asAndroidBitmap(), mat)

    // Convert image to grayscale
    val grayMat = Mat()
    Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY)

    // Threshold the grayscale image to segment the object based on color
    val thresholdedMat = Mat()
    Imgproc.threshold(grayMat, thresholdedMat, 0.0, 255.0, Imgproc.THRESH_BINARY or Imgproc.THRESH_OTSU)

    // Detect edges of the segmented object
    val edgesMat = Mat()
    Imgproc.Canny(thresholdedMat, edgesMat, 50.0, 150.0)

    // Display the extracted object with its edges
    val resultBitmap = Bitmap.createBitmap(edgesMat.cols(), edgesMat.rows(), Bitmap.Config.ARGB_8888)
    Utils.matToBitmap(edgesMat, resultBitmap)

    return applyColorToBitmap(resultBitmap, selectedColorIndex)
}

fun applyColorToBitmap(bitmap: Bitmap, colorIndex: Int): Bitmap {
    val color = when (colorIndex) {
        0 -> android.graphics.Color.RED
        1 -> android.graphics.Color.GREEN
        else -> android.graphics.Color.BLUE
    }

    val width = bitmap.width
    val height = bitmap.height
    val coloredBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = bitmap.getPixel(x, y)
            if (pixel == -1) { // If the pixel is white (edges)
                coloredBitmap.setPixel(x, y, color)
            } else {
                coloredBitmap.setPixel(x, y, pixel)
            }
        }
    }

    return coloredBitmap
}

enum class Color {
    RED, GREEN, BLUE
}
@Composable
fun ColorPicker(selectedColorIndex: Int, onColorSelected: (Int) -> Unit) {
    val colors = listOf(
        Triple("Red", Color.Red, selectedColorIndex == 0),
        Triple("Green", Color.Green, selectedColorIndex == 1),
        Triple("Blue", Color.Blue, selectedColorIndex == 2),
        Triple("Yellow", Color.Yellow, selectedColorIndex == 3),
        Triple("Orange", Color(0xFFF57C00), selectedColorIndex == 4),
        Triple("Purple", Color(0xFF6200EA), selectedColorIndex == 5),
        Triple("Pink", Color(0xFFC2185B), selectedColorIndex == 6),
        Triple("Teal", Color(0xFF009688), selectedColorIndex == 7),
        Triple("Cyan", Color.Cyan, selectedColorIndex == 8),
        Triple("Lime", Color(0xFFCDDC39), selectedColorIndex == 9),
        Triple("Amber", Color(0xFFFFC107), selectedColorIndex == 10),
        Triple("Grey", Color.Gray, selectedColorIndex == 11),
        Triple("Black", Color.Black, selectedColorIndex == 12),
        Triple("White", Color.White, selectedColorIndex == 13)
    )

    LazyRow(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        items(colors) { (label, color, isSelected) ->
            ColorOption(color, isSelected) {
                onColorSelected(colors.indexOfFirst { it.first == label })
            }
        }
    }
}

@Composable
fun ColorOption(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) Color.Black else Color.Transparent  // Changed border color for visibility on lighter colors
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(56.dp)
            .border(BorderStroke(2.dp, borderColor), CircleShape)
            .background(color, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = if (color == Color.White) Color.Black else Color.White  // Ensure the check icon is visible on white
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val context = LocalContext.current
    EditPixelTheme{
        MyAppContent(context)
    }
}

