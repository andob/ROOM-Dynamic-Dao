package ro.dobrescuandrei.roomdynamicdao

import android.database.DatabaseUtils

internal object SQLEscape
{
    fun escapeString(string : String) : String
    {
        return DatabaseUtils.sqlEscapeString(string)
    }

    fun escapeStringArray(values : Array<String>) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add(escapeString(token))
        return "(${mergeTokens(tokens, delimitedBy = ",")})"
    }

    fun escapeStringCollection(values : Collection<String>) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add(escapeString(token))
        return "(${mergeTokens(tokens, delimitedBy = ",")})"
    }

    fun escapeNumberArray(values : IntArray) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return "(${mergeTokens(tokens, delimitedBy = ",")})"
    }

    fun escapeNumberArray(values : LongArray) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return "(${mergeTokens(tokens, delimitedBy = ",")})"
    }

    fun escapeNumberArray(values : DoubleArray) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return "(${mergeTokens(tokens, delimitedBy = ",")})"
    }

    fun escapeNumberArray(values : FloatArray) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return "(${mergeTokens(tokens, delimitedBy = ",")})"
    }

    fun escapeNumberCollection(values : Collection<*>) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add(token.toString())
        return "(${mergeTokens(tokens, delimitedBy = ",")})"
    }

    fun escapeBoolean(value : Boolean) : Int =
        if (value) 1 else 0

    fun mergeTokens(tokens : List<String>, delimitedBy : String) : String
    {
        val stringBuilder=StringBuilder()
        val length=tokens.size

        for (i in 0 until length)
        {
            stringBuilder.append(tokens[i])

            if (i!=length-1)
                stringBuilder.append(" $delimitedBy ")
        }

        return stringBuilder.toString()
    }
}