package com.example.tetris

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class ScoreActivity : AppCompatActivity() {

    lateinit var mediaPlayer : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
        val textViewScore = findViewById<TextView>(R.id.textViewScore)
        textViewScore.text = "${intent.getStringExtra("score")} "
        mediaPlayer = MediaPlayer.create(this, R.raw.scoremusic)
        mediaPlayer.start()
    }

    fun replayClicked(v : View) {
        val myIntent = Intent(this, GameActivity::class.java)
        startActivity(myIntent)
        mediaPlayer.stop()
        finish()
    }

    fun menuClicked(v : View) {
        val myIntent = Intent(this, MainActivity::class.java)
        startActivity(myIntent)
        mediaPlayer.stop()
        finish()
    }
}