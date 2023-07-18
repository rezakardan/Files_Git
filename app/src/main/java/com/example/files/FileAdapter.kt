package com.example.files

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.net.URLConnection
import java.util.*

class FileAdapter(val data: ArrayList<File> , val fileEvents: FileEvents) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

var ourViewType=0
    inner class FileViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val name = itemview.findViewById<TextView>(R.id.textView)

        val img = itemview.findViewById<ImageView>(R.id.imageView)

        fun onBind(position: Int) {

            var fileType=""



            name.text = data[position].name


            if (data[position].isDirectory) {

                img.setImageResource(R.drawable.ic_folder)


            } else {


                when {

                    isVideo(data[position].path) -> {


                        img.setImageResource(R.drawable.ic_video)

                        fileType = "video/*"                    }

                    isImage(data[position].path) -> {


                        img.setImageResource(R.drawable.ic_image)
                        fileType = "image/*"

                    }


                    isZip(data[position].name) -> {


                        img.setImageResource(R.drawable.ic_zip)
                        fileType = "application/*"

                    }
                    else -> {

                        img.setImageResource(R.drawable.ic_file)

                        fileType = "text/*"
                    }

                }


            }
itemView.setOnClickListener {


    if (data[position].isDirectory){


        fileEvents.onFolderClicked(data[position].path)


    }else{

        fileEvents.onFileClicked(data[position],fileType)


    }




}

            itemView.setOnLongClickListener {

                fileEvents.onLongClicked(data[position],adapterPosition)


                true
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {

        val view:View

        if (viewType==0){


            view = LayoutInflater.from(parent.context).inflate(R.layout.item_file_linear, parent, false)

            return FileViewHolder(view)


        }else{


            view=LayoutInflater.from(parent.context).inflate(R.layout.item_file_grid,parent,false)

            return FileViewHolder(view)

        }


    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.onBind(position)
    }








    override fun getItemCount(): Int {
        return data.size
    }






    fun addNewFile(file:File){

        data.add(0,file)

        notifyItemInserted(0)






    }


    fun deleteFile(file:File,position: Int){

        data.remove(file)

        notifyItemRemoved(position)







    }







    override fun getItemViewType(position: Int): Int {
        return ourViewType
    }



   private fun isImage(path: String): Boolean {


        val mimeType: String = URLConnection.guessContentTypeFromName(path)


        return mimeType.startsWith("image")


    }


   private fun isVideo(path: String): Boolean {


        val mimeType: String = URLConnection.guessContentTypeFromName(path)

        return mimeType.startsWith("video")


    }


    private fun isZip(name: String): Boolean {
        return name.contains(".zip") || name.contains(".rar")
    }




    fun changeViewType(newViewType: Int) {
        ourViewType = newViewType
        notifyDataSetChanged()
    }


    interface FileEvents{


        fun onLongClicked(file: File,position: Int)




        fun onFileClicked(file:File,type:String)


        fun onFolderClicked(path: String)


    }


}