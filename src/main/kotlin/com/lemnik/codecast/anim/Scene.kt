package com.lemnik.codecast.anim

import com.lemnik.codecast.BlockedString
import com.lemnik.codecast.BlockedStringBuilder
import com.lemnik.codecast.render.FrameRenderContext
import com.lemnik.codecast.render.RenderTheme
import com.lemnik.codecast.render.boundsOf
import javafx.animation.Interpolator
import javafx.util.Duration
import java.awt.Color
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.image.BufferedImage
import kotlin.math.roundToInt

class Scene(
        val elements: Array<Element>,
        val screenSize: Dimension,
        val fps: Int
) {
    val msPerFrame = (1000.0 / fps.toDouble())
    val endTime = elements.map { it.endTime }.max()!!
    val frameCount = (endTime.toSeconds() * fps.toDouble()).roundToInt()

    fun animate() = sequence {
        (0..frameCount).forEach { yield(renderFrame(it)) }
    }

    fun renderFrame(frame: Int): BufferedImage {
        val timeMs = msPerFrame * frame.toDouble()
        val elapsed = Duration.millis(timeMs)

        val buffer = BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_RGB)
        val renderer = FrameRenderContext(buffer.createGraphics(), screenSize)
        renderer.clear()

        elements.filter { elapsed >= it.startTime && (elapsed <= it.endTime || it.persistent) }.forEach { element ->
            val fraction = ((elapsed.toMillis() - element.startTime.toMillis()) / element.duration.toMillis()).coerceIn(0.0, 1.0)
            //println("(${elapsed.toMillis()} - ${element.startTime.toMillis()}) / ${element.duration.toMillis()}")
            val time = element.interpolator.interpolate(0.0, 1.0, fraction)
            element.animated.render(time, renderer)
        }

        return buffer
    }

    data class Element(
            val duration: Duration,
            val animated: Animated,
            val interpolator: Interpolator = Interpolator.LINEAR,
            val startTime: Duration = Duration.ZERO,
            val persistent: Boolean = false
    ) {
        val endTime
            get() = startTime.add(duration)
    }

}

private object NoopAnimated : Animated {
    override fun render(time: Double, ctx: FrameRenderContext) {
    }
}

class SceneBuilder(val theme: RenderTheme, val screenSize: Dimension, val fps: Int) {
    val elements = ArrayList<Scene.Element>()
    var clock = Duration.ZERO

    fun type(
            duration: Duration = Duration.seconds(10.0),
            startTime: Duration = clock,
            bounds: Rectangle = Rectangle(0, 0, screenSize.width, screenSize.height),
            theme: RenderTheme = this.theme,
            tick: Boolean = true,
            persistent: Boolean = false,
            interpolator: Interpolator = Interpolator.LINEAR,
            block: BlockedStringBuilder.() -> String) {

        val blockedStringBuilder = BlockedStringBuilder()
        val code = blockedStringBuilder.block()
        val blockedCode = blockedStringBuilder.build(code)

        elements.add(Scene.Element(
                duration,
                TypingRenderer(blockedCode, bounds, theme),
                startTime = startTime,
                persistent = persistent,
                interpolator = interpolator
        ))

        if (tick) {
            clock = clock.add(duration)
        }
    }

    fun code(
            code: String,
            duration: Duration = Duration.seconds(10.0),
            startTime: Duration = clock,
            bounds: Rectangle = Rectangle(0, 0, screenSize.width, screenSize.height),
            theme: RenderTheme = this.theme
    ) {
        elements.add(Scene.Element(
                duration,
                CodeRenderer(code, bounds, theme),
                startTime = startTime
        ))
    }

    fun rect(
            color: Color,
            start: Rectangle,
            end: Rectangle = start,
            duration: Duration = Duration.seconds(10.0),
            startTime: Duration = clock,
            tick: Boolean = false,
            persistent: Boolean = false,
            interpolator: Interpolator = Interpolator.LINEAR
    ) {
        elements.add(Scene.Element(
                duration,
                RectangleRenderer(color, start, end),
                interpolator,
                startTime,
                persistent
        ))

        if (tick) {
            clock = clock.add(duration)
        }
    }

    fun rect(
            color: Color,
            duration: Duration = Duration.seconds(10.0),
            startTime: Duration = clock,
            bounds: Rectangle = Rectangle(0, 0, screenSize.width, screenSize.height),
            tick: Boolean = false,
            persistent: Boolean = false,
            interpolator: Interpolator = Interpolator.LINEAR,
            block: BlockedStringBuilder.() -> String
    ) {
        val blockedStringBuilder = BlockedStringBuilder()
        val code = blockedStringBuilder.block()
        val blockedCode = blockedStringBuilder.build(code)
        rect(color, blockedCode, duration, startTime, bounds, tick, persistent, interpolator)
    }

    fun rect(
            color: Color,
            blockedCode: BlockedString,
            duration: Duration = Duration.seconds(10.0),
            startTime: Duration = clock,
            bounds: Rectangle = Rectangle(0, 0, screenSize.width, screenSize.height),
            tick: Boolean = false,
            persistent: Boolean = false,
            interpolator: Interpolator = Interpolator.LINEAR
    ) {
        val (start, end) = theme.boundsOf(blockedCode, bounds.x, bounds.y)

        elements.add(Scene.Element(
                duration,
                RectangleRenderer(color, start, end),
                interpolator,
                startTime,
                persistent
        ))

        if (tick) {
            clock = clock.add(duration)
        }
    }

    fun delay(duration: Duration) {
        elements.add(Scene.Element(
                duration,
                NoopAnimated,
                startTime = clock
        ))

        clock = clock.add(duration)
    }

    fun build() = Scene(elements.toTypedArray(), screenSize, fps)
}

class AnimationBuilder(
        val theme: RenderTheme,
        val screenSize: Dimension,
        val fps: Int
) {
    val scenes = ArrayList<Scene>()
    fun build() = scenes.toTypedArray()
}

fun animation(
        theme: RenderTheme,
        screenSize: Dimension = Dimension(1920, 1080),
        fps: Int = 25,
        block: AnimationBuilder.() -> Unit
): Array<Scene> {

    val builder = AnimationBuilder(theme, screenSize, fps)
    builder.block()
    return builder.build()
}

fun AnimationBuilder.scene(block: SceneBuilder.() -> Unit) {
    val builder = SceneBuilder(theme, screenSize, fps)
    builder.block()
    scenes.add(builder.build())
}
