package com.example.mediaplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.view.*

class MainActivity : AppCompatActivity() {

    var listSongs=ArrayList<SongInfo>()
    var adapter:MySongAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadURLOnline()
        adapter=MySongAdapter(listSongs)
        lsListSongs.adapter=adapter
    }

    fun loadURLOnline(){

        listSongs.add(SongInfo("gaana","abc","https://gaana.com/playlist/rosidathokchom-gbabd-audiohindisong"))
        listSongs.add(SongInfo("gaana","abc","https://gaana.com/song/audio-13"))
        listSongs.add(SongInfo("gaana","abc","https://gaana.com/song/audio-14"))
        listSongs.add(SongInfo("gaana","abc","https://gaana.com/song/audio-15"))
        listSongs.add(SongInfo("gaana","abc","https://gaana.com/song/audio-16"))
        listSongs.add(SongInfo("gaana","abc","https://gaana.com/song/audio-17"))

    }

    inner class MySongAdapter:BaseAdapter{
        var myListSong=ArrayList<SongInfo>()
        constructor(myListSong:ArrayList<SongInfo>):super(){
        this.myListSong= myListSong
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater:LayoutInflater = LayoutInflater.from(applicationContext)
            val view:View = layoutInflater.inflate(R.layout.song_ticket,null)
            val song = this .myListSong[position]
            view.tvSongName.text = song.Title
            view.tvAuthor.text = song.AuthorName


            view.buPlay.setOnClickListener{

            }

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
}
