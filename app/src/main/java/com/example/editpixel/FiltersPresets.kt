package com.example.editpixel

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun noFilter(originalBitmap: Bitmap): ImageBitmap{
    return originalBitmap.asImageBitmap()
}

fun autoFilter(originalBitmap: Bitmap): ImageBitmap{

    val editable = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)

    val factor = analyzeImage(editable)

    for (x in 0 until editable.width) {
        for (y in 0 until editable.height) {
            val pixel = editable.getPixel(x, y)
            val alpha = Color.alpha(pixel)
            val red = Color.red(pixel)
            val green = Color.green(pixel)
            val blue = Color.blue(pixel)

            val hsv = FloatArray(3)
            Color.RGBToHSV(red, green, blue, hsv)
            hsv[1] *= (factor[4])
            val newColor = Color.HSVToColor(hsv)

            val newRed = Color.red(newColor) * factor[0]
            val newGreen = Color.green(newColor) * factor[0]
            val newBlue = Color.blue(newColor) * factor[0]

            val newPixel = Color.argb(alpha, (newRed * factor[1]).toInt(), (newGreen * factor[2]).toInt(), (newBlue * factor[3]).toInt())

            editable.setPixel(x, y, newPixel)
        }
    }
    return editable.asImageBitmap()
}

fun invertFilter(originalBitmap: Bitmap): ImageBitmap {

    val editable = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
    for (x in 0 until editable.width) {
        for (y in 0 until editable.height) {
            val pixel = editable.getPixel(x, y)
            val alpha = Color.alpha(pixel)
            val red = Color.red(pixel)
            val green = Color.green(pixel)
            val blue = Color.blue(pixel)
            val newPixel = Color.argb(alpha, 255 - red, 255 - green, 255 - blue)
            editable.setPixel(x, y, newPixel)
        }
    }
    return editable.asImageBitmap()
}

fun greyscaleFilter(originalBitmap: Bitmap): ImageBitmap {

    val editable = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)

    for (x in 0 until editable.width) {
        for (y in 0 until editable.height) {
            val pixel = editable.getPixel(x, y)
            val alpha = Color.alpha(pixel)
            val red = Color.red(pixel)
            val green = Color.green(pixel)
            val blue = Color.blue(pixel)
            val grey = (0.2989 * red + 0.5870 * green + 0.1140 * blue).toInt() //standard of greyscale conversion
            val newPixel = Color.argb(alpha, grey, grey, grey)
            editable.setPixel(x, y, newPixel)
        }
    }
    return editable.asImageBitmap()
}

fun parisFilter(originalBitmap: Bitmap): ImageBitmap {

    val editable = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)

    for (x in 0 until editable.width) {
        for (y in 0 until editable.height) {
            val pixel = editable.getPixel(x, y)
            val red = Color.red(pixel)
            val green = Color.green(pixel)
            val blue = minOf((Color.blue(pixel) * 1.2).toInt(),255)

            val hsv = FloatArray(3)
            Color.RGBToHSV(red,green,blue,hsv)
            hsv[1] = hsv[1] * 0.8f
            val newPixel = Color.HSVToColor(hsv)
            editable.setPixel(x, y, newPixel)
        }
    }
    return editable.asImageBitmap()
}

fun egyptFilter(originalBitmap: Bitmap): ImageBitmap {

    val editable = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)

    for (x in 0 until editable.width) {
        for (y in 0 until editable.height) {
            val pixel = editable.getPixel(x, y)
            val red = minOf((Color.red(pixel) * 1.2).toInt(),255)
            val green = Color.green(pixel)
            val blue = minOf((Color.blue(pixel) * 1.2).toInt(),255)

            val hsv = FloatArray(3)
            Color.RGBToHSV(red,green,blue,hsv)
            hsv[1] = hsv[1] * 0.8f
            val newPixel = Color.HSVToColor(hsv)
            editable.setPixel(x, y, newPixel)
        }
    }
    return editable.asImageBitmap()
}

fun vintageFilter(originalBitmap: Bitmap): ImageBitmap {

    val editable = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)

    for (x in 0 until editable.width) {
        for (y in 0 until editable.height) {
            val pixel = editable.getPixel(x, y)
            val alpha = Color.alpha(pixel)
            val red = Color.red(pixel)
            val green = Color.green(pixel)
            val blue = Color.blue(pixel)
            val newred = minOf((red * 1.5).toInt(),255)
            val newblue = (blue * 0.7).toInt()


            val newPixel = Color.argb(alpha, newred, green, newblue)
            editable.setPixel(x, y, newPixel)
        }
    }
    return editable.asImageBitmap()
}
