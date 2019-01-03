package com.lemnik.codecast.themes

import com.lemnik.codecast.render.DEFAULT_FONT
import com.lemnik.codecast.render.FontStyle
import com.lemnik.codecast.render.RenderTheme
import java.awt.Color
import java.awt.Font

val JAVA_THEME = RenderTheme(
        languageParts = mapOf(
                "package" to "struct",
                "import" to "struct",
                "class" to "struct",
                "interface" to "struct",
                "extends" to "struct",
                "implements" to "struct",
                "public" to "struct",
                "protected" to "struct",
                "private" to "struct",
                "static" to "struct",
                "final" to "struct",
                "var" to "struct",
                "new" to "struct",

                "\"" to "string",
                "\'" to "string",

                "byte" to "primitive",
                "short" to "primitive",
                "int" to "primitive",
                "long" to "primitive",
                "boolean" to "primitive",
                "double" to "primitive",
                "float" to "primitive",
                "void" to "value",

                "true" to "value",
                "false" to "value"
        ),
        partStyles = mapOf(
                "struct" to FontStyle(Color(0x0032BB), DEFAULT_FONT.deriveFont(Font.BOLD)),
                "string" to FontStyle(Color(0x00008036), DEFAULT_FONT.deriveFont(Font.BOLD)),
                "number" to FontStyle(Color(0x002F4B74), DEFAULT_FONT),
                "value" to FontStyle(Color(0x002F4B74), DEFAULT_FONT)
        )
)