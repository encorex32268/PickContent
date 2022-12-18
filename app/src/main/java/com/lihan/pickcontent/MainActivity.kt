package com.lihan.pickcontent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import com.lihan.pickcontent.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {


    private lateinit var binding : ActivityMainBinding
    
    private val requestFilePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null){
                val contentResolver = this@MainActivity.contentResolver
                val mineType = MimeTypeMap.getSingleton()
                val fileType = mineType.getExtensionFromMimeType(contentResolver.getType(uri))
                val file = File(cacheDir , "file")
                file.createNewFile()
                file.outputStream().use {
                  this@MainActivity.contentResolver.openInputStream(uri)?.copyTo(it)
                }
                binding.fileNameTextView.text = file.path
                Log.d("TAG", "Yes Can Send  this file : $fileType")

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            pickButton.setOnClickListener {
                requestFilePickerLauncher.launch(
                    "*/*"
                )
            }
        }
    }
}