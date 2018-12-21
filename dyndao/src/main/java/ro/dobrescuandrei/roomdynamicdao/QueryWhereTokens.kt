package ro.dobrescuandrei.roomdynamicdao

import android.text.TextUtils
import java.util.*

class QueryWhereTokens : LinkedList<String>()
{
    companion object
    {
        fun queryWhereTokensFrom() : QueryWhereTokens = QueryWhereTokens()
    }

    fun addSearchTokens(search : String?, onColumns : Array<String>)
    {
        if (!TextUtils.isEmpty(search))
        {
            if (onColumns.size==1)
            {
                add(" ${onColumns[0]} like '%${SQLEscape.string(search?:"")}%' ")
            }
            else
            {
                val subtokens=QueryWhereTokens()
                for (columnName in onColumns)
                    subtokens.add(" $columnName like '%${SQLEscape.string(search?:"")}%' ")
                add("(${subtokens.or()})")
            }
        }
    }

    fun and () : String = build(delimiter = " and ")
    fun or  () : String = build(delimiter = " or ")

    private fun build(delimiter : String) : String =
        if (!isEmpty()) SQLEscape.tokens(this, delimitedBy = delimiter)
        else " 1==1 "
}