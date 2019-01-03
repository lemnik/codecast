package com.lemnik.codecast.render

import com.lemnik.codecast.BlockedString
import java.awt.Graphics2D
import java.awt.Rectangle
import java.util.*

class RenderCode(val graphics: Graphics2D, val theme: RenderTheme) {

    private val metrics = graphics.getFontMetrics(theme.defaultStyle.font)
    private val tabWidth = metrics.stringWidth("    ")

    fun render(source: String, bounds: Rectangle = Rectangle(0, 0, 1920, 1080), lineHighlighter: (Int, Graphics2D, Rectangle) -> Unit = { _, _, _ -> }) =
            render(source.split("\n"), bounds, lineHighlighter)

    fun render(lines: List<String>, bounds: Rectangle = Rectangle(0, 0, 1920, 1080), lineHighlighter: (Int, Graphics2D, Rectangle) -> Unit = { _, _, _ -> }) {
        lines.forEachIndexed { index, s ->
            val y = bounds.y + (metrics.height * index)
            val x = bounds.x
            val width = bounds.width
            val height = metrics.height

            lineHighlighter(index, graphics, Rectangle(x, y, width, height))
            renderLine(s, x, y, width, height)
        }
    }

    fun renderLine(s: String, x: Int, y: Int, width: Int, height: Int) {
        graphics.setClip(x, y, width, height)

        var xcursor = x
        var inString = false
        val tokenizer = StringTokenizer(s, " \t.,;:'\"[]{}()", true)
        while (tokenizer.hasMoreTokens()) {
            val token = tokenizer.nextToken()

            if (token == "\"" || token == "\'") {
                inString = !inString
            }

            if (token == " ") {
                xcursor += metrics.charWidth(' ')
            } else if (token == "\t") {
                xcursor += tabWidth
            } else {
                val style =
                        if (inString) theme.partStyles["string"]!!
                        else if(token.toIntOrNull() != null) theme.partStyles["number"]!!
                        else theme.styleFor(token)

                val styleMetrics = graphics.getFontMetrics(style.font)
                graphics.color = style.color
                graphics.font = style.font

                if (style.underline) {
                    graphics.drawLine(xcursor, y + styleMetrics.ascent, xcursor + styleMetrics.stringWidth(token), y + styleMetrics.ascent)
                }

                graphics.drawString(token, xcursor, y + styleMetrics.ascent)

                xcursor += styleMetrics.stringWidth(token)
            }
        }
    }

}