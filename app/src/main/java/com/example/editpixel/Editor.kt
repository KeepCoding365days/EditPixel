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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class Editor : AppCompatActivity() {
    private var ImgBitmap=BitmapObject.bitmap
    private var project_name=BitmapObject.project_name
    private var file_name=BitmapObject.file_name
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
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
fun AdjustBrightness(source: Bitmap, brightness: Float): Bitmap {
        val isMutable = source.isMutable

        // Create a mutable copy of the bitmap if it is not mutable
        val mutableBitmap = if (isMutable) source else source.copy(Bitmap.Config.ARGB_8888, true)

        val canvas = Canvas(mutableBitmap)
        val paint = Paint()

        // Create a color matrix. This matrix will be used to adjust the saturation
        val colorMatrix = ColorMatrix().apply {
            setScale(brightness,brightness,brightness,1f)
        }
        // Set the paint to use this color matrix
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)

        // Draw the original bitmap onto the canvas using the paint with saturation adjustment
        canvas.drawBitmap(mutableBitmap, 0f, 0f, paint)

        return mutableBitmap
    }
    fun AdjustContrast(source: Bitmap, contrast: Float): Bitmap {
        val isMutable = source.isMutable

        // Create a mutable copy of the bitmap if it is not mutable
        val mutableBitmap = if (isMutable) source else source.copy(Bitmap.Config.ARGB_8888, true)

        val canvas = Canvas(mutableBitmap)
        val paint = Paint()

        // Create a color matrix. This matrix will be used to adjust the saturation
        val colorMatrix = ColorMatrix().apply {
            val scale = contrast + 1f
            val translate = -(128 * contrast) / 2
            set(floatArrayOf(
                scale, 0f, 0f, 0f, translate,
                0f, scale, 0f, 0f, translate,
                0f, 0f, scale, 0f, translate,
                0f, 0f, 0f, 1f, 0f
            ))
        }
        // Set the paint to use this color matrix
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)

        // Draw the original bitmap onto the canvas using the paint with saturation adjustment
        canvas.drawBitmap(mutableBitmap, 0f, 0f, paint)

        return mutableBitmap
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
        // Assuming the value ranges from -100f (cool) to 100f (warm)
        // Adjust the scaling to suit your specific needs. This is a basic implementation.
        val adjustment = value / 100f

        // Adjust Red, Green, Blue channels - a rudimentary approximation
        // The higher the temperature value, the warmer the color
        val r = if (adjustment > 0) 1f + adjustment else 1f
        val g = 1f
        val b = if (adjustment < 0) 1f - adjustment else 1f

        set(floatArrayOf(
            r, 0f, 0f, 0f, 0f,
            0f, g, 0f, 0f, 0f,
            0f, 0f, b, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        ))
    }



    fun BacktoProject(){
        val i = Intent(applicationContext, ProjectGallery::class.java)
        i.putExtra("project_name", project_name)
        startActivity(i)
        finish()
    }

    fun CallFilters(){
        //update it
        //val i = Intent(applicationContext, ProjectGallery::class.java)
        //startActivity(i)
        //finish()
    }
    fun CallBg(){
        //update it
        //val i = Intent(applicationContext, ProjectGallery::class.java)
        //startActivity(i)
        //finish()
    }
    fun CallFg(){
        //update it
        //val i = Intent(applicationContext, ProjectGallery::class.java)
        //startActivity(i)
        //finish()
    }
    fun CallCrop(){
        //update it
        //val i = Intent(applicationContext, ProjectGallery::class.java)
        //startActivity(i)
        //finish()
    }
    fun CallAdvance(){
        //update it
        //val i = Intent(applicationContext, ProjectGallery::class.java)
        //startActivity(i)
        //finish()
    }
    suspend fun saveToProject(composer_bitmap:Bitmap){
        val helper=StorageHelper()
        helper.AddtoProject(
            project_name,
            applicationContext,
            composer_bitmap
        )
    }
    suspend fun saveImage(composer_bitmap: Bitmap){
        val helper=StorageHelper()
        helper.SaveImage(
            project_name = project_name,
            file_name,
            applicationContext,
            composer_bitmap
        )
    }

    @Composable
    fun EditorUI(bitmap:Bitmap) {
        var composer_bitmap by remember {
            mutableStateOf(bitmap)
        }
        var selectedButton by remember { mutableStateOf("") }
        var saveButton by remember {
            mutableStateOf(false)
        }
        var cancelBtn by remember {
            mutableStateOf(false)
        }

        Surface(modifier = Modifier.fillMaxSize(),
            color = Color.Black) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                //verticalArrangement = Arrangement.,
                //modifier = Modifier.fillMaxSize()
            ) {

                /* top icons row */

                /*Icon(imageVector = Icons.Filled.Done,contentDescription= "",tint=Color.Green,
                    modifier = Modifier.clickable (onClick={
                        saveButton=true
                    }
                    )
                )*/
                Row(
                    modifier = Modifier.padding(vertical = 5.dp)
                ) {
                    TextButton(onClick = {cancelBtn=true}, modifier = Modifier.weight(1f).
                        background(color=Color.Gray).padding(2.dp)){
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription ="",tint=Color.White
                            )
                    }

                    TextButton(onClick = {saveButton=true}, modifier = Modifier.weight(1f).
                        background(color=Color.Black).padding(2.dp)){
                        Icon(imageVector = Icons.Filled.Done, contentDescription ="", tint = Color.White

                             )
                    }
                }
                if(cancelBtn) {
                    AlertDialog(onDismissRequest = { cancelBtn=false },
                        title={
                              Text("Are you sure to Leave editing")
                        },
                        confirmButton = {
                        TextButton(onClick = {BacktoProject()  }

                        ) {
                            Text("Leave")
                        }
                    },
                        dismissButton = {
                            TextButton(onClick = {cancelBtn=false}
                            ) {
                                Text("Cancel")
                            }
                        }

                    )
                }
                if(saveButton){
                    Dialog(onDismissRequest =
                    { saveButton=false }){
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(16.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                horizontalAlignment=Alignment.CenterHorizontally
                            ) {
                                Text(text="How would you like to save your image?",
                                    modifier=Modifier.padding(6.dp).weight(1f), textAlign = TextAlign.Center)
                                Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.weight(1f)) {
                                    TextButton(onClick = {
                                        BitmapObject.bitmap=composer_bitmap
                                        ImgBitmap=BitmapObject.bitmap
                                        saveButton=false
                                        val helper = StorageHelper()
                                        CoroutineScope(Dispatchers.Main).launch{
                                            saveImage(composer_bitmap)
                                        }
                                    }, modifier = Modifier.weight(1f)) {
                                        Text("Save")
                                    }
                                    TextButton(onClick = {
                                        composer_bitmap=ImgBitmap
                                        saveButton=false
                                        CoroutineScope(Dispatchers.Main).launch {
                                            saveToProject(composer_bitmap)
                                        }

                                    },modifier=Modifier.weight(1f)) {
                                        Text("Save a Copy")
                                    }
                                }
                            }
                        }
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

                var brightness by remember { mutableStateOf(0f) }
                var contrast by remember { mutableStateOf(1f) }


                var colorTemperature by remember { mutableStateOf(0f) }
                fun reset_values(){
                    saturation=0f
                    hue=0f
                    brightness=0f
                    contrast=1f
                    colorTemperature=0f
                }

                Row(
                    //horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .padding(2.dp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    BarButton("Saturation", R.drawable.hue, selectedButton == "Saturation") {
                        reset_values()
                        composer_bitmap=ImgBitmap
                        selectedButton = "Saturation"
                    }
                    BarButton(
                        "Temprature",
                        R.drawable.temp,
                        selectedButton == "Colors"
                    ) {
                        reset_values()
                        composer_bitmap=ImgBitmap
                        selectedButton = "Colors"
                    }
                    BarButton("Hue", R.drawable.hue, selectedButton == "Hue") {
                        reset_values()
                        composer_bitmap=ImgBitmap
                        selectedButton = "Hue"
                    }
                    BarButton("Brightness", R.drawable.brightness, selectedButton == "brightness") {
                        reset_values()
                        composer_bitmap=ImgBitmap
                        selectedButton = "brightness"
                    }
                    BarButton("Contrast", R.drawable.contrast, selectedButton == "contrast") {
                        reset_values()
                        composer_bitmap=ImgBitmap
                        selectedButton = "contrast"
                    }
                    BarButton("Filter", R.drawable.filters, selectedButton == "Filter") {
                        selectedButton = "Filter"
                    }
                    BarButton("Crop", R.drawable.crop, selectedButton == "Crop") {
                        selectedButton = "Crop"
                    }
                    BarButton(
                        "ForeGround",
                        R.drawable.fg,
                        selectedButton == "Foreground"
                    ) {
                        selectedButton = "Foreground"
                    }
                    BarButton(
                        "Background",
                        R.drawable.bg,
                        selectedButton == "Background"
                    ) {
                        selectedButton = "Background"
                    }
                    BarButton(
                        "Advanced",
                        R.drawable.advanced,
                        selectedButton == "Advanced"
                    ) {
                        composer_bitmap=ImgBitmap
                        selectedButton = "Advanced"
                    }
                }
                if (selectedButton=="Background"){
                    CallBg()
                }
                else if(selectedButton=="Foreground"){
                    CallFg()
                }
                else if(selectedButton=="Filter"){
                    CallFilters()
                }
                else if(selectedButton=="Crop"){
                    CallCrop()
                }
                else if(selectedButton=="Advanced"){
                    CallAdvance()
                }
                else if (selectedButton == "Colors") { // Assuming "Color Palette" is the text for the button
                    //composer_bitmap=ImgBitmap
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Slider(
                            value = colorTemperature,
                            onValueChange = { newValue ->
                                colorTemperature = newValue},
                            onValueChangeFinished={
                                composer_bitmap =
                                    changeBitmapTemperature(ImgBitmap, colorTemperature)
                            },
                            valueRange = -100f..100f // Adjust the range as needed for your temperature scale
                        )
                        Text(text = String.format("%.2f", colorTemperature),color=Color.LightGray)
                    }
                }

                else if (selectedButton == "Saturation") {
                    //composer_bitmap=ImgBitmap
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Slider(value = saturation, onValueChange = { value ->
                            saturation = value
                        }, onValueChangeFinished = {composer_bitmap=changeBitmapSaturation(ImgBitmap,saturation)
                                }, valueRange = 0f..100f)
                        Text(text=String.format("%.2f",saturation),color=Color.LightGray)
                    }
                } else if (selectedButton == "Hue") {
                    //composer_bitmap=ImgBitmap
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Slider(value = hue, onValueChange = { value -> hue = value },
                            onValueChangeFinished = {
                                composer_bitmap=adjustHue(ImgBitmap,hue)
                            },
                            valueRange = 0f..360f, modifier = Modifier)
                        Text(text=String.format("%.2f",hue),color=Color.LightGray)
                    }
                }
                else if(selectedButton=="brightness"){
                    //composer_bitmap=ImgBitmap
                    Column(horizontalAlignment = Alignment.CenterHorizontally){
                        Slider(value = brightness, onValueChange = { value -> brightness = value },
                            onValueChangeFinished = {
                                composer_bitmap=AdjustBrightness (ImgBitmap,brightness)
                            },
                            valueRange = -255f..255f,modifier = Modifier)

                        Text(text=String.format("%.2f",brightness),modifier=Modifier,color=Color.LightGray)
                    }
                }
                else if(selectedButton=="contrast"){
                    //composer_bitmap=ImgBitmap
                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Slider(value = contrast, onValueChange = { value -> contrast = value },
                            onValueChangeFinished = {
                                composer_bitmap=AdjustContrast (ImgBitmap,contrast)
                            },
                            valueRange = 0f..10f, modifier = Modifier)

                        Text(text=String.format("%.2f",contrast),modifier=Modifier,color=Color.LightGray)
                    }
                }
            }
        }
    }

    @Composable
    fun BarButton(text: String, iconRes: Int, isSelected: Boolean, onClick: () -> Unit) {
        Column( horizontalAlignment= Alignment.CenterHorizontally) {
            Button(

                onClick = onClick,
                modifier = Modifier.padding(horizontal = 2.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = if (isSelected) Color.LightGray else Color.White
                )
            ) {
                Column() {
                    Image(
                        painter = painterResource(iconRes),
                        contentDescription = text,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(text)
                }
            }
        }
    }

    @Preview
    @Composable
    fun PreviewEditorUI() {
        EditorUI(ImgBitmap)
    }
}
