package com.lihan.pickcontent

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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

                Log.d("TAG", " :${uri.scheme} ")
                if (uri.scheme?.contains("content") == true) {
                    var fileName : String? = null
                    val cursor = contentResolver.query(
                        uri,null,null,null,null
                    )
                    try {
                        if (cursor!=null && cursor.moveToFirst()){
                            fileName = cursor.getString(
                                 cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                            )
                            Log.d("TAG", " cursor!=null  :  ${fileName}")
                        }
                    }catch (e : Exception){
                        e.stackTrace
                    }finally {
                        cursor?.close()
                    }
                    if (fileName == null){
                        fileName = uri.path
                        val cutt : Int = fileName?.lastIndexOf('/')?:0
                        if (cutt != -1){
                            fileName = fileName?.substring((cutt + 1))
                        }
                    }
                    Log.d("TAG", " fileName >>> ${fileName}")
                }



//                val file = File(cacheDir , "file")
//                file.createNewFile()
//                file.outputStream().use {
//                    contentResolver.openInputStream(uri)?.copyTo(it)
//                }
//                binding.fileNameTextView.text = file.path
//                Log.d("TAG", "Yes Can Send  this file : $fileType")

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