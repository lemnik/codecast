package com.lemnik.codecast.render

import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.font.FontRenderContext
import java.awt.image.BufferedImage

val DEFAULT_FONT = Font(Font.MONOSPACED, Font.PLAIN, 24)

data class FontStyle(
        val color: Color,
        val font: Font = DEFAULT_FONT,
        val underline: Boolean = false
)

class RenderTheme(
        val defaultStyle: FontStyle = FontStyle(Color.BLACK),
        val languageParts: Map<String, String>,
        val partStyles: Map<String, FontStyle>
) {
    val fontMetrics: FontMetrics by lazy {
        BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB).createGraphics().getFontMetrics(defaultStyle.font)!!
    }

    val lineHeight
        get() = fontMetrics.height

    val charWidth
        get() = fontMetrics.charWidth(' ')

    fun stringWidth(string: String) =
            fontMetrics.stringWidth(string)

    fun styleFor(word: String): FontStyle =
            partStyles[languageParts[word]] ?: defaultStyle

    fun derive(parts: Map<String, String>) =
            RenderTheme(
                    defaultStyle,
                    languageParts + parts,
                    partStyles
            )
}