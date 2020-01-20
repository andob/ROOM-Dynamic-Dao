package ro.dobrescuandrei.roomdynamicdao

import ro.andreidobrescu.basefilter.BaseFilter

fun <FILTER : BaseFilter> newQueryBuilder(
    tableName : String,
    projection : (QueryProjectionClauses) -> (String) = { "*" },
    join : (QueryJoinClauses) -> (String?) = { null },
    where : (QueryWhereConditions) -> (String?) = { null },
    orderBy : () -> (String?) = { null },
    isPaginationEnabled : Boolean = QueryBuilderDefaults.isPaginationEnabled
) : QueryBuilder<BaseFilter> =
    object : QueryBuilder<BaseFilter>(object : BaseFilter() {})
    {
        override fun tableName() = tableName
        override fun projection(clauses : QueryProjectionClauses) : String = projection(clauses)
        override fun join(clauses : QueryJoinClauses) : String? = join(clauses)
        override fun where(conditions : QueryWhereConditions) : String? = where(conditions)
        override fun orderBy() : String? = orderBy()
        override fun isPaginationEnabled() : Boolean = isPaginationEnabled
    }
