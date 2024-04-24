package com.example.editpixel

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {

    companion object {
        const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val helper=StorageHelper()
        val projects=helper.ProjectList(applicationContext)
        var i= Intent(applicationContext,LandingPage::class.java)
        if(projects.size>0){
            i=Intent(applicationContext,ProjectList::class.java)
        }
        startActivity(i)
        finish()

    }

}
