package com.example.tetris

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.example.tetris.models.EnumSens
import com.example.tetris.models.EnumsRL
import com.example.tetris.models.Figure
import com.example.tetris.models.Grille
import com.example.tetris.models.figures.*
import kotlin.math.pow
import kotlin.math.sqrt


class GameView(context: Context) : SurfaceHolder.Callback , SurfaceView(context){

    private var gameDrawThread = GameDrawThread(holder, this)
    private var gameFallThread = GameFallThread(holder, this)
    var grille = Grille(20,12)
    var valuesAccelerometerY = 0F
    var currentForm : Figure = RandomFigure.chooseFigure()
    private var touch = mutableListOf(0F, 0F)
    private var unTouch = mutableListOf(0F, 0F)

    init {
        holder.addCallback(this)
    }

    //Si l'ecran est touché
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touch[0] = event.x
                touch[1] = event.y
                println(event.y)
            }
            MotionEvent.ACTION_UP -> {
                unTouch[0] = event.x
                unTouch[1] = event.y
                if(isSwip()){
                    swip()
                }
            }
        }
        return true
    }

    private fun isSwip() : Boolean{
        val distance = sqrt((unTouch[0] - touch[0]).pow(2) + (unTouch[1] - touch[1]).pow(2))
        return distance > 150
    }

    fun swip(){
        if(touch[0] < unTouch[0]){
         currentForm.updateCoordX(grille, EnumsRL.RIGHT)
        } else {
            currentForm.updateCoordX(grille, EnumsRL.LEFT)
        }
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}


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
            currentForm.updateCoordY()
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

//    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//        when (event?.action) {
//            MotionEvent.ACTION_UP -> {
//                touch[0] = event.x
//                touch[1] = event.y
//                println(event.x)
//                println(event.y)
//            }
//            MotionEvent.ACTION_DOWN -> {
//                unTouch[0] = event.x
//                unTouch[1] = event.y
//            }
//
//        }
//
//        return v?.onTouchEvent(event) ?: true
//    }


}