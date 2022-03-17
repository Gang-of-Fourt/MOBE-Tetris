package com.example.tetris

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity()  {

    lateinit var gameView : GameView
    lateinit var sensorEvent : SensorEvent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO Tester si les capteurs sont présents sur le tel
        gameView = GameView(this)
        sensorEvent = SensorEvent(this, gameView)
        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
            sensorEvent.onResume()
        }
}