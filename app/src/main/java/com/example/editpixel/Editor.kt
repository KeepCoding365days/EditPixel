package com.example.editpixel
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.editpixel.BitmapObject
import com.example.editpixel.R
import com.example.editpixel.ui.theme.EditPixelTheme
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.Dialog

class Editor : AppCompatActivity() {
    private var ImgBitmap=BitmapObject.bitmap
    private var project_name=BitmapObject.project_name
    private var file_name=BitmapObject.file_name
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {

                    EditorUI(ImgBitmap)
                }


    }
    fun adjustHue(source: Bitmap, hue: Float): Bitmap {
        val isMutable = source.isMutable

        // Create a mutable copy of the bitmap if it is not mutable
        val newBitmap = if (isMutable) source else source.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(newBitmap)
        val paint = Paint()

        // Create a color matrix and rotate it to adjust hue
        val colorMatrix = ColorMatrix()
        colorMatrix.setRotate(0, hue) // Red channel
        colorMatrix.setRotate(1, hue) // Green channel
        colorMatrix.setRotate(2, hue) // Blue channel

        // Set the paint to use this color matrix
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)

        // Draw the bitmap onto the canvas using the paint
        canvas.drawBitmap(newBitmap, 0f, 0f, paint)

        return newBitmap
    }
    fun changeBitmapSaturation(source: Bitmap, saturation: Float): Bitmap {
        val isMutable = source.isMutable

        // Create a mutable copy of the bitmap if it is not mutable
        val mutableBitmap = if (isMutable) source else source.copy(Bitmap.Config.ARGB_8888, true)

        val canvas = Canvas(mutableBitmap)
        val paint = Paint()

        // Create a color matrix. This matrix will be used to adjust the saturation
        val colorMatrix = ColorMatrix()

        // Set the saturation
        colorMatrix.setSaturation(saturation)

        // Set the paint to use this color matrix
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)

        // Draw the original bitmap onto the canvas using the paint with saturation adjustment
        canvas.drawBitmap(mutableBitmap, 0f, 0f, paint)

        return mutableBitmap
    }

    @Composable 
    fun saveOptions(){
        AlertDialog(onDismissRequest = { /*TODO*/ }, confirmButton = { /*TODO*/ })
    }

    @Composable
    fun EditorUI(bitmap:Bitmap) {
        var composer_bitmap by remember {
            mutableStateOf(bitmap)
        }
        Surface(modifier = Modifier.fillMaxSize()) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {

                /* top icons row */
                var selectedButton by remember { mutableStateOf("") }
                var saveButton by remember {
                    mutableStateOf(false)
                }
                Icon(imageVector = Icons.Filled.Done,contentDescription= "",tint=Color.Green,
                    modifier = Modifier.clickable (onClick={
                        saveButton=true
                    }
                    )
                )
                if(saveButton){
                    Dialog(onDismissRequest =
                    { saveButton=false }){
                        
                        Column(){
                            Text(text=R.string.saveOptions.toString())
                            Button(onClick = { val helper=StorageHelper()
                                helper.SaveImage(project_name = project_name,file_name,applicationContext,composer_bitmap)
                            }) {
                                Text("Save")
                            }
                            Button(onClick = { val helper=StorageHelper()
                                BitmapObject.file_name=helper.AddtoProject(project_name,applicationContext,composer_bitmap)

                            }) {
                                Text("Save a copy")
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    BarButton("filter", R.drawable.filters, selectedButton == "filter") {
                        selectedButton = "filter"
                    }
                    BarButton("Crop", R.drawable.crop, selectedButton == "Crop") {
                        selectedButton = "Crop"
                    }
                    BarButton(
                        "foreground and background",
                        R.drawable.fg,
                        selectedButton == "foreground and background"
                    ) {
                        selectedButton = "foreground and background"
                    }
                    BarButton(
                        "bring to front",
                        R.drawable.fgg,
                        selectedButton == "bring to front"
                    ) {
                        selectedButton = "bring to front"
                    }
                }
                /* center selected image */
                Image(
                    bitmap = composer_bitmap.asImageBitmap()  ,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                /* bottom icons row*/

                var saturation by remember { mutableStateOf(0f) }
                var hue by remember { mutableStateOf(0f) }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    BarButton("Saturation", R.drawable.hue, selectedButton == "Saturation") {
                        selectedButton = "Saturation"
                    }
                    BarButton(
                        "Color Palette",
                        R.drawable.brush,
                        selectedButton == "Color Palette"
                    ) {
                        selectedButton = "Color Palette"
                    }
                    BarButton("Hue", R.drawable.drop, selectedButton == "Hue") {
                        selectedButton = "Hue"
                    }
                    BarButton("Brush", R.drawable.brush, selectedButton == "Brush") {
                        selectedButton = "Brush"
                    }
                }


                // Show sliders based on selected button
                if (selectedButton == "Saturation") {
                    Column() {
                        Slider(value = saturation, onValueChange = { value ->
                            saturation = value
                        }, onValueChangeFinished = {composer_bitmap=changeBitmapSaturation(ImgBitmap,saturation)
                                }, valueRange = 0f..100f)
                        Text(text=saturation.toString())
                    }
                } else if (selectedButton == "Hue") {
                    Column() {
                        Slider(value = hue, onValueChange = { value -> hue = value },
                            onValueChangeFinished = {
                                composer_bitmap=adjustHue(ImgBitmap,hue)
                            },
                            valueRange = 0f..360f)
                        Text(text=hue.toString())
                    }
                }
            }
        }
    }

    @Composable
    fun BarButton(text: String, iconRes: Int, isSelected: Boolean, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            modifier = Modifier.padding(horizontal = 2.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent
            )
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = text,
                modifier = Modifier.size(40.dp)
            )
        }
    }

    @Preview
    @Composable
    fun PreviewEditorUI() {
        EditorUI(ImgBitmap)
    }
}
