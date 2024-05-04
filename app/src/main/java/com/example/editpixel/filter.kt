package com.example.editpixel

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.editpixel.ui.theme.EditPixelTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class filter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContent(){
            EditPixelTheme {
                Filters(bitmap = BitmapObject,::CloseFilters)

            }
        }
    }
    fun CloseFilters(){
        //set filters flag to true to display filter module
        val i= Intent(applicationContext,Editor::class.java)
        startActivity(i)
        finish()
    }
    @Composable
    fun Filters(bitmap: BitmapObject, callback: () -> Unit) {

        var fnum by remember { mutableStateOf(0) }

        val d = LocalDensity.current.density.toInt()

        val original = bitmap.bitmap.asImageBitmap()
        val aspectRatio = original.width.toFloat() / original.height.toFloat()

        val newWidth = 120 * d
        val newHeight = (newWidth / aspectRatio).toInt()

        val preview = if (newWidth >= original.width.toFloat()) {
            bitmap.bitmap.asImageBitmap()
        } else {
            Bitmap.createScaledBitmap(original.asAndroidBitmap(), newWidth, newHeight, false)
                .asImageBitmap()
        }
        val thumb = Bitmap.createScaledBitmap(preview.asAndroidBitmap(), 30 * d, 30 * d, false)
            .asImageBitmap()


        Log.d("Decode", fnum.toString())

        var isLoading by remember { mutableStateOf(false) }
        var imagepreview = applyFilter(preview.asAndroidBitmap(), fnum)

        fun applyFilterToOriginal() {
            isLoading = true
            CoroutineScope(Dispatchers.Default).launch {
                BitmapObject.bitmap =
                    applyFilter(original.asAndroidBitmap(), fnum).asAndroidBitmap()
                // Update the UI on the main thread
                withContext(Dispatchers.Main) {
                    callback()
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.hsv(0f, 0f, 0f)), verticalArrangement = Arrangement.Bottom
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
//                    Button(
//                        shape = RoundedCornerShape(0),
//                        onClick = { callback() },
//                        modifier = Modifier
//                            .height(50.dp)
//                            .padding(2.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            contentColor = Color.White,
//                            containerColor = Color.Black
//                        )
//
//                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.ArrowBack,
//                            contentDescription = "",
//                            tint = Color.White
//                        )
//                    }
                }
            }
            Row {
                Button(
                    shape = RoundedCornerShape(0),
                    onClick = { fnum = 0; callback() },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .padding(2.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color.Gray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
                Button(
                    shape = RoundedCornerShape(0),
                    onClick = { applyFilterToOriginal() },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .padding(2.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color.Black
                    )

                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }

            Image(
                bitmap = imagepreview,
                contentDescription = "some description",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 20.dp, end = 5.dp, start = 5.dp, bottom = 0.dp)
            )
            FilterButtons(thumb) { newFnum -> fnum = newFnum }
        }
    }
    @Composable
    fun FilterButtons(bg : ImageBitmap, update : (Int) -> (Unit)) {
        val names = arrayOf("Original", "Auto", "GreyScale", "Paris", "Invert", "Egypt", "Vintage")
        val scrollState = rememberScrollState()

        Row(modifier = Modifier
            .horizontalScroll(scrollState)
            .fillMaxWidth()
            ,verticalAlignment = Alignment.CenterVertically)
        {
            repeat(7) { index ->
                val newbg = applyFilter(bg.asAndroidBitmap() , index)
                var name = names.getOrNull(index)
                if (name == null){
                    name = "Undefined"
                }
                FilterButton(text = name, bg = newbg, i = index , { num -> update(num) })
            }
        }
    }

    @Composable
    fun FilterButton(text: String, bg: ImageBitmap, i : Int, update: (Int) -> Unit){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = 2.dp, end = 2.dp, top = 4.dp, bottom = 4.dp)
                .clickable { update(i) }
                .clip(RoundedCornerShape(5.dp))
        ){
            Text(
                text=text,
                color= Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Image(
                bitmap = bg,
                contentDescription = null,
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
            )
        }
    }
    fun applyFilter(bitmap: Bitmap, fnum : Int) : ImageBitmap {

        Log.d("Decode","Applying Filter")
        Log.d("Decode",fnum.toString())
        val updated = if (fnum == 0){
            noFilter(bitmap)
        }
        else if(fnum == 1){
            autoFilter(bitmap)
        }
        else if(fnum == 2){
            greyscaleFilter(bitmap)
        }
        else if(fnum == 3){
            parisFilter(bitmap)
        }
        else if(fnum == 4){
            invertFilter(bitmap)
        }
        else if(fnum == 5){
            egyptFilter(bitmap)
        }
        else{
            vintageFilter(bitmap)
        }
        Log.d("Decode","Done Applying")
        return updated
    }
}