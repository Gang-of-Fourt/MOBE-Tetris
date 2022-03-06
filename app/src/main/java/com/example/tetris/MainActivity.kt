package com.example.tetris

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), SensorEventListener {
    @RequiresApi(Build.VERSION_CODES.R)
    var valuesAcceleromoetre : MutableList<Float> = MutableList(3) {0F}
    lateinit var accelerometre : Sensor
    lateinit var sensorManager : SensorManager
    lateinit var gameView : GameView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gameView = GameView(this)
        setContentView(gameView)

    }
    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                valuesAcceleromoetre[0] = event.values[0]
                valuesAcceleromoetre[1] = event.values[1]
                valuesAcceleromoetre[2] = event.values[2]
                // On donne les nouvelles valeures de l'acceleromatre au gameView
                gameView.valuesAccelerometer = valuesAcceleromoetre
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometre, SensorManager.SENSOR_DELAY_NORMAL)
    }



}