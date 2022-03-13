package com.example.tetris

import android.content.Context
import android.content.pm.ActivityInfo
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
import kotlin.math.abs


class MainActivity : AppCompatActivity(), SensorEventListener {
    @RequiresApi(Build.VERSION_CODES.R)
    lateinit var accelerometre : Sensor
    lateinit var gyroscope : Sensor
    lateinit var light: Sensor

    private lateinit var sensorManager : SensorManager
    lateinit var gameView : GameView

    var timeSave = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO Tester si les capteurs sont présents sur le tel
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        gameView = GameView(this)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        setContentView(gameView)

    }
    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                // On donne les nouvelles valeures de l'acceleromatre au gameView
                gameView.valuesAccelerometerY = event.values[1]
            }
            Sensor.TYPE_GYROSCOPE -> {
                val timeCurrent = System.currentTimeMillis()
                if (abs(timeCurrent - timeSave) > 200) {
                    if (event.values[2] < -2) {
                        gameView.currentForm.rotate(EnumSens.SENS_HORAIRE, gameView.grille)
                        timeSave = System.currentTimeMillis()
                    }
                    if (event.values[2] > 2) {
                        gameView.currentForm.rotate(EnumSens.SENS_ANTIHORAIRE, gameView.grille)
                        timeSave = System.currentTimeMillis()
                    }
                }
            }
            Sensor.TYPE_LIGHT -> {
                gameView.lightSensor = event.values[0]
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometre, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
    }



}