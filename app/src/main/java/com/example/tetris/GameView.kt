package com.example.tetris

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.annotation.RequiresApi
import com.example.tetris.models.*
import com.example.tetris.models.figures.*
import kotlin.math.pow
import kotlin.math.sqrt


class GameView(context: Context) : SurfaceHolder.Callback , SurfaceView(context){

    private var gameDrawThread = GameDrawThread(holder, this)
    private var gameFallThread = GameFallThread(holder, this)
    private var gameSaveFigureThread = GameSaveFigureThread(holder, this)

    var valuesAccelerometerY = 0F
    var lightSensor = 0F

    // La grille du jeu
    var grille = Grille(22,11)

    // La figure courrante
    var currentFigure= RandomFigure.chooseFigure()

    // La prochaine figure
    var nextFigure = NextFigure(RandomFigure.chooseFigure())

    // La figure sauvgardée par le joueur
    var saveFigure = SaveFigure(null)

    var highScore = HighScore()

    // Les coordonées de l'ecran quand le joueur le touche
    private var touch = mutableListOf(0F, 0F)

    // Les coordonées de l'ecran quand le joueur enleve son doigt de celui-ci
    private var unTouch = mutableListOf(0F, 0F)

    init {
        holder.addCallback(this)
    }

    //Si l'ecran est touché
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
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

    // Si le joueur a fait un swip sur l'ecran
    private fun isSwip() : Boolean{
        val distance = sqrt((unTouch[0] - touch[0]).pow(2) + (unTouch[1] - touch[1]).pow(2))
        return distance > 150
    }

    // Effectu le swip, donc décale la figure à gauche ou à droite
    fun swip(){
        if(touch[0] < unTouch[0]){
            currentFigure.updateCoordX(grille, EnumsRL.RIGHT)
        } else {
            currentFigure.updateCoordX(grille, EnumsRL.LEFT)
        }
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}


    // Permet de dessiner le jeu (appelé par le thread "GameDrawThread")
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        val SIZE  = ( (height / grille.height) * 3/4 ).toFloat()
        val CONSTY = (width - (SIZE * grille.width))/2
        val CONSTX = height - SIZE* grille.height

        // Tans que le joueur n'a pas perdu la partie
        if (canvas != null && !grille.isGameOver()) {
            canvas.drawColor(Color.WHITE)
            grille.draw(canvas, SIZE, CONSTX, CONSTY)
            currentFigure.draw(canvas, SIZE, CONSTX, CONSTY)
            nextFigure.draw(canvas, SIZE)
            saveFigure.draw(canvas, SIZE)
            highScore.draw(canvas)

            // Si la figure courante n'a pas touché le sol
        } else {
            // Dessine uniquement les fgigures qui composent la grille si le joueur a perdu sa partie
            grille.draw(canvas!!, SIZE, CONSTX, CONSTY)
        }
    }

    // Vérifie s'il faut save la figure et la save si c'est la cas
    fun save(){
        println("light : $lightSensor")
        if (!grille.isGameOver()) {
            if (!currentFigure.hasItGround(grille)) {
                currentFigure.changeColorLight(lightSensor)

                // Si la figure est save
                if(lightSensor<10 && !saveFigure.alreadySave){
                    saveFigure.addFigure(currentFigure)
                    currentFigure = nextFigure.figure
                    nextFigure.figure = RandomFigure.chooseFigure()
                }
            }
        }

    }

    // Permet de faire fonctionner le jeu (appelé par le thread "GameFallThread")
    fun fall() {
        if (!grille.isGameOver()) {
            if (!currentFigure.hasItGround(grille)) {
                currentFigure.updateCoordY()
            } else { // Si la figure courante a touché le sol

                grille.update(currentFigure) // Ajoute la figure courante à la grille
                currentFigure = nextFigure.figure // choisi une nouvelle figure
                nextFigure.figure = RandomFigure.chooseFigure()
                saveFigure.alreadySave = false
                val lineFull = grille.isLineFull() // Vérifie si une ligne de la grille est remplie
                if (lineFull.isNotEmpty()) {
                    grille.deletLines(lineFull) // supprime les lignes remplies si eil y en a
                    highScore.addScore(lineFull.size)
                }
            }
        }
    }

    //Utilise la figure sauvgardé
    fun useSaveFigure(){
        if (saveFigure.figure != null){
            currentFigure = saveFigure.figure!!
            saveFigure.figure = null
        }
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        gameDrawThread.setRunning(true)
        gameDrawThread.start()
        gameFallThread.setRunning(true)
        gameFallThread.start()
        gameSaveFigureThread.setRunning(true)
        gameSaveFigureThread.start()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                gameDrawThread.setRunning(false)
                gameDrawThread.join()
                gameFallThread.setRunning(false)
                gameFallThread.join()
                gameSaveFigureThread.setRunning(false)
                gameSaveFigureThread.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            retry = false
        }
    }
}