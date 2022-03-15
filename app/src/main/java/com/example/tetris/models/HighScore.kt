package com.example.tetris.models

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build

class HighScore {
    var score = 0

    fun addScore(lineDestroy : Int){
        score = (10*lineDestroy) * lineDestroy
    }

    fun draw(canvas: Canvas?){
        if (canvas != null) {
            val paint = Paint()
            paint.textSize = 35F
            paint.color = Color.DKGRAY
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect((canvas.width * 3/4).toFloat(), 20F, canvas.width.toFloat() -20F, 200F,10F, 10F, paint)
            }
            canvas.drawText("SCORE : "+score, (canvas.width * 3/4).toFloat() +20F , 120F, paint)

        }
    }
}