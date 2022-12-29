package ro.dobrescuandrei.roomdynamicdao

fun <FILTER : BaseFilter> FILTER.toQueryBuilder
(
    tableName : String,
    projection : QueryBuilder<FILTER>.(QueryProjectionClauses) -> (String) = { "*" },
    join : QueryBuilder<FILTER>.(QueryJoinClauses) -> (String?) = { null },
    where : QueryBuilder<FILTER>.(QueryWhereConditions) -> (String?) = { null },
    orderBy : QueryBuilder<FILTER>.() -> (String?) = { null },
    isPaginationEnabled : Boolean = QueryBuilderDefaults.isPaginationEnabled,
) : QueryBuilder<FILTER> =
    object : QueryBuilder<FILTER>(this)
    {
        override fun tableName() = tableName
        override fun projection(clauses : QueryProjectionClauses) : String = projection(this, clauses)
        override fun join(clauses : QueryJoinClauses) : String? = join(this, clauses)
        override fun where(conditions : QueryWhereConditions) : String? = where(this, conditions)
        override fun orderBy() : String? = orderBy(this)
        override fun isPaginationEnabled() : Boolean = isPaginationEnabled
    }
