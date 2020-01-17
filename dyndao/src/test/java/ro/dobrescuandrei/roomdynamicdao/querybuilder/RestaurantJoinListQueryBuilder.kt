package ro.dobrescuandrei.roomdynamicdao.querybuilder

import com.yatatsu.fieldschema.FS
import ro.dobrescuandrei.roomdynamicdao.QueryBuilder
import ro.dobrescuandrei.roomdynamicdao.QueryJoinClauses
import ro.dobrescuandrei.roomdynamicdao.QueryProjectionClauses
import ro.dobrescuandrei.roomdynamicdao.QueryWhereConditions
import ro.dobrescuandrei.roomdynamicdao.model.RestaurantFilter

class RestaurantJoinListQueryBuilder : QueryBuilder<RestaurantFilter>
{
    constructor(filter : RestaurantFilter) : super(filter)

    override fun tableName() = FS.Restaurant

    override fun where(conditions : QueryWhereConditions) = "1=1"

    override fun projection(clauses : QueryProjectionClauses): String
    {
        clauses.addAllFieldsFromTable(FS.Restaurant)

        clauses.addField(
            FS.City_name,
            fromTable = FS.City,
            projectAs = FS.RestaurantJoin_cityName)

        clauses.addField(
            FS.Country_name,
            fromTable = FS.Country,
            projectAs = FS.RestaurantJoin_countryName)

        return clauses.merge()
    }

    override fun join(clauses : QueryJoinClauses) : String?
    {
        clauses.addInnerJoin(
            table = FS.Restaurant,
            column = FS.Restaurant_cityId,
            remoteTable = FS.City,
            remoteColumn = FS.City_id)

        clauses.addInnerJoin(
            table = FS.City,
            column = FS.City_countryId,
            remoteTable = FS.Country,
            remoteColumn = FS.Country_id)

        return clauses.merge()
    }

    override fun orderBy() = "${FS.Restaurant_id} asc"
}
