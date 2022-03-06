package com.example.tetris

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.tetris.models.EnumSens
import com.example.tetris.models.Figure
import com.example.tetris.models.Grille
import com.example.tetris.models.figures.*


class GameView(context: Context) : SurfaceHolder.Callback , SurfaceView(context){

    private var timer : Int = 0
    private var thread : GameThread
    var grille = Grille(20,11)
    var valuesAccelerometer : MutableList<Float> = MutableList(3) {0F}
    var currentForm : Figure = RandomFigure.chooseFigure()


    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (!currentForm.hasItGround(grille)) currentForm.rotate(EnumSens.SENS_HORAIRE, grille)
        }
        return true
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}

    fun update() {
        timer = (timer + 1 ) % 15
    }
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas != null && !grille.isGameOver()) {
            canvas.drawColor(Color.WHITE)
            currentForm.draw(canvas)
            grille.draw(canvas)
            if (timer == 0){
                if (!currentForm.hasItGround(grille)){
                    currentForm.updateCoord(valuesAccelerometer, grille)
                } else {
                    grille.update(currentForm)
                    currentForm = RandomFigure.chooseFigure()

                    val lineFull = grille.isLineFull()
                    if (lineFull.isNotEmpty()){
                        grille.deletLines(lineFull)
                    }
                }
            }
        } else {
            grille.draw(canvas!!)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            retry = false
        }
    }

}