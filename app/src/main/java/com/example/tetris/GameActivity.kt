package com.example.tetris

import android.media.MediaPlayer
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class GameActivity : AppCompatActivity()  {

    lateinit var gameView : GameView
    lateinit var sensorEvent : SensorEvent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController = ViewCompat.getWindowInsetsController(
            window.decorView
        )
//        if(windowInsetsController != null) {
//            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
//        }

        // TODO Tester si les capteurs sont présents sur le tel
        gameView = GameView(this)
        sensorEvent = SensorEvent(this, gameView)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
        sensorEvent.onResume()
    }
}