package ro.dobrescuandrei.roomdynamicdao

import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.db.SupportSQLiteQuery
import android.text.TextUtils

abstract class QueryBuilder<FILTER>
(
    val search: String?,
    val filter: FILTER?,
    val limit: Int = Int.MAX_VALUE,
    val offset: Int = 0
)
{
    fun build(): SupportSQLiteQuery
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
            sql+=" limit $limit "
            sql+=" offset $offset "
        }

        return SimpleSQLiteQuery(sql)
    }

    open fun orderBy(): String? = null
    open fun enablePagination() : Boolean = false
    open fun projection(tokens : QueryProjectionTokens) = "*"
    open fun join(tokens : QueryJoinTokens) : String? = null
    abstract fun tableName(): String?
    abstract fun where(tokens: QueryWhereTokens): String?
}