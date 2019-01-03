package com.lemnik.codecast.render

import java.awt.*
import java.awt.image.BufferedImage

class FrameRenderContext(val graphics: Graphics2D, val screenSize: Dimension) {

    constructor(image: BufferedImage) : this(image.createGraphics(), Dimension(image.width, image.height))

    init {
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    }

    fun clear() {
        graphics.color = Color.WHITE
        graphics.fillRect(0, 0, screenSize.width, screenSize.height)
    }

    fun code(theme: RenderTheme): RenderCode = RenderCode(graphics, theme)

}