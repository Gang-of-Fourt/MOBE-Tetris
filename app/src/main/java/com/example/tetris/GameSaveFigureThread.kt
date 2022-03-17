package com.example.tetris

import android.view.SurfaceHolder
import android.graphics.Canvas
import com.example.tetris.models.figures.RandomFigure
import kotlin.random.Random

// S'occupe de faire tomber, pivoter et décaler les pieces
class GameSaveFigureThread (var surfaceHolder : SurfaceHolder, var gameView : GameView) : Thread() {

    private var running : Boolean = false

    fun setRunning(isRunning: Boolean) {
        running = isRunning
    }
    override fun run(){
        var canvas : Canvas? = null
        try {
//            canvas = surfaceHolder.lockCanvas();

            synchronized(surfaceHolder) {
                gameView.save()
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
            gameView.handler.postDelayed(this, 16)
    }
}