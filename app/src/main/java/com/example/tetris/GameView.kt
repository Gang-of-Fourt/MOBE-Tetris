package com.example.tetris

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.DisplayMetrics
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.tetris.models.EnumSens
import com.example.tetris.models.figures.*


class GameView(context: Context, val metrics: DisplayMetrics) : SurfaceHolder.Callback , SurfaceView(context) {

    private var timer : Int = 0
    private var thread : GameThread
    var baton: Baton = Baton(Color.RED)
    var carre: Carre = Carre(Color.BLUE)
    var formeL: FormeL = FormeL(Color.CYAN)
    var formeT: FormeT = FormeT(Color.BLACK)
    var formeZ1: FormeZ1 = FormeZ1(Color.YELLOW)
    var formeZ2: FormeZ2 = FormeZ2(Color.GRAY)


    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}

    fun update() {
        timer = (timer + 1 ) % 10
    }
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas != null) {
            val paint = Paint()
            paint.setColor(Color.BLUE)

            canvas.drawColor(Color.WHITE)
//            baton.draw(canvas)
            println(canvas.height)
            println(canvas.width)
            carre.draw(canvas)
            baton.draw(canvas)
//            canvas.drawRect(0F,0F,800F,1880F, paint);
//            if (carre.hasItGround(canvas)){
//                formeL.draw(canvas)
//            }
            if (timer == 0){
                if (!baton.hasItGround(canvas)){
                    baton.updateCoord()
//                    baton.rotate(EnumSens.SENS_HORAIRE)
                }
                if (!carre.hasItGround(canvas)){
                    carre.updateCoord()
                    carre.rotate(EnumSens.SENS_HORAIRE)
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