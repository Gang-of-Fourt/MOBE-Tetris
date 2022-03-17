package com.example.tetris

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tetris.models.EnumSens
import java.lang.Math.abs

class SensorEvent (context : Context, private val gameView : GameView) : SensorEventListener {

    private var sensorManager : SensorManager

    @RequiresApi(Build.VERSION_CODES.R)
    var accelerometre : Sensor
    var gyroscope : Sensor
    var light: Sensor

    var timeSave = 0L

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
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
                        gameView.currentFigure.rotate(EnumSens.SENS_HORAIRE, gameView.grille)
                        timeSave = System.currentTimeMillis()
                    }
                    if (event.values[2] > 2) {
                        gameView.currentFigure.rotate(EnumSens.SENS_ANTIHORAIRE, gameView.grille)
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

    fun onResume() {
        sensorManager.registerListener(this, accelerometre, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
    }
}