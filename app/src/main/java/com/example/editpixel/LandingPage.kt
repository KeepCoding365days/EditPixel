package com.example.editpixel

import android.content.ContentValues.TAG
import android.content.Intent
import android.icu.text.CaseMap.Title
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import com.example.editpixel.ui.theme.EditPixelTheme

class LandingPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContent(){
            EditPixelTheme() {
                Surface(
                    modifier=Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    AddProject()
                }
            }
        }
    }
    @Composable
    fun AddProject(){
        val Addicon:Painter= painterResource(id = R.drawable.addicon)
        var addProject by remember{ mutableStateOf(false) }
        fun toggle(){
            addProject=!addProject
        }

        Column (
            verticalArrangement=Arrangement.Center,
            horizontalAlignment=Alignment.CenterHorizontally,
            modifier = Modifier.clickable(onClick = {addProject=true}
            )
        ){
            Image(painter = Addicon, contentDescription = "Add icon")
            Text(text = "Create Project", color = Color.White)
        }
        if(addProject){
            ProjectDialog(onDismiss = {addProject=false})
        }

    }
    fun Projectlist(){
        val helper=StorageHelper()

        if (helper.ProjectList(applicationContext).isNotEmpty()){
            val i= Intent(applicationContext,ProjectList::class.java)
            startActivity(i)
            finish()
        }

    }


    @OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
    @Composable
    fun ProjectDialog(onDismiss: ()->Unit){

        val inputText= remember{ mutableStateOf("") }
        val keyboardController= LocalSoftwareKeyboardController.current
        fun Project(){
            val i= Intent(applicationContext, ProjectGallery::class.java)
            val helper=StorageHelper()
            var created=false

            val projects=helper.ProjectList(applicationContext)
            for (project in projects) {
                if (project==inputText.value){
                    created=true;
                }
            }
            if(created){
                Toast.makeText(this,"A project with this name already exists",Toast.LENGTH_SHORT).show()
            }
            else{
                created=helper.createProject(applicationContext,inputText.value)
                if(!created){
                    Toast.makeText(this,"Project is not created",Toast.LENGTH_SHORT).show()
                }
                else{
                    i.putExtra("project_name",inputText.value)
                    Log.d(TAG,inputText.value)
                    startActivity(i)
                    finish()
                }
            }
        }
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title={
                Text(text = "Create a New Project")
            },
            text = {
                   TextField(value =inputText.value ,
                       onValueChange = {inputText.value=it},
                       label={Text("Enter Project Name")},
                       keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                       keyboardActions = KeyboardActions(onDone = {
                           keyboardController?.hide()
                       })
                   )
            },
            dismissButton = {
                            Button(onClick = { onDismiss()
                                Projectlist()
                                //inputText.value=""
                            }) {
                                Text("Cancel")
                            }
            },
            confirmButton = {
                Button(onClick = { Project() },
                    enabled = inputText.value.isNotEmpty()) {
                    Text("Create")
                }
            })
    }

}