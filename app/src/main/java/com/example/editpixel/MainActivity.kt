package com.example.editpixel

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.editpixel.ui.theme.EditPixelTheme
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview


class MainActivity : ComponentActivity() {
    private val imagePaths = mutableStateListOf<Uri>()

    companion object {
        const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        } else {
            setContent {
                EditPixelTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainContent()
                    }
                }
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setContent {
                        EditPixelTheme {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                MainContent()
                            }
                            }
                        }
                    }
                else {
                    // Permission denied. Handle the functionality that depends on this permission.
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun ExtractBitmap(path: String?):Bitmap {
        val bitmap = BitmapFactory.decodeFile(path)
        return bitmap
    }


    @Composable
    fun MainContent() {
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
            imagePaths.clear()
            imagePaths.addAll(uris)
        }

        Column {
            Button(onClick = { launcher.launch(arrayOf("image/*")) }) {
                Text("Select Images")
            }
            Button(onClick = {if(imagePaths.isNotEmpty())
            { {

            }
            }}) {
                Text("Start Editing")
            }
    }
}
    @Composable
    fun sendBitmap(path: String){
        val bitmap= ExtractBitmap(path.toString())
        val i = Intent(applicationContext,EditingLandingPage::class.java)
        DrawBitmap(bitmap)
        //i.putExtra("Bitmap",bitmap)
        //startActivity(i)
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
    @Composable
    fun ImageGallery(imageUris: List<Uri>) {
        Column {
            for (uri in imageUris) {
                CoilImage(uri = uri)
            }
        }
    }

    @Composable
    fun CoilImage(uri: Uri, modifier: Modifier = Modifier) {
        val painter = rememberImagePainter(data = uri, builder = {
            crossfade(true)
        })
        Image(
            painter = painter,
            contentDescription = null,
            modifier = modifier.size(200.dp) // Adjust size as needed
        )
    }
}
