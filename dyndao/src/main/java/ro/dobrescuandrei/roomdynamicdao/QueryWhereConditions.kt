package ro.dobrescuandrei.roomdynamicdao

import java.util.*

class QueryWhereConditions : LinkedList<String>()
{
    fun addSearchConditions(search : String?, onColumns : Array<String>)
    {
        if (search!=null&&search.isNotEmpty())
        {
            val escapedSearch=SQLEscape.escapeString(search)
            val unquotedEscapedSearch=escapedSearch.substring(1, escapedSearch.length-1)
            val likeArgument="'%$unquotedEscapedSearch%'"

            if (onColumns.size==1)
                add(" ${onColumns[0]} like $likeArgument ")
            else
            {
                val subcondition=QueryWhereConditions()
                for (columnName in onColumns)
                    subcondition.add(" $columnName like $likeArgument ")
                add("(${subcondition.mergeWithOr()})")
            }
        }
    }

    fun mergeWithAnd() =
        if (!isEmpty())
            this.joinToString(separator = " and ")
        else " 1==1 "

    fun mergeWithOr() =
        if (!isEmpty())
            this.joinToString(separator = " or ")
        else " 1==1 "
}