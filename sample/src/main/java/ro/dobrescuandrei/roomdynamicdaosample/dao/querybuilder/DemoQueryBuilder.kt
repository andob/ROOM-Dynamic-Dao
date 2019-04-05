package ro.dobrescuandrei.roomdynamicdaosample.dao.querybuilder

import com.yatatsu.fieldschema.FS
import ro.dobrescuandrei.roomdynamicdao.*
import ro.dobrescuandrei.roomdynamicdaosample.model.RestaurantFilter

class DemoQueryBuilder : QueryBuilder<RestaurantFilter>
{
    constructor(filter: RestaurantFilter) : super(filter)

    //used to specify table name
    override fun tableName(): String? = FS.Restaurant

    //used to specify where conditions
    override fun where(tokens: QueryWhereTokens) : String?
    {
        val tokens=QueryWhereTokens()
        tokens.add("${FS.Restaurant_id} in (${SQLEscape.numberArray(intArrayOf(1, 5))})")
        tokens.add("${FS.Restaurant_id} = 1")
        tokens.add("${FS.Restaurant_rating} = 5")
        return tokens.and()
    }

    //enable pagination. By default, pagination is disabled
    override fun enablePagination() : Boolean = true

    //column projections
    override fun projection(tokens: QueryProjectionTokens): String
    {
        tokens.allFieldsFromTable(FS.Restaurant)

        tokens.field(FS.City_name,
            fromTable = FS.City,
            projectAs = FS.RestaurantJoin_cityName)

        tokens.field(FS.Country_name,
            fromTable = FS.Country,
            projectAs = FS.RestaurantJoin_countryName)

        return tokens.build()
    }

    //use for joins
    override fun join(tokens: QueryJoinTokens) : String?
    {
        tokens.innerJoin(
            table = FS.Restaurant,
            column = FS.Restaurant_cityId,
            remoteTable = FS.City,
            remoteColumn = FS.City_id)

        tokens.innerJoin(
            table = FS.City,
            column = FS.City_countryId,
            remoteTable = FS.Country,
            remoteColumn = FS.Country_id)

        return tokens.build()
    }

    //specify order by
    override fun orderBy(): String? = "${FS.Restaurant_id} asc"
}