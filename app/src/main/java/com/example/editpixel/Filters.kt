package com.example.editpixel

import androidx.compose.runtime.mutableStateOf
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.ActivityCompat.recreate

@Composable
fun Filters(bitmap: BitmapObject, callback: () -> Unit){

    var fnum by remember { mutableStateOf(0) }

    val image = bitmap.bitmap.asImageBitmap()
    val d = LocalDensity.current.density.toInt()
    val thumb = Bitmap.createScaledBitmap(image.asAndroidBitmap(),120*d,120*d, false).asImageBitmap()

    val result = if (fnum != 0) {
        applyFilter(image.asAndroidBitmap(), fnum)
    } else {
        image
    }
    Column(modifier = Modifier.fillMaxSize().background(Color.hsv(243f, 0.54f, 0.23f)),verticalArrangement = Arrangement.Bottom) {
        Image(
            bitmap = result,
            contentDescription = "some description",
            modifier = Modifier.fillMaxWidth().weight(1f).padding(top=20.dp,end=5.dp,start=5.dp,bottom=0.dp)
        )
        FilterButtons(thumb){newFnum -> fnum = newFnum}

        Row{
            Button(
                shape = RoundedCornerShape(0),
                onClick = { BitmapObject.bitmap = result.asAndroidBitmap(); fnum = 0; callback() },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(contentColor = Color.White, containerColor = Color.hsv(213f, 0.66f, 0.78f))
            ) {
                Text("Save")
            }
            Button(
                shape = RoundedCornerShape(0),
                onClick = { fnum = 0; callback() },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(contentColor = Color.White, containerColor = Color.DarkGray)
            ) {
                Text("Cancel")
            }
        }
    }
}

@Composable
fun FilterButtons(bg : ImageBitmap, update : (Int) -> (Unit)) {
    val scrollState = rememberScrollState()

    Row(modifier = Modifier
        .horizontalScroll(scrollState)
        .fillMaxWidth()
        ,verticalAlignment = Alignment.CenterVertically)
    {
        repeat(7) { index ->
            val newbg = applyFilter(bg.asAndroidBitmap() , index)
            FilterButton(text = "Button", bg = newbg, i = index , { num -> update(num) })
        }
    }
}

@Composable
fun FilterButton(text: String, bg: ImageBitmap, i : Int, update: (Int) -> Unit){
    Box(Modifier.fillMaxWidth().padding(start = 2.dp, end = 2.dp, top = 4.dp, bottom = 4.dp).clickable { update(i) }){
        Image(
            bitmap = bg,
            contentDescription = null,
            modifier = Modifier.height(120.dp).width(120.dp).clip(RoundedCornerShape(5.dp))
        )
        Text(
            text=text,
            color=Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center))
    }
}

fun save(){
    Log.d("Decode","save")
}

fun cancel(){
    Log.d("Decode","cancel")
}

fun applyFilter(bitmap: Bitmap, fnum : Int) : ImageBitmap{

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

    return updated
}


