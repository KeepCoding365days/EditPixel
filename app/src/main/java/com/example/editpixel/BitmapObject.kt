package com.example.editpixel

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource

object BitmapObject {

    var bitmap: Bitmap= createBitmap()
    private fun createBitmap(): Bitmap {
        // Assume these dimensions and config for example purposes
        return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    }
}