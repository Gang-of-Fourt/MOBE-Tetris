package com.example.tetris

import android.graphics.Canvas
import android.view.SurfaceHolder

class CalibrationThread(var surfaceHolder : SurfaceHolder, var gameView: GameView) : Thread() {

    private var running : Boolean = false

    fun setRunning(isRunning: Boolean) {
        running = isRunning
    }
    override fun run(){
        var canvas : Canvas? = null
        try {
            canvas = surfaceHolder.lockCanvas();

            synchronized(surfaceHolder) {
                gameView.calibrate();
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
        if (running)
            gameView.handler.postDelayed(this, 400)
    }
}
