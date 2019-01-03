package com.lemnik.codecast

import com.lemnik.codecast.anim.Scene
import com.lemnik.codecast.scripts.animScript
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import org.jcodec.api.awt.AWTSequenceEncoder
import org.jcodec.common.io.FileChannelWrapper
import org.jcodec.common.io.NIOUtils
import org.jcodec.common.model.Rational
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.PriorityBlockingQueue

class Args(parser: ArgParser) {
    val threads by parser.storing(
            "-T", "--threads",
            help = "enable threaded rendering"
    ) { toInt() }.default(1)

    val output by parser.storing(
            "-O", "--output",
            help = "output filename"
    ).default("")

    val preview by parser.flagging(
            "-P", "--preview",
            help = "preview only, don't write a video"
    ).default(false)

    val script by parser.positional(
            "SCRIPT",
            help = "animation script"
    )

}

interface SceneRenderer {
    fun renderScene(scene: Scene, encoder: AWTSequenceEncoder)
}

class InlineSceneRenderer(screenSize: Dimension) : SceneRenderer {
    val preview = LivePreview(screenSize)

    override fun renderScene(scene: Scene, encoder: AWTSequenceEncoder) {
        scene.animate().forEach { frame ->
            preview.showPreview(frame)
            encoder.encodeImage(frame)
        }
    }
}

class ThreadedSceneRenderer(val threadCount: Int) : SceneRenderer {
    val pool = Executors.newFixedThreadPool(threadCount)

    init {
        println("Rendering on $threadCount threads...")
    }

    override fun renderScene(scene: Scene, encoder: AWTSequenceEncoder) {
        val frameCount = scene.frameCount
        val queue = PriorityBlockingQueue<Frame>(threadCount * 2)

        (0..frameCount).forEach { index ->
            pool.submit {
                queue.add(Frame(index, scene.renderFrame(index)))
            }
        }

        var index = 0
        while (index < frameCount) {
            val frame = queue.take()
            if (frame.frameIndex != index) {
                queue.add(frame)
            } else {
                encoder.encodeImage(frame.image)
                index++
            }
        }
    }

    class Frame(val frameIndex: Int, val image: BufferedImage) : Comparable<Frame> {
        override fun compareTo(other: Frame): Int = frameIndex - other.frameIndex
    }
}

fun main(rawArgs: Array<String>) = ArgParser(rawArgs).parseInto(::Args).run {
    val script = File(script)
    val output = output.takeUnless { it.isNullOrBlank() }?.let { File(it) }
            ?: File(script.parentFile, "${script.name.substringBefore('.')}.mp4")

    val animation = animScript()

    val startTime = System.currentTimeMillis()

    if (!preview) {
        val renderer = if (threads <= 1) InlineSceneRenderer(Dimension(1920, 1080)) else ThreadedSceneRenderer(threads)

        NIOUtils.writableFileChannel(output.absolutePath).use { channel ->
            val encoder = AWTSequenceEncoder(channel, Rational.R(25, 1))
            animation.forEach { scene ->
                renderer.renderScene(scene, encoder)
            }

            encoder.finish()
        }
    } else {
        val preview = LivePreview(Dimension(1920, 1080))

        animation.forEach { scene ->
            scene.animate().forEach { frame ->
                preview.showPreview(frame)

                Thread.sleep(33)
            }
        }
    }

    println("Time taken: ${System.currentTimeMillis() - startTime}ms")

    System.exit(0)
}