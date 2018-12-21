package ro.dobrescuandrei.roomdynamicdao

import android.database.DatabaseUtils

object SQLEscape
{
    fun string(string : String) : String
    {
        val escaped= DatabaseUtils.sqlEscapeString(string).substring(1)
        return escaped.substring(0, escaped.length-1)
    }

    fun stringArray(values : Array<String>) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add("\"${string(token)}\"")
        return tokens(tokens, delimitedBy = ",")
    }

    fun stringCollection(values : Collection<String>) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add("\"${string(token)}\"")
        return tokens(tokens, delimitedBy = ",")
    }

    fun numberArray(values : IntArray) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return tokens(tokens, delimitedBy = ",")
    }

    fun numberArray(values : LongArray) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return tokens(tokens, delimitedBy = ",")
    }

    fun numberArray(values : DoubleArray) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return tokens(tokens, delimitedBy = ",")
    }

    fun numberArray(values : FloatArray) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return tokens(tokens, delimitedBy = ",")
    }

    fun numberCollection(values : Collection<*>) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add(token.toString())
        return tokens(tokens, delimitedBy = ",")
    }

    fun tokens(tokens : List<String>, delimitedBy : String) : String
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