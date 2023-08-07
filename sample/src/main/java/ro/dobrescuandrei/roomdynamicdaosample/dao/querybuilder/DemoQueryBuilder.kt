package ro.dobrescuandrei.roomdynamicdaosample.dao.querybuilder

import com.yatatsu.fieldschema.FS
import ro.dobrescuandrei.roomdynamicdao.*
import ro.dobrescuandrei.roomdynamicdaosample.model.RestaurantFilter

class DemoQueryBuilder : QueryBuilder<RestaurantFilter>
{
    constructor(filter : RestaurantFilter) : super(filter)

    //used to specify table name
    override fun tableName() = FS.Restaurant

    //used to specify where conditions
    override fun where(conditions : QueryWhereConditions) : String?
    {
        conditions.add("${FS.Restaurant_id} in ${intArrayOf(1, 5).sqlEscaped}")
        conditions.add("${FS.Restaurant_id} = 1")
        conditions.add("${FS.Restaurant_rating} = 5")
        return conditions.mergeWithAnd()
    }

    override fun isPaginationEnabled() = true

    //column projections
    override fun projection(clauses : QueryProjectionClauses) : String
    {
        clauses.addAllFieldsFromTable(FS.Restaurant)

        clauses.addField(FS.City_name,
            fromTable = FS.City,
            projectAs = FS.RestaurantJoin_cityName)

        clauses.addField(FS.Country_name,
            fromTable = FS.Country,
            projectAs = FS.RestaurantJoin_countryName)

        return clauses.merge()
    }

    //use for joins
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

    //specify order by
    override fun orderBy() : String? = "${FS.Restaurant_id} asc"
}