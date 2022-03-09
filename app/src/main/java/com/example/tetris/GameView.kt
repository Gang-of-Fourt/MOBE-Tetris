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
    fun update() {
        // Si le joueur a son téléphone penché
        if (valuesAccelerometer[2] >= 4){
            timer = (timer + 1 ) % 15
        } else {
            timer = (timer + 1 ) % 40
        }

    }


    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        val SIZE  = 0F
        if(canvas != null){
            val SIZE = (height / grille.height - (height / grille.height * 1/5)).toFloat()
        }

        // Tans que le joueur n'a pas perdu la partie
        if (canvas != null && !grille.isGameOver()) {

            canvas.drawColor(Color.WHITE)
            currentForm.draw(canvas, SIZE)
            grille.draw(canvas, SIZE)

            // A chaque fois que le timer est reset, le jeu vancera d'une frame
            // cad qu'il calculera si la piece va doite ou gauche, si elle touche le sol etc..
            if (timer == 0){
                // Si la figure courante n'a pas touché le sol
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
        } else {
            // Dessine uniquement les fgigures qui composent la grille si le joueur a perdu sa partie
            grille.draw(canvas!!, SIZE)
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