package ro.dobrescuandrei.roomdynamicdao

import com.yatatsu.fieldschema.FS
import org.junit.Assert.assertEquals
import org.junit.Test

class QueryWhereConditionsTests
{
    @Test
    fun testWhereWithOneCondition()
    {
        val conditions=QueryWhereConditions()

        val restaurantId=5

        conditions.add("${FS.Restaurant_id} = $restaurantId")

        val resultQuery="select * from ${FS.Restaurant} where ${conditions.mergeWithAnd()}".removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} "+
                "where ${FS.Restaurant_id} = $restaurantId"

        assertEquals(resultQuery, expectedQuery)
    }

    @Test
    fun testWhereWithAndConditions()
    {
        val conditions=QueryWhereConditions()

        val search="string to search"
        val restaurantId=5
        val restaurantRating=5

        conditions.addSearchConditions(search, onColumns = arrayOf(FS.Restaurant_name))
        conditions.add("${FS.Restaurant_id} = $restaurantId")
        conditions.add("${FS.Restaurant_rating} = $restaurantRating")

        val resultQuery="select * from ${FS.Restaurant} where ${conditions.mergeWithAnd()}".removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} "+
                "where ${FS.Restaurant_name} like '%$search%' "+
                "and ${FS.Restaurant_id} = $restaurantId "+
                "and ${FS.Restaurant_rating} = $restaurantRating"

        assertEquals(resultQuery, expectedQuery)
    }

    @Test
    fun testWhereWithOrConditions()
    {
        val conditions=QueryWhereConditions()

        val search="string to search"
        val restaurantId=5
        val restaurantRating=5

        conditions.addSearchConditions(search, onColumns = arrayOf(FS.Restaurant_name, FS.RestaurantJoin_cityName))
        conditions.add("${FS.Restaurant_id} = $restaurantId")
        conditions.add("${FS.Restaurant_rating} = $restaurantRating")

        val resultQuery="select * from ${FS.Restaurant} where ${conditions.mergeWithOr()}".removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} "+
                "where (" +
                    "${FS.Restaurant_name} like '%$search%' "+
                    "or ${FS.RestaurantJoin_cityName} like '%$search%'" +
                ") or ${FS.Restaurant_id} = $restaurantId "+
                "or ${FS.Restaurant_rating} = $restaurantRating"

        assertEquals(resultQuery, expectedQuery)
    }

    @Test
    fun testWhereWithSubconditions()
    {
        val conditions=QueryWhereConditions()

        val search="string to search"

        conditions.addSearchConditions(search, onColumns = arrayOf(FS.Restaurant_name, FS.RestaurantJoin_cityName))

        val firstRestaurantId=5
        val firstRestaurantRating=5
        val firstSubconditionSet=QueryWhereConditions()
        firstSubconditionSet.add("${FS.Restaurant_id} = $firstRestaurantId")
        firstSubconditionSet.add("${FS.Restaurant_rating} = $firstRestaurantRating")
        conditions.add("(${firstSubconditionSet.mergeWithAnd()})")

        val secondRestaurantId=5
        val secondRestaurantRating=5
        val secondSubconditionSet=QueryWhereConditions()
        secondSubconditionSet.add("${FS.Restaurant_id} = $secondRestaurantId")
        secondSubconditionSet.add("${FS.Restaurant_rating} = $secondRestaurantRating")
        conditions.add("(${secondSubconditionSet.mergeWithAnd()})")

        val resultQuery="select * from ${FS.Restaurant} where ${conditions.mergeWithOr()}".removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} "+
                "where (" +
                    "${FS.Restaurant_name} like '%$search%' "+
                    "or ${FS.RestaurantJoin_cityName} like '%$search%'" +
                ") or ("+
                    "${FS.Restaurant_id} = $firstRestaurantId "+
                    "and ${FS.Restaurant_rating} = $firstRestaurantRating"+
                ") or ("+
                    "${FS.Restaurant_id} = $secondRestaurantId "+
                    "and ${FS.Restaurant_rating} = $secondRestaurantRating"+
                ")"

        assertEquals(resultQuery, expectedQuery)
    }

    @Test
    fun testEmptyWhereCondition()
    {
        val conditions=QueryWhereConditions()

        val resultQuery="select * from ${FS.Restaurant} where ${conditions.mergeWithAnd()}".removeUnnecessarySpaces()
        val expectedQuery="select * from ${FS.Restaurant} where 1==1"

        assertEquals(resultQuery, expectedQuery)
    }
}
