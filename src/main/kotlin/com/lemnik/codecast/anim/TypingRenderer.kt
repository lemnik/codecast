package com.lemnik.codecast.anim

import com.lemnik.codecast.BlockedString
import com.lemnik.codecast.render.FrameRenderContext
import com.lemnik.codecast.render.RenderTheme
import java.awt.Rectangle
import kotlin.math.roundToInt

// FIXME: Handle more than one typing block
// FIXME: Treat blocks of whitespace as a single character

class TypingRenderer(val code: BlockedString, val bounds: Rectangle, val theme: RenderTheme): Animated {
    val start = code.blocks[0].first
    val end = code.blocks[0].second

    val prefixCode = code.content.substring(0, start)
    val codeToType = code.content.substring(start, end)
    val suffixCode = code.content.substring(end)

    override fun render(time: Double, ctx: FrameRenderContext) {
        val characterCount = time * codeToType.length.toDouble()
        val code = prefixCode + codeToType.substring(0, characterCount.roundToInt()) + suffixCode
        ctx.code(theme).render(code, bounds)
    }

    override fun toString(): String = "type[$code]"
}
