package com.lemnik.codecast.render

import com.lemnik.codecast.BlockedString
import java.awt.Rectangle

fun RenderTheme.boundsOf(s: BlockedString, x: Int, y: Int) = s.blocks.map { (start, end) ->
    val prefix = s.content.substring(0, start)
    val block = s.content.substring(start, end)

    val prefixLines = prefix.count { it == '\n' }
    val linePrefix = prefix.substringAfterLast('\n')
    val blockLines = 1 + block.count { it == '\n' }
    val blockLength = block.split('\n').maxBy { it.length }!!.length

    return@map Rectangle(
            (x + stringWidth(linePrefix)).toInt(),
            y + (lineHeight * prefixLines).toInt(),
            (charWidth * blockLength).toInt(),
            (blockLines * lineHeight).toInt()
    )
}