package com.lemnik.codecast.anim

import com.lemnik.codecast.render.FrameRenderContext
import com.lemnik.codecast.render.RenderTheme
import java.awt.Rectangle

class CodeRenderer(val code: String, val bounds: Rectangle, val theme: RenderTheme): Animated {
    override fun render(time: Double, ctx: FrameRenderContext) {
        ctx.code(theme).render(code, bounds)
    }
}