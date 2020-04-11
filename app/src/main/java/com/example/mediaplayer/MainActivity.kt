package com.example.mediaplayer

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.view.*

class MainActivity : AppCompatActivity() {
    var listSongs=ArrayList<SongInfo>()
    var adapter:MySongAdapter?=null
    var mp: MediaPlayer?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadURLOnline()
        CheckUserPermsions()
      //  adapter=MySongAdapter(listSongs)
       // lsListSongs.adapter=adapter
        var mytracking= mySongTrack()
        mytracking.start()
    }
    fun loadURLOnline(){
        listSongs.add(SongInfo("gaana","abc","https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_700KB.mp3"))
        listSongs.add(SongInfo("gaana","abc","https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_1MG.mp3"))
        listSongs.add(SongInfo("gaana","abc","https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_2MG.mp3"))
        listSongs.add(SongInfo("gaana","abc","https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_5MG.mp3"))
        listSongs.add(SongInfo("gaana","abc","https://file-examples.com/wp-content/uploads/2017/11/file_example_MP3_5MG.mp3"))
    }
    inner class MySongAdapter:BaseAdapter{
        var myListSong=ArrayList<SongInfo>()
        constructor(myListSong:ArrayList<SongInfo>):super(){
        this.myListSong= myListSong
        }
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater:LayoutInflater = LayoutInflater.from(applicationContext)
            val view:View = layoutInflater.inflate(R.layout.song_ticket,null)
            val song = this.myListSong[position]
            view.tvSongName.text = song.Title
            view.tvAuthor.text = song.AuthorName
            view.buPlay.setOnClickListener(View.OnClickListener{
                if (view.buPlay.text == "stop"){
                    mp!!.stop()
                    view.buPlay.text= "start"
                }else {
                    mp = MediaPlayer()
                    try {
                        val uri = Uri.parse(song.SongURL)
                        mp!!.setDataSource(applicationContext, uri)
                        mp!!.prepare()
                        mp!!.start()
                        view.buPlay.text = "Stop"
                        sbProgress.max=mp!!.duration
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            })
            return view
        }
        override fun getItem(position: Int): Any {
            return  this.myListSong[position]
        }
        override fun getItemId(position: Int): Long {
         return position.toLong()
        }
        override fun getCount(): Int {
            return this.myListSong.size
            }
    }
    inner  class  mySongTrack :Thread(){
        override fun run() {
            while(true){
                try{
                    sleep(1000)
                }catch (ex:Exception){
                    ex.printStackTrace()
                }
                runOnUiThread {
                    if (mp!=null){
                        sbProgress.progress = mp!!.currentPosition
                    }
                }
            }
        }
    }

    fun CheckUserPermsions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_ASK_PERMISSIONS)
                return
            }
        }
        LoadSong()
    }
 private val REQUEST_CODE_ASK_PERMISSIONS = 123

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoadSong()
            } else {
                Toast.makeText(this, "denail", Toast.LENGTH_SHORT).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
    fun   LoadSong() {
        val allSongsURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val cursor = contentResolver.query(allSongsURI, null, selection, null, null)
        if (cursor != null) {
            if (cursor!!.moveToFirst()) {
                do {
                    val songURL = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val SongAuthor = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val SongName = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    listSongs.add(SongInfo(SongName, SongAuthor, songURL))
                } while (cursor!!.moveToNext())
            }
            cursor!!.close()
            adapter=MySongAdapter(listSongs)
            lsListSongs.adapter=adapter
        }
    }
}

