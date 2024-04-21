package com.example.editpixel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.editpixel.ui.theme.EditPixelTheme
import android.content.ContextWrapper
import android.graphics.Paint
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

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
    @Composable
    fun Editor(bitmap: Bitmap){
        Column() {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            {
                OptionCard(id = R.drawable.filters, text ="Filter" )
                OptionCard(id = R.drawable.crop, text ="Crop" )
                OptionCard(id = R.drawable.fg, text ="ForeGround" )
                OptionCard(id = R.drawable.brush, text = "Advanced")

            }
            Image(
                bitmap = bitmap.asImageBitmap(), contentDescription = "Image", modifier = Modifier
                    .size(400.dp)
                    .padding(6.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OptionCard(id = R.drawable.hue, text ="Hue" )
                OptionCard(id = R.drawable.drop, text ="Saturation" )
                OptionCard(id = R.drawable.fgg, text ="Color" )
                OptionCard(id = R.drawable.bg, text = "BackGround")
            }
        }
    }
    @Composable
    fun OptionCard(id:Int, text:String){

        Card(

        ) {
            Column (
                horizontalAlignment= Alignment.CenterHorizontally
            ){
                Image(painter = painterResource(id = id), contentDescription = null,Modifier.size(30.dp))
                Text(text = text)
            }

        }
    }
    @Preview
    @Composable
    fun preview(){
        Editor(bitmap = BitmapObject.bitmap)
    }
}
