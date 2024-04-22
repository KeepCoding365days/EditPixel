package com.example.editpixel

import android.graphics.Bitmap
import android.graphics.Color

fun analyzeImage(image: Bitmap): FloatArray {
    var totalIntensity = 0.0f
    var maxIntensity = 0.0f
    var totalRed = 0.0f
    var totalGreen = 0.0f
    var totalBlue = 0.0f

    var totalSaturation = 0.0f

    val pixelCount = image.width * image.height

    for (x in 0 until image.width) {
        for (y in 0 until image.height) {
            val pixel = image.getPixel(x, y)

            val red = Color.red(pixel)
            val green = Color.green(pixel)
            val blue = Color.blue(pixel)
            totalRed += red
            totalGreen += green
            totalBlue += blue

            val hsv = FloatArray(3)
            Color.RGBToHSV(Color.red(pixel), Color.green(pixel), Color.blue(pixel), hsv)
            totalSaturation += hsv[1]

            val intensity = (red + green + blue)/ 3f
            maxIntensity = maxOf(maxIntensity,intensity)
            totalIntensity += intensity
        }
    }

    val brightnessFactor = 1 - ((totalIntensity / pixelCount)/ (maxIntensity) )

    var redFactor = (totalRed/pixelCount)
    var greenFactor = (totalGreen/pixelCount)
    var blueFactor = (totalBlue/pixelCount)
    val maxColor = maxOf(redFactor, greenFactor, blueFactor)
    redFactor /= maxColor
    greenFactor /= maxColor
    blueFactor /= maxColor

    val saturationFactor = 1 - (totalSaturation/pixelCount)

    return  floatArrayOf(brightnessFactor, redFactor, greenFactor, blueFactor, saturationFactor)
}