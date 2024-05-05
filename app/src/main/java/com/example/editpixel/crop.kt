package com.example.editpixel

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.editpixel.ui.theme.EditPixelTheme
import com.example.editpixel.PolygonCropActivity
import okio.IOException
import java.io.File
import java.io.FileOutputStream

object BitmapHolder {
    var originalBitmap: Bitmap? = null
}
class crop :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EditPixelTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(applicationContext)
                }
            }
        }
        if (BitmapHolder.originalBitmap == null) {
            BitmapHolder.originalBitmap = BitmapObject.bitmap.copy(BitmapObject.bitmap.config, false)
        }
    }
    fun BacktoEditor(){
        //BitmapObject.bitmap=ur bitmap
        val i= Intent(applicationContext,Editor::class.java)
        startActivity(i)
        finish()
    }



    @Composable
    fun HomeScreen(context: Context) {
        val originalBitmap = BitmapHolder.originalBitmap
        val context = LocalContext.current
      //  val originalBitmap by remember { mutableStateOf<Bitmap>(BitmapObject.bitmap.copy(BitmapObject.bitmap.config, false)) }
        var bitmap by remember { mutableStateOf<Bitmap>(BitmapObject.bitmap) }
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                imageUri = result.uriContent
            }
            else {
                val exception = result.error
            }
        }

        val polygonCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                imageUri = result.uriContent
            } else {
                val exception = result.error
            }
        }

        if (imageUri != null) {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            }
            else {
                val source = ImageDecoder.createSource(context.contentResolver, imageUri!!)
                bitmap = ImageDecoder.decodeBitmap(source)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight(0.1f)
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { val i=Intent(context,Editor::class.java)
                    startActivity(i)
                    finish()
                }) {
                    Text("Back")
                }

                Button(onClick = {
                    bitmap = originalBitmap ?: throw IllegalStateException("Original bitmap is null")
                    imageUri = bitmapToUri(context, bitmap)
                }) {
                    Text("Undo")
                }

                Button(onClick = {
                    BitmapObject.bitmap= bitmap
                    saveBitmapToFile(context, bitmap)
                }) {
                    Text("Save")
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .background(Color.DarkGray),
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap?.asImageBitmap()!!,
                        contentDescription = null,
                        // contentScale = ContentScale.Fit,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxHeight(0.1f)
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    val uri = bitmapToUri(context, bitmap)
                    val cropOption = CropImageContractOptions(uri, CropImageOptions())
                    imageCropLauncher.launch(cropOption)
                }) {
                    Text("Simple Crop")
                }

                Button(onClick = {
                    val intent = Intent(context, PolygonCropActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Text("Polygon Crop")
                }

                Button(onClick = {
                    val intent = Intent(context, StickerApplier::class.java)
                    context.startActivity(intent)
                }) {
                    Text("Sticker")
                }
            }
        }
    }

    private fun saveBitmapToFile(context: Context, bitmap: Bitmap?) {
        bitmap?.let { bmp ->
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "cropped_image_${System.currentTimeMillis()}.webp")
                put(MediaStore.Images.Media.MIME_TYPE, "image/webp")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/Stickers")
                }
            }

            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri == null) {
                Log.e("SaveImage", "Failed to create new MediaStore record.")
                return
            }

            try {
                context.contentResolver.openOutputStream(uri).use { outputStream ->
                    if (outputStream == null) {
                        Log.e("SaveImage", "Failed to get output stream.")
                        return
                    }
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                        Log.e("SaveImage", "Failed to save bitmap.")
                        return
                    }
                    outputStream?.flush()
                    outputStream?.close()
                }

                // Broadcasting to make the image available in the gallery immediately
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                mediaScanIntent.data = uri
                context.sendBroadcast(mediaScanIntent)
            } catch (e: Exception) {
                Log.e("SaveImage", "Exception in saving image", e)
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(context, "Bitmap is null", Toast.LENGTH_SHORT).show()
        }
    }

}

fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
    var uri: Uri? = null
    try {
        // Create a file in the cache directory
        val file = File(context.cacheDir, "tempFile.png")
        val outStream = FileOutputStream(file)

        // Compress and write the bitmap to the file
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)

        // Flush and close the output stream
        outStream.flush()
        outStream.close()

        // Get the Uri from the file
        uri = Uri.fromFile(file)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return uri
}