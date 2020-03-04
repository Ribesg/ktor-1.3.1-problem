package com.example.app.http

object JsonPrettyPrinter {

    private val jsonFirstChars = arrayOf('{', '[')
    private const val indent = "  "

    fun maybePrettyPrint(maybeJson: String): String {
        val trimmed = maybeJson.trim()
        if (trimmed.isEmpty()) return maybeJson
        if (trimmed.first() !in jsonFirstChars) return maybeJson
        return try {
            StringBuilder().appendPrettyJson(trimmed).toString()
        } catch (t: Throwable) {
            println("Failed to pretty print json:\n$maybeJson")
            t.printStackTrace()
            maybeJson
        }
    }

    private fun StringBuilder.appendPrettyJson(src: String): StringBuilder {
        var inStringValue = false
        var indentCount = 0
        var currentIndent = ""
        src.forEachIndexed { i, c ->
            if (inStringValue) {
                append(
                    when (c) {
                        '\n' -> "\\n"
                        '\r' -> "\\r"
                        '\t' -> "\\t"
                        else -> c
                    }
                )
                if (c == '"' && src[i - 1] != '\\') {
                    inStringValue = false
                }
            } else if (!c.isWhitespace()) when (c) {
                '"' -> {
                    append(c)
                    inStringValue = true
                }
                '{', '[' -> {
                    append(c)
                    append('\n')
                    indentCount++
                    currentIndent = indent.repeat(indentCount)
                    append(currentIndent)
                }
                '}', ']' -> {
                    indentCount--
                    currentIndent = indent.repeat(indentCount)
                    append('\n')
                    append(currentIndent)
                    append(c)
                }
                ',' -> {
                    append(c)
                    append('\n')
                    append(currentIndent)
                }
                ':' -> {
                    append(' ')
                    append(c)
                    append(' ')
                }
                else -> {
                    append(c)
                }
            }
        }
        return this
    }

}
