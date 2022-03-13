package com.example.tetris

import android.view.SurfaceHolder
import android.graphics.Canvas
import kotlin.random.Random

// S'occupe de dessiner le jeu sur le téléphone
class GameDrawThread (var surfaceHolder : SurfaceHolder, var gameView : GameView) : Thread() {

    private var running : Boolean = false

    fun setRunning(isRunning: Boolean) {
        running = isRunning
    }
    override fun run(){
        var canvas : Canvas? = null
        try {
            canvas = surfaceHolder.lockCanvas();

            synchronized(surfaceHolder) {
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
        gameView.handler.postDelayed(this, 16)
    }
}