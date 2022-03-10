package com.example.tetris

import android.view.SurfaceHolder
import android.graphics.Canvas
import com.example.tetris.models.figures.RandomFigure
import kotlin.random.Random

class GameFallThread (var surfaceHolder : SurfaceHolder, var gameView : GameView) : Thread() {

    private var running : Boolean = false

    fun getDuration() =  1500F/ gameView.valuesAccelerometer[1].coerceAtLeast(1F)

    fun setRunning(isRunning: Boolean) {
        running = isRunning
    }
    override fun run(){
        var canvas : Canvas? = null
        try {
            canvas = surfaceHolder.lockCanvas();

            synchronized(surfaceHolder) {

                gameView.fall()
            }
        } catch (e: Exception) {}
        finally {
            if (canvas != null) {
                try {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                } catch (e: Exception) {
                    e.printStackTrace();
                }
            }
        }
        gameView.handler.postDelayed(this, getDuration().toLong())
    }
}