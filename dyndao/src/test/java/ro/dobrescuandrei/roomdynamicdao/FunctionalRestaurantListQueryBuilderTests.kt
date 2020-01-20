package ro.dobrescuandrei.roomdynamicdao

import com.yatatsu.fieldschema.FS
import org.junit.Assert.assertEquals
import org.junit.Test
import ro.dobrescuandrei.roomdynamicdao.model.RestaurantFilter
import ro.dobrescuandrei.roomdynamicdao.querybuilder.toSQLiteQuery

class FunctionalRestaurantListQueryBuilderTests
{
    @Test
    fun testFunctionalQueryBuilder()
    {
        val filter=RestaurantFilter()
        filter.rating=4

        val resultQuery=filter.toSQLiteQuery().sql!!.removeUnnecessarySpaces()
        val expectedQuery="select ${FS.Restaurant}.*, "+
                    "${FS.City}.${FS.City_name} as ${FS.RestaurantJoin_cityName} "+
                "from ${FS.Restaurant} "+
                "inner join ${FS.City} on ${FS.Restaurant}.${FS.Restaurant_cityId} = ${FS.City}.${FS.City_id} "+
                "where ${FS.Restaurant_rating} = ${filter.rating} "+
                "order by id asc"

        assertEquals(resultQuery, expectedQuery)
    }
}
