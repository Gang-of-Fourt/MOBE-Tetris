package com.example.tetris

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    lateinit var mediaPlayer : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mediaPlayer = MediaPlayer.create(this, R.raw.menumusic)
        mediaPlayer.start()
    }

    fun playClicked(v : View) {
        mediaPlayer.stop()
        val myIntent = Intent(this, GameActivity::class.java)
        startActivity(myIntent)
        finish()
    }

    fun calibrateClicked(v : View) {
        mediaPlayer.stop()
        val myIntent = Intent(this, GameActivity::class.java)
        startActivity(myIntent)
        finish()
    }
}