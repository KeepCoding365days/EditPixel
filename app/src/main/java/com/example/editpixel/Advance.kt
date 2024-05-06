package com.example.editpixel

import android.R.attr.x
import android.R.attr.y
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.editpixel.ui.theme.EditPixelTheme
import kotlin.math.abs
import kotlin.math.sqrt

import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalConfiguration


class Advance : AppCompatActivity() {

    @Composable
    fun rememberContentCopy(): ImageVector {
        return remember {
            ImageVector.Builder(
                name = "content_copy",
                defaultWidth = 40.0.dp,
                defaultHeight = 40.0.dp,
                viewportWidth = 40.0f,
                viewportHeight = 40.0f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(13.292f, 30.958f)
                    quadToRelative(-1.084f, 0f, -1.875f, -0.77f)
                    quadToRelative(-0.792f, -0.771f, -0.792f, -1.855f)
                    verticalLineToRelative(-22f)
                    quadToRelative(0f, -1.083f, 0.792f, -1.854f)
                    quadToRelative(0.791f, -0.771f, 1.875f, -0.771f)
                    horizontalLineToRelative(17.083f)
                    quadToRelative(1.083f, 0f, 1.854f, 0.771f)
                    quadTo(33f, 5.25f, 33f, 6.333f)
                    verticalLineToRelative(22f)
                    quadToRelative(0f, 1.084f, -0.771f, 1.855f)
                    quadToRelative(-0.771f, 0.77f, -1.854f, 0.77f)
                    close()
                    moveToRelative(0f, -2.625f)
                    horizontalLineToRelative(17.083f)
                    verticalLineToRelative(-22f)
                    horizontalLineTo(13.292f)
                    verticalLineToRelative(22f)
                    close()
                    moveTo(8f, 36.25f)
                    quadToRelative(-1.083f, 0f, -1.854f, -0.771f)
                    quadToRelative(-0.771f, -0.771f, -0.771f, -1.854f)
                    verticalLineTo(10.792f)
                    quadToRelative(0f, -0.542f, 0.375f, -0.938f)
                    quadToRelative(0.375f, -0.396f, 0.917f, -0.396f)
                    quadToRelative(0.583f, 0f, 0.958f, 0.396f)
                    reflectiveQuadToRelative(0.375f, 0.938f)
                    verticalLineToRelative(22.833f)
                    horizontalLineToRelative(17.625f)
                    quadToRelative(0.5f, 0f, 0.896f, 0.375f)
                    reflectiveQuadToRelative(0.396f, 0.917f)
                    quadToRelative(0f, 0.583f, -0.396f, 0.958f)
                    reflectiveQuadToRelative(-0.896f, 0.375f)
                    close()
                    moveToRelative(5.292f, -29.917f)
                    verticalLineToRelative(22f)
                    verticalLineToRelative(-22f)
                    close()
                }
            }.build()
        }
    }

    private fun path(
        fill: SolidColor,
        fillAlpha: Float,
        stroke: Nothing?,
        strokeAlpha: Float,
        strokeLineWidth: Float,
        strokeLineCap: StrokeCap,
        strokeLineJoin: Any,
        strokeLineMiter: Float,
        pathFillType: Any,
        function: () -> Unit
    ) {

    }
    @Composable
    fun rememberBrushIcon(): ImageVector {
        return remember {
            ImageVector.Builder(
                name = "brush",
                defaultWidth = 40.0.dp,
                defaultHeight = 40.0.dp,
                viewportWidth = 40.0f,
                viewportHeight = 40.0f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(12.0f, 32.0f)
                    lineTo(12.0f, 28.0f)
                    lineTo(16.0f, 28.0f)
                    lineTo(16.0f, 32.0f)
                    lineTo(12.0f, 32.0f)
                    close()
                    moveTo(16.0f, 24.0f)
                    lineTo(20.0f, 24.0f)
                    lineTo(20.0f, 28.0f)
                    lineTo(16.0f, 28.0f)
                    lineTo(16.0f, 24.0f)
                    close()
                    moveTo(20.0f, 16.0f)
                    lineTo(24.0f, 16.0f)
                    lineTo(24.0f, 24.0f)
                    lineTo(20.0f, 24.0f)
                    lineTo(20.0f, 16.0f)
                    close()
                    moveTo(24.0f, 8.0f)
                    lineTo(28.0f, 8.0f)
                    lineTo(28.0f, 16.0f)
                    lineTo(24.0f, 16.0f)
                    lineTo(24.0f, 8.0f)
                    close()
                }
            }.build()
        }
    }

    @Composable
    fun rememberArrowSelectorTool(): ImageVector {
        return remember {
            ImageVector.Builder(
                name = "arrow_selector_tool",
                defaultWidth = 40.0.dp,
                defaultHeight = 40.0.dp,
                viewportWidth = 40.0f,
                viewportHeight = 40.0f
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    fillAlpha = 1f,
                    stroke = null,
                    strokeAlpha = 1f,
                    strokeLineWidth = 1.0f,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero
                ) {
                    moveTo(12.708f, 24.875f)
                    lineToRelative(4.209f, -5.917f)
                    horizontalLineTo(25.5f)
                    lineTo(12.708f, 8.875f)
                    close()
                    moveToRelative(12.5f, 10.667f)
                    quadToRelative(-0.833f, 0.416f, -1.646f, 0.104f)
                    quadToRelative(-0.812f, -0.313f, -1.229f, -1.146f)
                    lineToRelative(-5.208f, -11.208f)
                    lineToRelative(-4.667f, 6.458f)
                    quadToRelative(-0.583f, 0.792f, -1.479f, 0.5f)
                    quadToRelative(-0.896f, -0.292f, -0.896f, -1.25f)
                    verticalLineTo(6.208f)
                    quadToRelative(0f, -0.833f, 0.75f, -1.187f)
                    quadToRelative(0.75f, -0.354f, 1.375f, 0.146f)
                    lineToRelative(17.959f, 14.041f)
                    quadToRelative(0.708f, 0.625f, 0.395f, 1.5f)
                    quadToRelative(-0.312f, 0.875f, -1.27f, 0.875f)
                    horizontalLineToRelative(-8.209f)
                    lineToRelative(5.125f, 11.125f)
                    quadToRelative(0.417f, 0.834f, 0.104f, 1.646f)
                    quadToRelative(-0.312f, 0.813f, -1.104f, 1.188f)
                    close()
                    moveToRelative(-8.291f, -16.584f)
                    close()
                }
            }.build()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EditPixelTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UI()
                }
            }
        }
    }

    fun onSavedBack(ImgBitmap: Bitmap) {
        //if u wanna save
        BitmapObject.bitmap = ImgBitmap
        val i = Intent(applicationContext, Editor::class.java)
        startActivity(i)
        finish()

    }

    fun onBack() {

        val i = Intent(applicationContext, Editor::class.java)
        startActivity(i)
        finish()

    }


    sealed class EditAction {
        data class AddLine(val line: Line) : EditAction()
        data class RemoveLine(val line: Line) : EditAction()
    }

    data class Line(
        val start: Offset,
        val end: Offset,
        val color: Color = Color.Black,
        val strokeWidth: Dp = 1.dp
    )

    @Composable
    fun SimpleColorPicker(
        selectedColor: MutableState<Color>,
        colors: List<Color>,
        modifier: Modifier = Modifier
    ) {
        Row(modifier = modifier) {
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                        .background(color)
                        .clickable {
                            selectedColor.value = color
                        }
                        .then(
                            if (selectedColor.value == color) Modifier.border(
                                2.dp,
                                Color.Black
                            ) else Modifier
                        )
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BrushSettingsDialog(
        onDismiss: () -> Unit,
        onApply: (Color, Float) -> Unit
    ) {
        var selectedWidth by remember { mutableStateOf(4f) }

        // Define your list of colors
        val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta)

        val selectedColorState = remember { mutableStateOf(Color.Black) }
        val selectedColor = selectedColorState.value

        AlertDialog(
            onDismissRequest = onDismiss,

            {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Title
                    Text("Brush Settings")

                    Spacer(modifier = Modifier.height(16.dp))

                    // Color picker
                    Text("Select Color")
                    Spacer(modifier = Modifier.height(8.dp))
                    SimpleColorPicker(selectedColor = selectedColorState, colors = colors)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Width slider
                    Text("Select Width")
                    Spacer(modifier = Modifier.height(8.dp))
                    Slider(
                        value = selectedWidth,
                        onValueChange = { selectedWidth = it },
                        valueRange = 1f..10f
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                onApply(selectedColor, selectedWidth)
                                onDismiss()
                            }
                        ) {
                            Text("Apply")
                        }
                    }
                }
            }

        )
    }


    private fun Offset.distanceTo(other: Offset): Float {
        val dx = other.x - x
        val dy = other.y - y
        return sqrt((dx * dx) + (dy * dy))
    }

    fun isInsideCanvas(offset: Offset, canvasWidth: Int, canvasHeight: Int): Boolean {
        return offset.x >= 0 && offset.x <= canvasWidth && offset.y >= 0 && offset.y <= canvasHeight
    }

    @Preview
    @Composable
    fun UI() {
        // State to manage the visibility of the dialog
        var showDialog by remember { mutableStateOf(false) }
        var cancelDialogVisible by remember { mutableStateOf(false) }
        var saveDialogVisible by remember { mutableStateOf(false) }
        val selectedColor = remember { mutableStateOf(Color.Black) }
        val ColorWidth = remember { mutableStateOf(4f) }
        var isEraserActive by remember { mutableStateOf(false) }
        var isBrushActive by remember { mutableStateOf(false) }
        var isCloneActive by remember { mutableStateOf(false) }
        var isSelectActive by remember { mutableStateOf(false) }
        var isSelected by remember { mutableStateOf(false) }
        var isSelecting by remember { mutableStateOf(false) }
        var CloneBrush by remember { mutableStateOf(false) }
        val lines = remember {
            mutableStateListOf<Line>()
        }
        var startOffset by remember { mutableStateOf(Offset.Zero) }

        var endOffset by remember { mutableStateOf(Offset.Zero) }
        val image = BitmapObject.bitmap
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp.dp
        val screenHeightDp = configuration.screenHeightDp.dp-200.dp

        // Calculate the scaling factors for width and height
        val widthScaleFactor = screenWidthDp.value / image.width
        val heightScaleFactor = screenHeightDp.value / image.height

        // Choose the smaller scaling factor to maintain aspect ratio
        val scaleFactor = if (widthScaleFactor < heightScaleFactor) widthScaleFactor else heightScaleFactor

        // Calculate the target width and height
        val targetWidth = (image.width * scaleFactor).toInt()
        val targetHeight = (image.height * scaleFactor).toInt()

        // Resize the bitmap
        val imageBitmap = Bitmap.createScaledBitmap(image, targetWidth*2, targetHeight*2, true)
        val density = LocalDensity.current.density
        val hdp = imageBitmap.height.dp/2
        val wdp = imageBitmap.width.dp/2
        val h = (hdp * density).value.toInt()

        val w = (wdp * density).value.toInt()
        val isMutable = imageBitmap.isMutable
        val newBitmap =
            if (isMutable) imageBitmap else imageBitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(newBitmap)
        val undoHistory by remember { mutableStateOf<MutableList<EditAction>>(mutableListOf()) }
        val redoHistory by remember { mutableStateOf<MutableList<EditAction>>(mutableListOf()) }
        var size = Size.Zero
        var topLeft = Offset.Zero

        var clonedImage: Bitmap? = null
        val newConfig = Bitmap.Config.RGB_565 // Example configuration

        val newBit: Bitmap = imageBitmap.copy(newConfig, true)
        canvas.drawBitmap(newBit, 0f, 0f, null)
        val undo = {
            if (undoHistory.isNotEmpty()) {
                val undoAction = undoHistory.removeLast()
                when (undoAction) {
                    is EditAction.AddLine -> {
                        lines.remove(undoAction.line)
                        redoHistory.add(undoAction)
                    }

                    is EditAction.RemoveLine -> {
                        lines.add(undoAction.line)
                        redoHistory.add(undoAction)
                    }
                }
            }
        }

        val redo = {
            if (redoHistory.isNotEmpty()) {
                val redoAction = redoHistory.removeLast()
                when (redoAction) {
                    is EditAction.AddLine -> {
                        lines.add(redoAction.line)
                        undoHistory.add(redoAction)
                    }

                    is EditAction.RemoveLine -> {
                        lines.remove(redoAction.line)
                        undoHistory.add(redoAction)
                    }
                }
            }
        }





        Column(
            modifier=Modifier.wrapContentHeight(Alignment.CenterVertically)
        ) {

            Box(
                modifier = Modifier
                    .border(1.dp, Color.White).align(Alignment.CenterHorizontally)
            ) {
                val context = LocalContext.current
                Canvas(
                    modifier = Modifier
                        .width(wdp)
                        .height(hdp)
                        .pointerInput(true) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                val line = Line(
                                    start = change.position - dragAmount,
                                    end = change.position,
                                    color = selectedColor.value,
                                    strokeWidth = ColorWidth.value.dp,
                                )
                                if (!isSelecting && isSelectActive) {
                                    startOffset = change.position - dragAmount
                                    isSelecting = true
                                }
                                if (isCloneActive || CloneBrush) {
                                    startOffset = change.position - dragAmount
                                    isSelected = true
                                }

                                endOffset = change.position

                                if (isBrushActive) {
                                    if (isInsideCanvas(
                                            line.start,
                                            w,
                                            h
                                        ) &&
                                        isInsideCanvas(
                                            line.end,
                                            w,
                                            h
                                        )
                                    ) {
                                        // Store the complete line as a single action
                                        undoHistory.add(EditAction.AddLine(line))
                                        lines.add(line)


                                    }


                                } else if (isEraserActive) {
                                    // If the eraser is active, check if any existing lines are within the eraser touch area and remove them
                                    val touchPosition = change.position
                                    lines.removeAll {
                                        it.start.distanceTo(touchPosition) < 20 || it.end.distanceTo(
                                            touchPosition
                                        ) < 20
                                    }
                                    undoHistory.add(EditAction.RemoveLine(line))
                                }
                            }
                        }
                ) {


                    if (isCloneActive) {

                        if (clonedImage != null) {
                            canvas.drawBitmap(newBit, 0f, 0f, null)
                        }
                    }

                    drawImage(
                        image = newBitmap.asImageBitmap(),
                        topLeft = Offset.Zero
                    )

                    if (isSelecting && isSelectActive) {
                        topLeft = Offset(
                            x = minOf(startOffset.x, endOffset.x),
                            y = minOf(startOffset.y, endOffset.y)
                        )
                        size = Size(
                            width = abs(startOffset.x - endOffset.x),
                            height = abs(startOffset.y - endOffset.y)
                        )
                        drawRect(
                            color = Color.Red,
                            style = Stroke(2.dp.toPx()),
                            topLeft = topLeft,
                            size = size
                        )
                        val rect = Rect().apply {
                            left = topLeft.x.toInt()
                            top = topLeft.y.toInt()
                            right = (topLeft.x + size.width).toInt()
                            bottom = (topLeft.y + size.height).toInt()
                        }
                        val selectedBitmap = Bitmap.createBitmap(
                            imageBitmap,
                            rect.left,
                            rect.top,
                            rect.width(),
                            rect.height()
                        )
                        clonedImage = selectedBitmap


                    }

                    if (isSelected && (isCloneActive || CloneBrush)) {


                        clonedImage?.let { image ->
                            val newbitmap: Bitmap = image.copy(newConfig, true)
                            val offset =
                                Offset(startOffset.x + image.width, startOffset.y + image.height)
                            if (isInsideCanvas(startOffset, w, h) && isInsideCanvas(offset, w, h)) {
                                drawImage(image.asImageBitmap(), topLeft = startOffset)
                                canvas.drawBitmap(newbitmap, startOffset.x, startOffset.y, null)
//                            canvas.drawBitmap(image, startOffset.x, startOffset.y, null)
                            }
//}
                        }





//                        clonedImage?.let { image ->
//                            val offset =
//                                Offset(startOffset.x + image.width, startOffset.y + image.height)
//
//                            println("we did it")
//                            if (isInsideCanvas(startOffset, w, h) && isInsideCanvas(offset, w, h)) {
//
//                            }
//
//                        }
                        println("The Offset is: $startOffset")


                    }
                    if (clonedImage == null) {
                        println("NICEEE!!")
                    } else {
                        println("NICE")
                    }


                    // Extract the portion of the original image inside the rectangle

                    // Assign the extracted bitmap to the clonedImage variable

                    lines.forEach { line ->
                        drawLine(
                            color = line.color,
                            start = line.start,
                            end = line.end,
                            strokeWidth = line.strokeWidth.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
//                    lines.forEach { line ->
//                        val paint = Paint().apply {
//                            color = line.color.toArgb() // Set the color of the lines
//
//                            val width = line.strokeWidth.value * density
//                            strokeWidth = width // Set the stroke width of the lines
//                        }
//                        canvas.drawLine(
//                            line.start.x,
//                            line.start.y,
//                            line.end.x,
//                            line.end.y,
//                            paint
//                        )
//                    }


                }


            }
        }
        Column (verticalArrangement = Arrangement.Bottom){

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                //horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(20.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                IconButton(
                    onClick = {

                        showDialog = true
                        isBrushActive = true
                        isEraserActive = false
                        isCloneActive = false
                        isSelectActive = false
                        CloneBrush=false
                    },
                    modifier = Modifier.border(1.dp, Color.White)
                ) {
                    BrushIcon(selectedColor, ColorWidth.value)
                }
                Spacer(modifier = Modifier.width(16.dp)) // Add space between icons
                IconButton(
                    onClick = {
                        canvas.drawBitmap(newBit, 0f, 0f, null)

                        isEraserActive = true
                        isBrushActive = false
                        showDialog = false
                        isCloneActive = false
                        isSelectActive = false
                        CloneBrush=false
                    },
                    modifier = Modifier.border(1.dp, Color.White)
                ) {
                    EraserIcon()
                }
                Spacer(modifier = Modifier.width(16.dp)) // Add space between icons
                IconButton(
                    onClick = {
                        undo()
                        isEraserActive = false
                        isBrushActive = false
                        showDialog = false
                        isCloneActive = false
                        isSelectActive = false
                        CloneBrush=false
                    },
                    modifier = Modifier.border(1.dp, Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Undo Icon",
                        tint = Color.White,
                        modifier = Modifier
                            .size(50.dp)
                            .rotate(180f)
                    )
                }



                Spacer(modifier = Modifier.width(16.dp)) // Add space between icons
                IconButton(
                    onClick = {
                        isEraserActive = false
                        isBrushActive = false
                        showDialog = false
                        isCloneActive = false
                        isSelectActive = false
                        CloneBrush=false
                        redo()
                    },
                    modifier = Modifier.border(1.dp, Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Redo Icon",
                        tint = Color.White,
                        modifier = Modifier.size(100.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp)) // Add space between icons
                IconButton(
                    onClick = {
                        lines.clear()
                        canvas.drawBitmap(newBit, 0f, 0f, null)
                        undoHistory.clear()
                        redoHistory.clear()
                        isSelecting = false
                        isEraserActive = false
                        isBrushActive = false
                        showDialog = false
                        isCloneActive = false
                        isSelectActive = true
                        CloneBrush=false
                    },
                    modifier = Modifier.border(1.dp, Color.White)
                )
                //selection icon
                {
                    Icon(
                        imageVector = rememberArrowSelectorTool(),
                        contentDescription = "Save Icon",
                        tint = Color.White,
                        modifier = Modifier.size(100.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp)) // Add space between icons
                IconButton(
                    onClick = {
                        lines.clear()
                        val newConfig = Bitmap.Config.RGB_565 // Example configuration

                        val newBit: Bitmap = imageBitmap.copy(newConfig, true)
                        canvas.drawBitmap(newBit, 0f, 0f, null)
                        redoHistory.clear()
                        undoHistory.clear()
                        isEraserActive = false
                        isBrushActive = false
                        showDialog = false
                        isCloneActive = true
                        isSelectActive = false
                        isSelected = false
                        CloneBrush=false
                    },
                    modifier = Modifier.border(1.dp, Color.White)
                )
                //clone icon
                {
                    Icon(
                        imageVector = rememberContentCopy(),
                        contentDescription = "back Icon",
                        tint = Color.White,
                        modifier = Modifier.size(100.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(
                    onClick = {
                        isEraserActive = false
                        isBrushActive = false
                        showDialog = false
                        isCloneActive = false
                        isSelectActive = false
                        cancelDialogVisible = false
                        CloneBrush=true
                    },
                    modifier = Modifier.border(1.dp, Color.White)
                )
                //modifier = Modifier.weight(0.5f).
                //background(color=Color.Gray).padding(2.dp))
                {
                    Icon(
                        imageVector =  rememberBrushIcon(),
                        contentDescription = "brushcloning",
                        tint = Color.White,
                        modifier = Modifier.size(100.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp)) // Add space between icons
                IconButton(
                    onClick = {
                        lines.forEach { line ->
                            val paint = Paint().apply {
                                color = line.color.toArgb() // Set the color of the lines

                                val width = line.strokeWidth.value * density
                                strokeWidth = width // Set the stroke width of the lines
                            }
                            canvas.drawLine(
                                line.start.x,
                                line.start.y,
                                line.end.x,
                                line.end.y,
                                paint
                            )
                        }

                        onSavedBack(newBitmap)


//                        saveDialogVisible = true
                        isEraserActive = false
                        isBrushActive = false
                        showDialog = false
                        isCloneActive=false
                        isSelectActive=false
                        CloneBrush=false
                    },
                    modifier = Modifier.border(1.dp, Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Save Icon",
                        tint = Color.White,
                        modifier = Modifier.size(100.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp)) // Add space between icons
                IconButton(
                    onClick = {
                        isEraserActive = false
                        isBrushActive = false
                        showDialog = false
                        isCloneActive = false
                        isSelectActive = false
                        cancelDialogVisible = false
                        CloneBrush=false
                        onBack()
                    },
                    modifier = Modifier.border(1.dp, Color.White)
                )
                //modifier = Modifier.weight(0.5f).
                //background(color=Color.Gray).padding(2.dp))
                {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "back Icon",
                        tint = Color.White,
                        modifier = Modifier.size(100.dp)
                    )
                }


            }
//            Row {
//
//
//            }
//            Spacer(modifier = Modifier.height(20.dp)) // Add space between icons
//            Row {
//
//
//
//            }


            // Show dialog when showDialog is true
            if (showDialog) {
                BrushSettingsDialog(
                    onDismiss = { showDialog = false },
                    onApply = { color, width ->
                        selectedColor.value = color
                        ColorWidth.value = width
                    }
                )
            }

            if (cancelDialogVisible) {

                AlertDialog(
                    onDismissRequest = { cancelDialogVisible = false },
                    title = {
                        Text(text = "Cancel")
                    },
                    text = {
                        Text(text = "Are you sure you want to cancel?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                cancelDialogVisible = false
                                onBack() // Call onBack if the user confirms cancellation
                            }
                        ) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { cancelDialogVisible = false }
                        ) {
                            Text("No")
                        }
                    }
                )
            }


            // Code for the save confirmation dialog
            if (saveDialogVisible) {
                AlertDialog(
                    onDismissRequest = { saveDialogVisible = false },
                    title = {
                        Text(text = "Save")
                    },
                    text = {
                        Text(text = "Are you sure you want to save?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                saveDialogVisible = false

                                println("SOMETHING")
//                                onSavedBack(newBitmap)
                            }
                        ) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { saveDialogVisible = false }
                        ) {
                            Text("No")
                        }
                    }
                )

            }

        }


    }


    @Composable
    fun BrushIcon(selectedColor: MutableState<Color>, brushWidth: Float) {
        val color = selectedColor.value
        val iconSize = 100.dp

        val brushWidthDp = with(LocalDensity.current) { brushWidth.dp }

        // Convert dp to pixels
        val brushWidthPx = with(LocalDensity.current) { brushWidthDp.toPx() }

        // Calculate the radius
        val radius = remember(brushWidthPx) {
            brushWidthPx * 2
        }

        Canvas(
            modifier = Modifier.size(iconSize)
        ) {
            val centerOffset = Offset(size.width / 2, size.height / 2)
            drawCircle(
                brush = SolidColor(color),
                center = centerOffset,
                radius = radius,
                alpha = 1f // Alpha should be specified explicitly
            )
        }
    }

    @Composable
    fun EraserIcon() {
        Icon(
            imageVector = Icons.Default.Create,
            contentDescription = "Eraser Icon",
            tint = Color.White,
            modifier = Modifier.size(100.dp)
        )
    }



}