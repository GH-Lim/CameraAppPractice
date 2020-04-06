package com.example.mycameraapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var permission_list = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        button.setOnClickListener { view ->
            dispatchTakePictureIntent()
        }

        button2.setOnClickListener { view ->
            openGallery()
        }
    }

    fun checkPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return
        }
        for(permission: String in permission_list){
            var chk = checkCallingOrSelfPermission(permission)

            if(chk == PackageManager.PERMISSION_DENIED){
                requestPermissions(permission_list, 0);
                break
            }
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1
    val OPEN_GALLERY = 2

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        } else if (requestCode == OPEN_GALLERY && resultCode == Activity.RESULT_OK) {
            val currentImageUrl : Uri? = data?.data

            try{
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUrl)
                imageView.setImageBitmap(bitmap)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }
}
