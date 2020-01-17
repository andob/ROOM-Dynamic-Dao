package ro.dobrescuandrei.roomdynamicdao

import com.yatatsu.fieldschema.FS
import org.junit.Test

import org.junit.Assert.*
import ro.dobrescuandrei.roomdynamicdao.model.BoundingBox
import ro.dobrescuandrei.roomdynamicdao.model.RestaurantFilter
import ro.dobrescuandrei.roomdynamicdao.querybuilder.RestaurantListQueryBuilder

class RestaurantListQueryBuilderTests
{
    @Test
    fun testWithoutFilter()
    {
        val filter=RestaurantFilter()

        val queryBuilder=RestaurantListQueryBuilder(filter)
        val resultQuery=queryBuilder.build().sql!!.removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} where 1==1"

        assertEquals(resultQuery, expectedQuery)
    }

    @Test
    fun testWithRatingFilter()
    {
        val filter=RestaurantFilter()
        filter.rating=4

        val queryBuilder=RestaurantListQueryBuilder(filter)
        val resultQuery=queryBuilder.build().sql!!.removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} "+
                "where ${FS.Restaurant_rating} = ${filter.rating}"

        assertEquals(resultQuery, expectedQuery)
    }

    @Test
    fun testWithBoundingBoxFilter()
    {
        val filter=RestaurantFilter()
        filter.boundingBox=BoundingBox(northWestLat = 1.0, northWestLng = 2.0, southEastLat = 3.0, southEastLng = 4.0)

        val queryBuilder=RestaurantListQueryBuilder(filter)
        val resultQuery=queryBuilder.build().sql!!.removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} "+
                "where ${FS.Restaurant_latitude} <= ${filter.boundingBox!!.northWestLat} and ${FS.Restaurant_latitude} >= ${filter.boundingBox!!.southEastLat} "+
                "and ${FS.Restaurant_longitude} >= ${filter.boundingBox!!.northWestLng} and ${FS.Restaurant_longitude} <= ${filter.boundingBox!!.southEastLng}"

        assertEquals(resultQuery, expectedQuery)
    }

    @Test
    fun testWithBoundingBoxAndRatingFilter()
    {
        val filter=RestaurantFilter()
        filter.boundingBox=BoundingBox(northWestLat = 1.0, northWestLng = 2.0, southEastLat = 3.0, southEastLng = 4.0)
        filter.rating=4

        val queryBuilder=RestaurantListQueryBuilder(filter)
        val resultQuery=queryBuilder.build().sql!!.removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} "+
                "where ${FS.Restaurant_rating} = ${filter.rating} "+
                "and ${FS.Restaurant_latitude} <= ${filter.boundingBox!!.northWestLat} and ${FS.Restaurant_latitude} >= ${filter.boundingBox!!.southEastLat} "+
                "and ${FS.Restaurant_longitude} >= ${filter.boundingBox!!.northWestLng} and ${FS.Restaurant_longitude} <= ${filter.boundingBox!!.southEastLng}"

        assertEquals(resultQuery, expectedQuery)
    }
}
