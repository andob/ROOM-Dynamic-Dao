package ro.dobrescuandrei.roomdynamicdao

import android.text.TextUtils
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import ro.andreidobrescu.basefilter.BaseFilter

abstract class QueryBuilder<FILTER : BaseFilter>
(
    val filter : FILTER
)
{
    fun build() : SupportSQLiteQuery
    {
        var sql = "select ${projection(QueryProjectionTokens())} from ${tableName()} "

        join(QueryJoinTokens())?.let { join ->
            if (!TextUtils.isEmpty(join))
                sql+=join
        }

        where(QueryWhereTokens())?.let { where ->
            if (!TextUtils.isEmpty(where))
                sql+=" where $where "
        }

        orderBy()?.let { order ->
            if (!TextUtils.isEmpty(order))
                sql+=" order by $order "
        }

        if (enablePagination())
        {
            sql+=" limit ${filter.limit} "
            sql+=" offset ${filter.offset} "
        }

        return SimpleSQLiteQuery(sql)
    }

    abstract fun tableName() : String?
    open fun projection(tokens : QueryProjectionTokens) = "*"
    open fun join(tokens : QueryJoinTokens) : String? = null
    abstract fun where(tokens : QueryWhereTokens) : String?
    open fun orderBy() : String? = null
    open fun enablePagination() : Boolean = false

    val String.sqlEscaped get() = SQLEscape.string(this)
    val IntArray.sqlEscaped get() = SQLEscape.numberArray(this)
    val LongArray.sqlEscaped get() = SQLEscape.numberArray(this)
    val DoubleArray.sqlEscaped get() = SQLEscape.numberArray(this)
    val FloatArray.sqlEscaped get() = SQLEscape.numberArray(this)
    val Boolean.sqlEscaped get() = SQLEscape.boolean(this)
}
