package com.example.tetris.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build

class SaveFigure(var figure : Figure?) {

    // Permet de savoir si on a deja save la piece courante
    var alreadySave = false


    fun addFigure(figureAdd : Figure){
        figure = figureAdd
        alreadySave = true
        figure!!.resetCoord()
    }

    fun draw(canvas: Canvas?, SIZE : Float){
        if (canvas != null) {
            val paint = Paint()
            paint.textSize = 40F
            paint.color = Color.DKGRAY
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(canvas.width.toFloat()*1/4 + 40F, 20F, canvas.width.toFloat()*1/2 + 20F, canvas.height.toFloat()*1/6,10F, 10F, paint)
            }
            canvas.drawText("Save : ", canvas.width.toFloat()*1/3 , 60F, paint)
            drawSaveFigure(canvas, SIZE)

        }
    }

    // Dessine la figure en haut à gauche (pour faire comprendre aux joueurs que ce sera la prochaine qui apparaitra)
    private fun drawSaveFigure(canvas: Canvas?, sizeArg : Float){
        if (canvas != null && figure!=null) {
            val paint = Paint()
            val SIZE = sizeArg*3/4
            val CONSTX = canvas.width.toFloat()*1/4 + 80F
            val CONSTY = 80F
            for (i in 0 until figure!!.hitBox){
                for (j in 0 until figure!!.hitBox){
                    if( figure!!.blocs[i][j] != null){
                        figure!!.blocs[i][j]!!.color = figure!!.color
                        paint.color = figure!!.blocs[i][j]!!.color
                        canvas.drawRect(
                            j*SIZE + CONSTX,
                            i*SIZE +CONSTY,
                            j*SIZE + SIZE + CONSTX,
                            i*SIZE + SIZE +CONSTY,
                            paint)
                        paint.color = Color.argb(30,255,255, 255)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            canvas.drawRoundRect(
                                j*SIZE + SIZE*1/8 +CONSTX,
                                i*SIZE + SIZE*1/8 +CONSTY,
                                j*SIZE + SIZE - SIZE*1/8 +CONSTX,
                                i*SIZE + SIZE - SIZE*1/8 +CONSTY,
                                10F,
                                10F,
                                paint)
                        };
                    }
                }
            }
        }
    }
}