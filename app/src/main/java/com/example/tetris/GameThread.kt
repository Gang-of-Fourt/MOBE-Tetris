package com.example.tetris

import android.view.SurfaceHolder
import android.graphics.Canvas
import kotlin.random.Random

class GameThread (var surfaceHolder : SurfaceHolder , var gameView : GameView) : Thread() {

    private var running : Boolean = false

//    private val r = Random.nextInt(-50, 300).toFloat()

    fun setRunning(isRunning: Boolean) {
        running = isRunning
    }
    override fun run(){
        while(running){
            var canvas : Canvas? = null
            try {
                canvas = surfaceHolder.lockCanvas();

                synchronized(surfaceHolder) {
                    gameView.update()
                    gameView.draw(canvas);
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
        }
    }
}