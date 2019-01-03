package com.lemnik.codecast.anim

import com.lemnik.codecast.render.FrameRenderContext
import java.awt.Color
import java.awt.Rectangle
import kotlin.math.roundToInt

class RectangleRenderer(val color: Color, val start: Rectangle, val end: Rectangle = start) : Animated {

    fun interpolate(startValue: Double, endValue: Double, fraction: Double)
            = startValue + (endValue - startValue) * fraction

    override fun render(time: Double, ctx: FrameRenderContext) {
        val dx = interpolate(start.x.toDouble(), end.x.toDouble(), time)
        val dy = interpolate(start.y.toDouble(), end.y.toDouble(), time)
        val dw = interpolate(start.width.toDouble(), end.width.toDouble(), time)
        val dh = interpolate(start.height.toDouble(), end.height.toDouble(), time)

        ctx.graphics.color = color
        ctx.graphics.fillRect(dx.roundToInt(), dy.roundToInt(), dw.roundToInt(), dh.roundToInt())
    }

}