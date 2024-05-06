package com.example.editpixel

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.editpixel.ui.theme.EditPixelTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ProjectGallery : AppCompatActivity() {
    private var project_name:String=""
    private var file_name=""
    private var file_uri:Uri= Uri.EMPTY
    companion object {
        private const val TAG = "ProjectGallery"
        private const val MY_PERMISSIONS_REQUEST_CAMERA = 101
        private const val REQUEST_IMAGE_CAPTURE = 102
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        project_name= intent.getStringExtra("project_name").toString()
        Log.d(TAG,"project_name"+project_name)
        setContent(){
            EditPixelTheme {
                Surface(
                    modifier = Modifier,
                    color= Color.DarkGray
                ) {
                   Gallery(project_name)
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
    fun check_permission_cam() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
        }
    }


    fun saveImageToExternalStorage(context: Context, bitmap: Bitmap, filename: String) {
        Log.d(TAG,"Start of save")
        val obj=StorageHelper();
        obj.AddtoProject(project_name,applicationContext,bitmap,"PNG");
    }
    fun ExtractBitmap(uri: Uri):Bitmap{
        val source= ImageDecoder.createSource(this.contentResolver,uri)
        val bitmap= ImageDecoder.decodeBitmap(source)
        return bitmap
    }
    suspend fun savetoApp(list:List<Uri>){
        Log.d(TAG,"save function called")
        for( path in  list){
            Log.d(TAG,"Inside loop")
            val bitmap:Bitmap=ExtractBitmap(path)
            Log.d(TAG,"saveImagecalled")
            saveImageToExternalStorage(applicationContext,bitmap,path.toString());
        }
    }
    suspend fun savetoApp(uri: Uri){
        val bitmap=ExtractBitmap(uri)
        saveImageToExternalStorage(applicationContext,bitmap,"cam")
    }
    fun ProjectPage(){
        val i=Intent(applicationContext,ProjectList::class.java)
        startActivity(i)
        finish()
    }
    fun deletfile(file_name:String){
        val helper = StorageHelper()
        helper.deleteFile(
            applicationContext,
            project_name,
            file_name
        )
    }

    suspend fun ExportFile(uri:Uri){
        val helper = StorageHelper()
        Log.d(TAG,"helper called. with uri: "+uri.toString())
        helper.exportFileToGallery(applicationContext, uri)
        Log.d(TAG,"helper returned")
    }



    @Composable
    fun Gallery(name: String?){

        val imagePaths = remember {
            mutableStateListOf<Uri>()
        }
        val dir=getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val temp_uri=FileProvider.getUriForFile(applicationContext,"com.example.editpixel.provider",
            File.createTempFile("temp_image",".jpg",dir))

        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
            imagePaths.clear()
            imagePaths.addAll(uris)
            CoroutineScope(Dispatchers.Main).launch {
                savetoApp(imagePaths)
                setContent(){
                    EditPixelTheme {
                        Surface(
                            modifier = Modifier,
                            color= Color.DarkGray
                        ) {
                            Gallery(project_name)
                        }
                    }
                }
            }

        }
        val launcher_cam = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                CoroutineScope(Dispatchers.Main).launch {
                    savetoApp(temp_uri)
                    setContent(){
                        EditPixelTheme {
                            Surface(
                                modifier = Modifier,
                                color= Color.DarkGray
                            ) {
                                Gallery(project_name)
                            }
                        }
                    }
                }
            }
        }

        var temp="a"

        var delBtn by remember {
            mutableStateOf(false)
        }
        var ExportBtn by remember {
            mutableStateOf(false)
        }
        if(delBtn) {
            AlertDialog(onDismissRequest = { delBtn=false },
                title={
                    Text("Are you sure to delete this file")
                },
                confirmButton = {
                    TextButton(onClick = {deletfile(file_name)
                        delBtn=false
                    }

                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {delBtn=false}
                    ) {
                        Text("Cancel")
                    }
                }

            )
        }
        if(ExportBtn) {
            AlertDialog(onDismissRequest = { ExportBtn=false },
                title={
                    Text("Are you sure to Export this file to Gallery?")
                },
                confirmButton = {
                    TextButton(onClick = {
                        CoroutineScope(Dispatchers.Main).launch {
                            ExportFile(file_uri)
                        }
                        ExportBtn=false
                    }

                    ) {
                        Text("Export")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {ExportBtn=false}
                    ) {
                        Text("Cancel")
                    }
                }

            )
        }


        if(name!=null){
            temp=name
        }
            Column(
                modifier = Modifier.fillMaxSize().background(color = Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    temp, modifier = Modifier.padding(5.dp), color = Color.White,
                    fontFamily = FontFamily.Cursive, style = MaterialTheme.typography.displayMedium
                )

                LazyColumn(modifier = Modifier.padding(10.dp),horizontalAlignment = Alignment.CenterHorizontally) {
                    val obj = StorageHelper()
                    val uris = obj.ExtractProjectUri(temp, applicationContext)
                    items(uris) { uri ->
                        //CoroutineScope(Dispatchers.Main).launch {
                        val bitmap = ExtractBitmap(uri)
                    OutlinedCard(modifier = Modifier.padding(5.dp).background(color = Color.Black)) {
                        Column(modifier = Modifier
                            .fillMaxWidth().background(color = Color.DarkGray),
                            horizontalAlignment = Alignment.CenterHorizontally ) {
                            Image(
                                painter = BitmapPainter(bitmap.asImageBitmap()),
                                contentDescription = "images",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clickable(onClick = {
                                        BitmapObject.bitmap = bitmap
                                        BitmapObject.project_name = project_name
                                        BitmapObject.file_name = uri.lastPathSegment ?: ""
                                        val i = Intent(applicationContext, Editor::class.java)
                                        startActivity(i)
                                        finish()
                                    })
                                    .clip(RoundedCornerShape(16.dp))
                                    .widthIn(max = LocalConfiguration.current.screenWidthDp.dp - 30.dp)
                                    .heightIn(max = LocalConfiguration.current.screenHeightDp.dp - 100.dp)

                            )
                            Row(
                                modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp - 30.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                TextButton(onClick = {
                                    file_name = uri.lastPathSegment ?: ""
                                    delBtn = true
                                    setContent() {
                                        EditPixelTheme {
                                            Surface(
                                                modifier = Modifier,
                                                color = Color.DarkGray
                                            ) {
                                                Gallery(project_name)
                                            }
                                        }
                                    }
                                }, modifier = Modifier) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "",
                                        tint = Color.White,
                                    )
                                }
                                TextButton(
                                    onClick = {
                                        file_uri = uri
                                        Log.d(TAG, "file uri is:" + file_uri)
                                        ExportBtn = true
                                    },
                                    modifier = Modifier
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Send,
                                        contentDescription = "",
                                        tint = Color.White,
                                    )
                                }
                            }
                        }
                    }
                    }
                    (item {
                        Button(onClick = { ProjectPage() }, modifier = Modifier) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack, contentDescription = "",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    })
                }

            }



        SmallFloatingActionButton(
            onClick = { check_permission()
                launcher.launch(arrayOf("image/*"))

                      },
            modifier = Modifier
                .padding(16.dp)
                .offset(
                    LocalConfiguration.current.screenWidthDp.dp - 70.dp,
                    LocalConfiguration.current.screenHeightDp.dp - 100.dp
                )
        ) {
            Icon(Icons.Filled.Add, "Fap_Add")
        }
        SmallFloatingActionButton(
            onClick = { check_permission_cam()
                launcher_cam.launch(temp_uri)
            },
            modifier = Modifier
                .padding(16.dp)
                .offset(
                    0.dp,
                    LocalConfiguration.current.screenHeightDp.dp - 100.dp
                )
        ) {
            Icon(painter = painterResource(R.drawable.camera) , "Fap_Cam")
        }

    }
}
