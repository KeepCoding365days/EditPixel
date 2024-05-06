package com.example.editpixel

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.editpixel.ui.theme.EditPixelTheme

class ProjectList : AppCompatActivity() {
    private val helper=StorageHelper()
    private val project_name=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContent {
            EditPixelTheme {
                Surface (modifier = Modifier,
                    color = Color.Black
                ) {
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
                .offset(LocalConfiguration.current.screenWidthDp.dp-70.dp,
            LocalConfiguration.current.screenHeightDp.dp-100.dp)
        ) {
            Icon(Icons.Filled.Add, "Fap_Add")
        }
    }

    @Composable
    fun ProjectCard(name:String){
        val uri=helper.getProjectImage(applicationContext,name)
        var bitmap=BitmapFactory.decodeResource(applicationContext.resources,R.drawable.logo)
        var delBtn by remember {
            mutableStateOf(false)
        }

        if (delBtn){
            AlertDialog(onDismissRequest = { delBtn=false  },title={
                Text("Are you sure to delete this Project")
            },
                confirmButton = {
                    TextButton(onClick = {
                        if(helper.deleteProject(applicationContext,name)){
                        Toast.makeText(applicationContext,"Project deleted",Toast.LENGTH_SHORT).show()
                            setContent(){
                                EditPixelTheme {
                                    Surface (modifier = Modifier) {
                                        Projects()
                                    }
                                }
                            }
                    }
                    else{
                        Toast.makeText(applicationContext,"Project can not be deleted",Toast.LENGTH_SHORT).show()
                    }
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
                })

        }
        if (uri!=null){
            bitmap=ExtractBitmap(uri)
        }
        OutlinedCard (modifier = Modifier
            .padding(5.dp).background(color = Color.Black)
            .clickable(onClick = {
                val i = Intent(applicationContext, ProjectGallery::class.java)
                i.putExtra("project_name", name)
                startActivity(i)
                finish()
            })) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.background(color = Color.DarkGray).fillMaxWidth()
            ) {

                Text(
                    text = name, modifier = Modifier.padding(5.dp), textAlign = TextAlign.Center,color= Color.White,
                    fontFamily = FontFamily.Cursive, style = MaterialTheme.typography.displayMedium
                )

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    Modifier
                        .size(size = 200.dp)
                        .padding(5.dp)
                        .clip(CircleShape)
                )

                Icon(imageVector = Icons.Filled.Delete, contentDescription = "",tint=Color.White, modifier =
                Modifier.clickable (onClick = {
                    delBtn=true

                }).padding(5.dp) )
            }
        }
    }
}