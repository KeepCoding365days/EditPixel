package com.example.editpixel

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.editpixel.ui.theme.EditPixelTheme

class ProjectList : AppCompatActivity() {
    private val helper=StorageHelper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EditPixelTheme {
                Surface (modifier = Modifier) {
                    Projects()
                }
            }
        }
    }
    fun ExtractBitmap(uri: Uri):Bitmap{
        val source= ImageDecoder.createSource(this.contentResolver,uri)
        val bitmap= ImageDecoder.decodeBitmap(source)
        return bitmap
    }

    @Composable
    fun Projects(){

        val projects=helper.ProjectList(applicationContext)
        
        LazyColumn(modifier= Modifier
            .fillMaxSize()
            .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            items(projects){project->
                ProjectCard(name = project)
        }

        }
        SmallFloatingActionButton(
            onClick = {
                val i=Intent(applicationContext,LandingPage::class.java)
                startActivity(i)
                finish()
            },
            modifier = Modifier
                .padding(16.dp)
                .offset(300.dp, 600.dp)
        ) {
            Icon(Icons.Filled.Add, "Fap_Add")
        }
    }
    @Composable
    fun ProjectCard(name:String){
        val uri=helper.getProjectImage(applicationContext,name)
        var bitmap=BitmapFactory.decodeResource(applicationContext.resources,R.drawable.addicon)
        if (uri!=null){
            bitmap=ExtractBitmap(uri)
        }
        OutlinedCard (modifier = Modifier
            .padding(10.dp)
            .clickable(onClick = {
                val i = Intent(applicationContext, ProjectGallery::class.java)
                i.putExtra("project_name", name)
                startActivity(i)
                finish()
            })) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp)
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    Modifier
                        .size(size = 150.dp)
                        .border(BorderStroke(4.dp, Color.White), CircleShape)
                        .padding(10.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = name, modifier = Modifier.padding(10.dp), textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }
    }
}