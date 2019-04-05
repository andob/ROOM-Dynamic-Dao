package ro.dobrescuandrei.roomdynamicdaosample.dao.querybuilder

import com.yatatsu.fieldschema.FS
import ro.dobrescuandrei.roomdynamicdao.QueryBuilder
import ro.dobrescuandrei.roomdynamicdao.QueryWhereTokens
import ro.dobrescuandrei.roomdynamicdaosample.model.RestaurantFilter

class RestaurantListQueryBuilder : QueryBuilder<RestaurantFilter>
{
    constructor(filter: RestaurantFilter) : super(filter)

    override fun tableName(): String? = FS.Restaurant

    override fun where(tokens: QueryWhereTokens): String?
    {
        if (filter.search!=null)
            tokens.addSearchTokens(filter.search, onColumns = arrayOf(FS.Restaurant_name))

        if (filter.rating!=null)
            tokens.add("${FS.Restaurant_rating} = ${filter.rating}")

        if (filter.boundingBox!=null)
        {
            tokens.add("${FS.Restaurant_latitude}  <= ${filter.boundingBox?.northWestLat}")
            tokens.add("${FS.Restaurant_latitude}  >= ${filter.boundingBox?.southEastLat}")
            tokens.add("${FS.Restaurant_longitude} >= ${filter.boundingBox?.northWestLng}")
            tokens.add("${FS.Restaurant_longitude} <= ${filter.boundingBox?.southEastLng}")
        }

        return tokens.and()
    }
}