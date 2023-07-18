package com.example.files

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.files.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    companion object{


        var ourViewType=0

        var spanCount=1


    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding=ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)





        val file=getExternalFilesDir(null)!!


        val path=file.path




        val transAction=supportFragmentManager.beginTransaction()

        transAction.add(R.id.frame_main, FragmentFile(path))

        transAction.addToBackStack(null)
        transAction.commit()
    }
}
