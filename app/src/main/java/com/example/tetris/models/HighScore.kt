package com.example.tetris.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class HighScore {
    var score = 0

    fun addScore(lineDestroy : Int){
        score = (10*lineDestroy) * lineDestroy
    }

    fun draw(canvas: Canvas?){
        if (canvas != null) {
            val paint = Paint()
            paint.textSize = 40F
            paint.color = Color.DKGRAY
            canvas.drawRect((canvas.width * 3/4).toFloat(), 0F, canvas.width.toFloat(), 200F, paint)
            canvas.drawText("SCORE : "+score, (canvas.width * 3/4).toFloat() + 40F, 100F, paint)

        }
    }
}