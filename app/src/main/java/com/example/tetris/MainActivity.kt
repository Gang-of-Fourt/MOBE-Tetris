package com.example.tetris

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.tetris.models.EnumSens


class MainActivity : AppCompatActivity(), SensorEventListener {
    @RequiresApi(Build.VERSION_CODES.R)
    lateinit var accelerometre : Sensor
    lateinit var gyroscope : Sensor

    lateinit var sensorManager : SensorManager
    lateinit var gameView : GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        gameView = GameView(this)
        setContentView(gameView)

    }
    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                // On donne les nouvelles valeures de l'acceleromatre au gameView
                gameView.valuesAccelerometerY = event.values[1]
            }
            Sensor.TYPE_GYROSCOPE -> {
                if(event.values[2]>2) {
                    gameView.currentForm.rotate(EnumSens.SENS_HORAIRE, gameView.grille)
                } else if (event.values[2]<-2){
                    gameView.currentForm.rotate(EnumSens.SENS_HORAIRE, gameView.grille)
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometre, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
    }



}