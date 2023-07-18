package com.example.files

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.files.databinding.DialogAddFileBinding
import com.example.files.databinding.DialogAddFolderBinding
import com.example.files.databinding.DialogDeleteItemBinding
import com.example.files.databinding.FragmentfileBinding

import java.io.File

class FragmentFile(val path: String) : Fragment(), FileAdapter.FileEvents {
    lateinit var binding: FragmentfileBinding
    lateinit var myAdapter: FileAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentfileBinding.inflate(layoutInflater)

        return binding.root


    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if (MainActivity.ourViewType==0){


            binding.btnShowType.setImageResource(R.drawable.ic_grid)



        }else{


            binding.btnShowType.setImageResource(R.drawable.ic_list)


        }




        val ourFile = File(path)






        binding.txtPath.text = ourFile.name + ">"


        if (ourFile.isDirectory) {

            val listOfFiles = arrayListOf<File>()
            listOfFiles.sort()

            listOfFiles.addAll(ourFile.listFiles()!!)
            myAdapter = FileAdapter(listOfFiles, this)

            binding.recyclerMain.adapter = myAdapter
            binding.recyclerMain.layoutManager = GridLayoutManager(context,MainActivity.spanCount,LinearLayoutManager.VERTICAL,false)


           myAdapter.changeViewType(MainActivity.ourViewType)







            if (listOfFiles.size > 0) {


                binding.recyclerMain.visibility = View.VISIBLE
                binding.imgNoData.visibility = View.GONE

            } else {

                binding.recyclerMain.visibility = View.GONE
                binding.imgNoData.visibility = View.VISIBLE


            }


        }


        binding.btnAddFolder.setOnClickListener {

            createNewFolder()


        }


        binding.btnAddFile.setOnClickListener {

            createNewFile()


        }
binding.btnShowType.setOnClickListener {


    if (MainActivity.ourViewType==0){

        MainActivity.ourViewType=1
        MainActivity.spanCount=3


        myAdapter.changeViewType(MainActivity.ourViewType)


        binding.recyclerMain.layoutManager=GridLayoutManager(context,MainActivity.spanCount)


        binding.btnShowType.setImageResource(R.drawable.ic_list)

    }else if (MainActivity.ourViewType==1){


        MainActivity.ourViewType=0
        MainActivity.spanCount=1


        myAdapter.changeViewType(MainActivity.ourViewType)



        binding.recyclerMain.layoutManager=GridLayoutManager(context,MainActivity.spanCount)

binding.btnShowType.setImageResource(R.drawable.ic_grid)
    }



}

    }

    private fun createNewFile() {
        val dialog = AlertDialog.Builder(context).create()

        val addFileBinding = DialogAddFileBinding.inflate(layoutInflater)
        dialog.setView(addFileBinding.root)

        dialog.show()



        addFileBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }


        addFileBinding.btnCreate.setOnClickListener {

            val nameOfNewFile = addFileBinding.edtAddFolder.text.toString()

            val newFile = File(path + File.separator + nameOfNewFile)


            if (!newFile.exists()) {

                if (newFile.createNewFile()) {


                    myAdapter.addNewFile(newFile)

                    binding.recyclerMain.scrollToPosition(0)


                    binding.recyclerMain.visibility = View.VISIBLE
                    binding.imgNoData.visibility = View.GONE

                }


            }

            dialog.dismiss()

        }

    }

    private fun createNewFolder() {
        val dialog = AlertDialog.Builder(context).create()

        val addFolderBinding = DialogAddFolderBinding.inflate(layoutInflater)
        dialog.setView(addFolderBinding.root)

        dialog.show()

        addFolderBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        addFolderBinding.btnCreate.setOnClickListener {

            val nameOfNewFolder = addFolderBinding.edtAddFolder.text.toString()

            val newFile = File(path + File.separator + nameOfNewFolder)
            if (!newFile.exists()) {
                if (newFile.mkdir()) {
                    myAdapter.addNewFile(newFile)
                    binding.recyclerMain.scrollToPosition(0)

                    binding.recyclerMain.visibility = View.VISIBLE
                    binding.imgNoData.visibility = View.GONE
                }
            }

            dialog.dismiss()

        }

        }


    override fun onLongClicked(file: File, position: Int) {
        val dialog = AlertDialog.Builder(context).create()

        val deleteDialog = DialogDeleteItemBinding.inflate(layoutInflater)

        dialog.setView(deleteDialog.root)

        dialog.setCancelable(true)

        dialog.show()



        deleteDialog.btnCancel.setOnClickListener {

            dialog.dismiss()


        }


        deleteDialog.btnCreate.setOnClickListener {


            if (file.exists()) {

                if (file.deleteRecursively()) {

                    myAdapter.deleteFile(file, position)


                }
            }


            dialog.dismiss()
        }


    }


    override fun onFileClicked(file: File, type: String) {


        val intent = Intent(Intent.ACTION_VIEW)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            val fileProvider = FileProvider.getUriForFile(
                requireContext(),
                requireActivity().packageName + ".provider",
                file
            )
            intent.setDataAndType(fileProvider, type)

        } else {

            intent.setDataAndType(Uri.fromFile(file), type)

        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)



    }

    override fun onFolderClicked(path: String) {


        val transAction = parentFragmentManager.beginTransaction()

        transAction.replace(R.id.frame_main, FragmentFile(path))

        transAction.addToBackStack(null)

        transAction.commit()


    }


}