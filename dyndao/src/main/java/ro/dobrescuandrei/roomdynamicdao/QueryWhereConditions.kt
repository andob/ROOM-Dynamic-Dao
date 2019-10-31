package ro.dobrescuandrei.roomdynamicdao

import android.text.TextUtils
import java.util.*

class QueryWhereConditions : LinkedList<String>()
{
    fun addSearchConditions(search : String?, onColumns : Array<String>)
    {
        if (search!=null&&!TextUtils.isEmpty(search))
        {
            val likeArgument="'%${
                SQLEscape.escapeString(search)
                    .trimStart('\'')
                    .trimEnd('\'')
            }%'"

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
            SQLEscape.mergeTokens(this, delimitedBy = " and ")
        else " 1==1 "

    fun mergeWithOr() =
        if (!isEmpty())
            SQLEscape.mergeTokens(this, delimitedBy = " or ")
        else " 1==1 "
}