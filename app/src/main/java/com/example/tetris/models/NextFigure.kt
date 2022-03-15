package com.example.tetris.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build

class NextFigure(var figure : Figure) {

    fun draw(canvas: Canvas?, SIZE : Float){
        if (canvas != null) {
            val paint = Paint()
            paint.textSize = 40F
            paint.color = Color.DKGRAY
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(20F, 20F, canvas.width.toFloat()*1/4, canvas.height.toFloat()*1/6,10F, 10F, paint)
            }
            canvas.drawText("Suivant : ", 50F , 60F, paint)
            drawNextFigure(canvas, SIZE)

        }
    }

    // Dessine la figure en haut à gauche (pour faire comprendre aux joueurs que ce sera la prochaine qui apparaitra)
    private fun drawNextFigure(canvas: Canvas?, sizeArg : Float){
        if (canvas != null) {
            val paint = Paint()
            val SIZE = sizeArg*3/4
            val CONSTX = 60F
            val CONSTY = 80F
            for (i in 0 until figure.hitBox){
                for (j in 0 until figure.hitBox){
                    if( figure.blocs[i][j] != null){
                        figure.blocs[i][j]!!.color = figure.color
                        paint.color = figure.blocs[i][j]!!.color
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