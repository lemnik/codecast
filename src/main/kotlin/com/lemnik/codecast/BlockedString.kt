package com.lemnik.codecast

import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.util.regex.Pattern

data class BlockedString(
        val content: String,
        val blocks: List<Pair<Int, Int>>
) {
    val blockCount = blocks.size
    val blockedLength = blocks.fold(0) { acc, pair -> acc + (pair.second - pair.first) }

    operator fun get(index: Int) = content.substring(blocks[index].first, blocks[index].second)
}

class BlockedStringBuilder {
    var index = 0

    val start: String
        get() = "%%{{$index}}%%"

    val end: String
        get() {
            val blockIndex = index
            index++
            return "%%{{end$blockIndex}}%%"
        }

    fun build(string: String): BlockedString {
        val pattern = Pattern.compile("%%\\{\\{((?:end)?\\d+)\\}\\}%%", Pattern.MULTILINE)
        val matcher = pattern.matcher(string)

        val blocks = ArrayList<Pair<Int, Int>>()
        val sbuilder = StringBuilder(string.length)

        var lastGroupEnd = 0
        while (matcher.find()) {
            sbuilder.append(string.substring(lastGroupEnd, matcher.start()))
            val index = matcher.group(1)
            val blockStart = matcher.end()
            val newBlockStart = sbuilder.length

            if (!matcher.find() || matcher.group(1) != "end$index") {
                throw IllegalArgumentException("not a valid blocked string, block $index is not closed (${matcher.group(1)})\n$string")
            }

            val blockEnd = matcher.start()
            sbuilder.append(string.substring(blockStart, blockEnd))
            val newBlockEnd = sbuilder.length

            blocks.add(newBlockStart to newBlockEnd)

            lastGroupEnd = matcher.end()
        }

        sbuilder.append(string.substring(lastGroupEnd))

        return BlockedString(sbuilder.toString(), blocks)
    }
}

fun code(block: BlockedStringBuilder.() -> String): BlockedString {
    val builder = BlockedStringBuilder()
    val result = builder.block()

    return builder.build(result)
}
