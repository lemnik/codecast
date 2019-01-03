package com.lemnik.codecast.scripts

import com.lemnik.codecast.anim.*
import com.lemnik.codecast.themes.DEFAULT_HIGHLIGHT
import com.lemnik.codecast.themes.JAVA_THEME
import javafx.animation.Interpolator
import javafx.util.Duration

val SNIPPET2 =
"""
import java.awt.*;

public class MySpecialClass extends Frame {
    public static void main(final String[] args) {
        var something = new MySpecialClass();
        something.setSize(640, 480);
    }
}
""".trimIndent()

val SNIPPET3 =
        """
import java.awt.*;

public class MySpecialClass extends Frame {
    public static void main(final String[] args) {
        var something = new MySpecialClass();
        something.setSize(640, 480);
        something.setVisible(true);
    }
}
""".trimIndent()

fun animScript(): Array<Scene> = animation(JAVA_THEME) {
    /*scene {
        type(Duration.seconds(5.0), persistent = true) {
"""
    import java.awt.*;

    public class MySpecialClass extends Frame {
        ${TYPE_START}static void main(final String[] args) {
        }${TYPE_END}
    }
""".trimIndent().replace("    ", "\t")
        }

        delay(Duration.seconds(5.0))
    }*/

    scene {
        rect(DEFAULT_HIGHLIGHT, Duration.seconds(4.0), persistent = true) {
"""
import java.awt.*;

public$ class MySpecialClass extends Frame {
    public static void main(final String[] args) {
        ${start}${end}${start}var something = new MySpecialClass();${end}
    }
}
""".trimIndent()
        }

        type(Duration.seconds(3.0), persistent = true, interpolator = Interpolator.EASE_BOTH) {
"""
import java.awt.*;

public class MySpecialClass extends Frame {
    public static void main(final String[] args) {
        ${start}var something = new MySpecialClass();${end}
    }
}
""".trimIndent()
        }

        delay(Duration.seconds(3.0))
    }

    scene {
        rect(DEFAULT_HIGHLIGHT, Duration.seconds(0.35), persistent = true) {
            """
import java.awt.*;

public$ class MySpecialClass extends Frame {
    public static void main(final String[] args) {
        ${start}var something = new MySpecialClass();${end}
        ${start}something.setSize(640, 480);${end}
    }
}
""".trimIndent()
        }

        code(SNIPPET2, duration = Duration.seconds(2.0))
    }

    scene {
        rect(DEFAULT_HIGHLIGHT, Duration.seconds(0.35), persistent = true) {
            """
import java.awt.*;

public$ class MySpecialClass extends Frame {
    public static void main(final String[] args) {
        var something = new MySpecialClass();
        ${start}something.setSize(640, 480);${end}
        ${start}something.setVisible(true);${end}
    }
}
""".trimIndent()
        }

        code(SNIPPET3, duration = Duration.seconds(5.0))
    }
}