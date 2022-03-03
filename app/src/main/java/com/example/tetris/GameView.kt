package com.example.tetris

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.DisplayMetrics
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.tetris.models.EnumSens
import com.example.tetris.models.Grille
import com.example.tetris.models.figures.*


class GameView(context: Context, val metrics: DisplayMetrics) : SurfaceHolder.Callback , SurfaceView(context) {

    private var timer : Int = 0
    private var thread : GameThread
    var baton: Baton = Baton(Color.RED)
    var carre: Carre = Carre(Color.BLUE)
    var carre2: Carre = Carre(Color.RED)
    var carre3: Carre = Carre(Color.GREEN)
    var formeL: FormeL = FormeL(Color.CYAN)
    var formeT: FormeT = FormeT(Color.BLACK)
    var formeZ1: FormeZ1 = FormeZ1(Color.YELLOW)
    var formeZ2: FormeZ2 = FormeZ2(Color.GRAY)
    var grille = Grille(20,10)


    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
        carre2.coordonnees.posx=7
        carre3.coordonnees.posx=9
        formeL.coordonnees.posx=4
        formeL.rotate(EnumSens.SENS_HORAIRE)
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}

    fun update() {
        timer = (timer + 1 ) % 10
    }
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas != null) {
            canvas.drawColor(Color.WHITE)
            carre.draw(canvas)
            formeL.draw(canvas)
            grille.draw(canvas)
//            carre.draw(canvas)
//            carre2.draw(canvas)
//            carre3.draw(canvas)
//            baton.draw(canvas)
//            canvas.drawRect(900F,1900F,1000F,2000F, paint);
//            if (carre.hasItGround(canvas)){
//                formeL.draw(canvas)
//            }
            if (timer == 0){
                if (!carre.hasItGround(canvas, grille)){
                    carre.updateCoord()
//                    baton.rotate(EnumSens.SENS_HORAIRE)
                } else {
                    grille.update(carre)
                    if(!formeL.hasItGround(canvas, grille)){
                        formeL.updateCoord()
                    } else {
                        grille.update(formeL)
                    }
                }
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