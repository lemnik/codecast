package com.lemnik.codecast.anim

import com.lemnik.codecast.render.FrameRenderContext

interface Animated {
    fun render(time: Double, ctx: FrameRenderContext)
}