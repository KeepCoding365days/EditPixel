package com.example.editpixel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class StorageHelper {

    fun StorageHelper(){}
    fun AddtoProject(name:String,context: Context, bitmap: Bitmap):String{
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.d(ContentValues.TAG,"folderName"+name)
        val dir= File(storageDir,name)
        var file=""
        Log.d(ContentValues.TAG,"storage directory found")
        if (dir != null) {
            if (!dir.exists() && !dir.mkdirs()) {
                Log.e("IMAGE_SAVE", "Failed to create directory for image storage.")
                return file
            }
            var count=dir.listFiles()?.size ?:0
            count=count+1
            file=count.toString()
            file= "$file.png"
            val imageFile = File(dir, file)
            try {
                FileOutputStream(imageFile).use { fos ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                }
                Log.d("IMAGE_SAVE", "Image saved to ${imageFile.absolutePath}")
            } catch (e: Exception) {
                Log.d(ContentValues.TAG,"can't save file")
                e.printStackTrace()
            }
        } else {
            Log.e("IMAGE_SAVE", "External Storage is not available or not mounted.")
        }
        return file
    }
    fun SaveImage(project_name:String,file_name:String,context: Context, bitmap: Bitmap){
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.d(ContentValues.TAG,"folderName"+project_name)
        val dir= File(storageDir,project_name)

        Log.d(ContentValues.TAG,"storage directory found")
        if (dir != null) {
            if (!dir.exists() && !dir.mkdirs()) {
                Log.e("IMAGE_SAVE", "Failed to create directory for image storage.")
                return
            }

            val imageFile = File(dir, file_name)
            try {
                FileOutputStream(imageFile).use { fos ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                }
                Log.d("IMAGE_SAVE", "Image saved to ${imageFile.absolutePath}")
            } catch (e: Exception) {
                Log.d(ContentValues.TAG,"can't save file")
                e.printStackTrace()
            }
        } else {
            Log.e("IMAGE_SAVE", "External Storage is not available or not mounted.")
        }
    }
    fun ExtractProjectUri(name: String,context:Context):List<Uri>{
        val uriList= mutableListOf<Uri>()
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val dir= File(storageDir,name)

        if (dir.isDirectory){
            dir.listFiles()?.forEach {
                file ->
                val uri=FileProvider.getUriForFile(context, context.packageName + ".provider", file)
                uriList.add(uri)
            }
        }
        return uriList
    }
    fun ProjectList(context: Context):List<String>{
        val projects= mutableListOf<String>()
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir != null) {
            val files= storageDir.listFiles()
            for (file in files){
                if(file.isDirectory){
                    projects.add(file.name)
                }
            }
        }
        return projects
    }
    fun getProjectImage(context: Context,name: String):Uri?{
        var uri:Uri?=null;
        val storageDir=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val dir=File(storageDir,name)
        val files=dir.listFiles()
        if (files.size>0){
            return Uri.fromFile(files[0])
        }
        return null
    }
    fun createProject(context: Context,name:String):Boolean{
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val dir= File(storageDir,name)
        var status:Boolean=true

        if (!dir.exists() && !dir.mkdirs()) {
            Log.e("IMAGE_SAVE", "Failed to create directory for image storage.")
            status=false
        }
        return status
    }
    fun deleteProject(context: Context,name: String):Boolean{
        val storagDir=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val dir=File(storagDir,name)
        return dir.delete()
    }
    fun deleteFile(context: Context,project_name: String,file_name: String):Boolean{
        val storagDir=context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val dir=File(storagDir,project_name)
        val file=File(dir,file_name)
        return file.delete()
    }



}