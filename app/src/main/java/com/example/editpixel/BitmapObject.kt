package com.example.editpixel

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource

object BitmapObject {

    var bitmap: Bitmap= createBitmap()
    var project_name:String=""
    var file_name:String=""

    //flag to display filters module
    private fun createBitmap(): Bitmap {
        // Assume these dimensions and config for example purposes
        return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    }
}