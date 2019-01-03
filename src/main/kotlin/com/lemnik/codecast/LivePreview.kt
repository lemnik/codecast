package com.lemnik.codecast

import java.awt.*
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.awt.image.BufferedImage

class LivePreview(private val frameSize: Dimension): Frame(), WindowListener {

    var buffer: BufferedImage? = null

    init{
        size = Dimension(frameSize.width / 2, frameSize.height / 2)
        location = Point(0, 0)
        isVisible = true
        title = "CodeCast"

        addWindowListener(this)
    }

    fun showPreview(image: BufferedImage) {
        buffer = image
        repaint()
    }

    override fun update(g: Graphics?) {
        paint(g)
    }

    override fun paint(g: Graphics?) {
        val localBuffer = buffer
        if(localBuffer == null || g == null || g !is Graphics2D)
            return

        val x = insets.left
        val y = insets.top
        val width = width - insets.left - insets.right
        val height = height - insets.top - insets.bottom

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        g.drawImage(localBuffer, x, y, width, height, this)
    }

    override fun windowClosing(e: WindowEvent?) {
        System.exit(0)
    }

    override fun windowDeiconified(e: WindowEvent?) {
    }

    override fun windowClosed(e: WindowEvent?) {
    }

    override fun windowActivated(e: WindowEvent?) {
    }

    override fun windowDeactivated(e: WindowEvent?) {
    }

    override fun windowOpened(e: WindowEvent?) {
    }

    override fun windowIconified(e: WindowEvent?) {
    }

}