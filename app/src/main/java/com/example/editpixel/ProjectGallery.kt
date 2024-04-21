package com.example.editpixel

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.editpixel.ui.theme.EditPixelTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

class ProjectGallery : AppCompatActivity() {
    private val imagePaths = mutableStateListOf<Uri>()
    lateinit var name:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name= intent.getStringExtra("project_name").toString()
        Log.d(TAG,"project_name"+name)
        setContent(){
            EditPixelTheme {
                Surface(
                    modifier = Modifier,
                    color= Color.Black
                ) {
                   Gallery(name)
                }
            }
        }
    }

    fun check_permission() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                MainActivity.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        }

    }
    fun saveImageToExternalStorage(context: Context, bitmap: Bitmap, filename: String) {
        Log.d(TAG,"Start of save")
        val obj=StorageHelper();
        obj.AddtoProject(name,applicationContext,bitmap);
    }
    fun ExtractBitmap(uri: Uri):Bitmap{
        val source= ImageDecoder.createSource(this.contentResolver,uri)
        val bitmap= ImageDecoder.decodeBitmap(source)
        return bitmap
    }
    fun savetoApp(list:List<Uri>){
        Log.d(TAG,"save function called")
        for( path in  list){
            Log.d(TAG,"Inside loop")
            val bitmap:Bitmap=ExtractBitmap(path)
            val i= Intent(applicationContext,EditingLandingPage::class.java)
            //i.putExtra("bitmap",bitmap)

            Log.d(TAG,"saveImagecalled")
            saveImageToExternalStorage(applicationContext,bitmap,path.toString());
        }
    }
    @Composable
    fun Gallery(name: String?){
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
            imagePaths.clear()
            imagePaths.addAll(uris)
            savetoApp(uris)

        }
        var temp="a"

        if(name!=null){
            temp=name
        }
        Column (
            modifier=Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                temp, modifier = Modifier.padding(5.dp), color = Color.White,
                style = MaterialTheme.typography.displayMedium
            )
        }
        LazyRow(modifier = Modifier.padding(10.dp)){
            val obj=StorageHelper()
            val uris=obj.ExtractProjectUri(temp,applicationContext)
            items(uris){ uri->
                val bitmap=ExtractBitmap(uri)
                Image(bitmap=bitmap.asImageBitmap() , contentDescription = "images",Modifier.clickable(onClick = {
                    BitmapObject.bitmap=bitmap
                    val i= Intent(applicationContext,Editor::class.java)
                    startActivity(i)
                    finish()
                }))
            }

        }


        SmallFloatingActionButton(
            onClick = { check_permission()
                launcher.launch(arrayOf("image/*"))
                Log.d(TAG,"calling save function")
                //savetoApp();
                      },
            modifier = Modifier
                .padding(16.dp)
                .offset(300.dp, 600.dp)
        ) {
            Icon(Icons.Filled.Add, "Fap_Add")
        }
    }
}
