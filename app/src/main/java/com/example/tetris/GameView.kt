package com.example.tetris

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.tetris.models.figures.Baton
import com.example.tetris.models.figures.Carre
import com.example.tetris.models.figures.FormeL


class GameView(contextx: Context, val metrics: DisplayMetrics) : SurfaceHolder.Callback , SurfaceView(contextx) {

    private var timer : Int = 0
    private var thread : GameThread
    var baton: Baton
    var carre: Carre
    var formeL: FormeL


    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
        baton = Baton(Color.RED)
        carre = Carre(Color.BLUE)
        formeL = FormeL(Color.CYAN)

    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}


    fun update() {
        timer = (timer + 1 ) % 10
        context
    }
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas != null) {
            canvas.drawColor(Color.WHITE)
            baton.draw(canvas)
            carre.draw(canvas)
            if (baton.hasItGround(canvas)){
                formeL.draw(canvas)
            }
            if (timer == 0 ){
                if (!baton.hasItGround(canvas)){
                    baton.updateCoord()
                } else {
                    if (!formeL.hasItGround(canvas)){
                        formeL.updateCoord()
                    }
                }

                if (!carre.hasItGround(canvas)){
                    carre.updateCoord()
                }
//                baton.rotate(EnumSens.SENS_ANTIHORAIRE)
            }
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