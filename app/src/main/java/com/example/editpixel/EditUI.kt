import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.editpixel.BitmapObject
import com.example.editpixel.R

class EditingUI : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EditorUI()
        }
    }
}

@Composable
fun EditorUI() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {

            /* top icons row */
            var selectedButton by remember { mutableStateOf("") }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                BarButton("filter", R.drawable.filters, selectedButton == "filter") {
                    selectedButton = "filter"
                }
                BarButton("Crop", R.drawable.crop, selectedButton == "Crop") {
                    selectedButton = "Crop"
                }
                BarButton("foreground and background", R.drawable.fg, selectedButton == "foreground and background") {
                    selectedButton = "foreground and background"
                }
                BarButton("bring to front", R.drawable.fgg, selectedButton == "bring to front") {
                    selectedButton = "bring to front"
                }
            }
            /* center selected image */
            Image(
                bitmap=BitmapObject.bitmap.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier.weight(1f).fillMaxWidth()
            )

            /* bottom icons row*/

            var saturation by remember { mutableStateOf(0f) }
            var hue by remember { mutableStateOf(0f) }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                BarButton("Saturation", R.drawable.hue, selectedButton == "Saturation") {
                    selectedButton = "Saturation"
                }
                BarButton("Color Palette", R.drawable.brush, selectedButton == "Color Palette") {
                    selectedButton = "Color Palette"
                }
                BarButton("Hue", R.drawable.drop, selectedButton == "Hue") {
                    selectedButton = "Hue"
                }
                BarButton("Brush", R.drawable.brush, selectedButton == "Brush") {
                    selectedButton = "Brush"
                }
            }

            // Show sliders based on selected button
            if (selectedButton == "Saturation") {
                Slider(value = saturation, onValueChange = { value -> saturation = value })
            } else if (selectedButton == "Hue") {
                Slider(value = hue, onValueChange = { value -> hue = value })
            }
        }
    }
}

@Composable
fun BarButton(text: String, iconRes: Int, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent
        )
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = text,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Preview
@Composable
fun PreviewEditorUI() {
    EditorUI()
}
