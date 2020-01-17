package ro.dobrescuandrei.roomdynamicdao

import com.yatatsu.fieldschema.FS
import org.junit.Assert.assertEquals
import org.junit.Test
import ro.andreidobrescu.basefilter.BaseFilterDefaults
import ro.dobrescuandrei.roomdynamicdao.model.RestaurantFilter
import ro.dobrescuandrei.roomdynamicdao.querybuilder.RestaurantJoinListQueryBuilder

class RestaurantJoinListQueryBuilderTests
{
    @Test
    fun testJoinsAndProjections()
    {
        val filter=RestaurantFilter()

        val queryBuilder=RestaurantJoinListQueryBuilder(filter)
        val resultQuery=queryBuilder.build().sql!!.removeUnnecessarySpaces()
        val expectedQuery="select ${FS.Restaurant}.*, "+
                    "${FS.City}.${FS.City_name} as ${FS.RestaurantJoin_cityName}, "+
                    "${FS.Country}.${FS.Country_name} as ${FS.RestaurantJoin_countryName} "+
                "from ${FS.Restaurant} "+
                "inner join ${FS.City} on ${FS.Restaurant}.${FS.Restaurant_cityId} = ${FS.City}.${FS.City_id} "+
                "inner join ${FS.Country} on ${FS.City}.${FS.City_countryId} = ${FS.Country}.${FS.Country_id} "+
                "where 1=1 "+
                "order by ${FS.Restaurant_id} asc "+
                (if (QueryBuilderDefaults.enablePagination)
                    "limit ${BaseFilterDefaults.limit} offset 0"
                else "")

        assertEquals(resultQuery, expectedQuery.trim())
    }

    @Test
    fun testJoinsAndProjectionsWithModifiedDefaultLimit()
    {
        BaseFilterDefaults.limit=10000
        testJoinsAndProjections()
        BaseFilterDefaults.limit=100
    }

    @Test
    fun testJoinsAndProjectionsWithModifiedDefaultPaginationBehavior()
    {
        QueryBuilderDefaults.enablePagination=true
        testJoinsAndProjections()
        QueryBuilderDefaults.enablePagination=false
    }
}