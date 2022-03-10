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


class GameView(context: Context, handler: android.os.Handler) : SurfaceHolder.Callback , SurfaceView(context){

    private var gameDrawThread : GameDrawThread
    private var gameFallThread : GameFallThread
    var grille = Grille(20,12)
    var valuesAccelerometer : MutableList<Float> = MutableList(3) {0F}
    var valuesGyroscopeZ : Float = 0F
    var currentForm : Figure = RandomFigure.chooseFigure()

    init {
        holder.addCallback(this)
        gameDrawThread = GameDrawThread(holder, this)
        gameFallThread = GameFallThread(holder, this)
    }

    //Si l'ecran est touché
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            // Appelle la fonciton de rotation si celle ci n'a pas touchée le sol
            MotionEvent.ACTION_DOWN -> if (!currentForm.hasItGround(grille)) currentForm.rotate(EnumSens.SENS_HORAIRE, grille)
        }
        return true
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}

    // Incremente le timer, plus le modulo est grand, plus le jeu sera lent
//    fun update() {
//        // Si le joueur a son téléphone penché
//        if (valuesAccelerometer[2] >= 4){
//            timer = (timer + 1 ) % 15
//        } else {
//            timer = (timer + 1 ) % 40
//        }
//    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        val SIZE  = (height / grille.height - (height / grille.height * 1/5)).toFloat()
        val CONST = height - SIZE* grille.height

        // Tans que le joueur n'a pas perdu la partie
        if (canvas != null && !grille.isGameOver()) {
            canvas.drawColor(Color.WHITE)
            currentForm.draw(canvas, SIZE, CONST)
            grille.draw(canvas, SIZE, CONST)

            // Si la figure courante n'a pas touché le sol


        } else {
            // Dessine uniquement les fgigures qui composent la grille si le joueur a perdu sa partie
            grille.draw(canvas!!, SIZE, CONST)
        }
    }

    fun fall() {
        if (!currentForm.hasItGround(grille)){
            currentForm.updateCoord(valuesAccelerometer, grille)
        } else { // Si la figure courante a touché le sol
            grille.update(currentForm) // Ajoute la figure courante à la grille
            currentForm = RandomFigure.chooseFigure() // choisi une nouvelle figure
            val lineFull = grille.isLineFull() // Vérifie si une ligne de la grille est remplie
            if (lineFull.isNotEmpty()){
                grille.deletLines(lineFull) // supprime les lignes remplies si eil y en a
            }
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        gameDrawThread.setRunning(true)
        gameDrawThread.start()
        gameFallThread.setRunning(true)
        gameFallThread.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                gameDrawThread.setRunning(false)
                gameDrawThread.join()
                gameFallThread.setRunning(false)
                gameFallThread.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            retry = false
        }
    }

}