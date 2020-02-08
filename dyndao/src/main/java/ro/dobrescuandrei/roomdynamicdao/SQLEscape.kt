package ro.dobrescuandrei.roomdynamicdao

import android.database.DatabaseUtils

object SQLEscape
{
    @JvmStatic
    fun escapeString(string : String) : String
    {
        try
        {
            return DatabaseUtils.sqlEscapeString(string)
        }
        catch (ex : Exception)
        {
            val escaper=StringBuilder()
            escaper.append('\'')
            if (string.indexOf('\'') != -1)
            {
                for (character in string.toCharArray())
                {
                    if (character=='\'')
                        escaper.append('\'')
                    escaper.append(character)
                }

            }
            else escaper.append(string)
            escaper.append('\'')
            return escaper.toString()
        }
    }

    @JvmStatic
    fun escapeStringArray(values : Array<String>) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add(escapeString(token))
        return "(${tokens.joinToString(separator = ", ")})"
    }

    @JvmStatic
    fun escapeStringCollection(values : Collection<String>) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add(escapeString(token))
        return "(${tokens.joinToString(separator = ", ")})"
    }

    @JvmStatic
    fun escapeNumberArray(values : IntArray) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return "(${tokens.joinToString(separator = ", ")})"
    }

    @JvmStatic
    fun escapeNumberArray(values : LongArray) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return "(${tokens.joinToString(separator = ", ")})"
    }

    @JvmStatic
    fun escapeNumberArray(values : DoubleArray) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return "(${tokens.joinToString(separator = ", ")})"
    }

    @JvmStatic
    fun escapeNumberArray(values : FloatArray) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return "(${tokens.joinToString(separator = ", ")})"
    }

    @JvmStatic
    fun escapeNumberCollection(values : Collection<*>) : String
    {
        val tokens=mutableListOf<String>()
        for (token in values)
            tokens.add(token.toString())
        return "(${tokens.joinToString(separator = ", ")})"
    }

    @JvmStatic
    fun escapeBoolean(value : Boolean) : Int =
        if (value) 1 else 0
}