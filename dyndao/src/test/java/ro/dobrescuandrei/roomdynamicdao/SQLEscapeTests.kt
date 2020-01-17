package ro.dobrescuandrei.roomdynamicdao

import com.yatatsu.fieldschema.FS
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import ro.andreidobrescu.basefilter.BaseFilter
import ro.dobrescuandrei.roomdynamicdao.model.Restaurant

class SQLEscapeTests
{
    val dummyQueryBuilder=object : QueryBuilder<BaseFilter>(object : BaseFilter() {})
    {
        override fun tableName() = ""
        override fun where(conditions : QueryWhereConditions) = "1=1"
    }

    inline fun runOnQueryBuilder(toRun : QueryBuilder<BaseFilter>.() -> (Unit)) = toRun(dummyQueryBuilder)

    @Test
    fun testStringEscape()
    {
        runOnQueryBuilder {
            val stringToEscape="stuff"
            val resultCondition="where ${FS.Restaurant_name} = ${stringToEscape.sqlEscaped}".removeUnnecessarySpaces()
            val expectedCondition="where ${FS.Restaurant_name} = 'stuff'"

            assertEquals(resultCondition, expectedCondition)
        }
    }

    @Test
    fun testAnotherStringEscape()
    {
        runOnQueryBuilder {
            val stringToEscape="st'; drop database db;"
            val resultCondition="where ${FS.Restaurant_name} = ${stringToEscape.sqlEscaped}".removeUnnecessarySpaces()
            val expectedCondition="where ${FS.Restaurant_name} = 'st''; drop database db;'"

            assertEquals(resultCondition, expectedCondition)
        }
    }

    @Test
    fun testStringArrayEscape()
    {
        runOnQueryBuilder {
            val stringArrayToEscape=arrayOf("some", "strings")
            val resultCondition="where ${FS.Restaurant_name} in ${stringArrayToEscape.sqlEscaped}".removeUnnecessarySpaces()
            val expectedCondition="where ${FS.Restaurant_name} in ('some', 'strings')"

            assertEquals(resultCondition, expectedCondition)
        }
    }

    @Test
    fun testStringListEscape()
    {
        runOnQueryBuilder {
            val stringListToEscape=listOf("some", "strings")
            val resultCondition="where ${FS.Restaurant_name} in ${stringListToEscape.sqlEscaped}".removeUnnecessarySpaces()
            val expectedCondition="where ${FS.Restaurant_name} in ('some', 'strings')"

            assertEquals(resultCondition, expectedCondition)
        }
    }

    @Test
    fun testIntArrayEscape()
    {
        runOnQueryBuilder {
            val intArrayToEscape=intArrayOf(1, 2)
            val resultCondition="where ${FS.Restaurant_name} in ${intArrayToEscape.sqlEscaped}".removeUnnecessarySpaces()
            val expectedCondition="where ${FS.Restaurant_name} in (1, 2)"

            assertEquals(resultCondition, expectedCondition)
        }
    }

    @Test
    fun testLongArrayEscape()
    {
        runOnQueryBuilder {
            val intArrayToEscape=longArrayOf(1L, 2L)
            val resultCondition="where ${FS.Restaurant_name} in ${intArrayToEscape.sqlEscaped}".removeUnnecessarySpaces()
            val expectedCondition="where ${FS.Restaurant_name} in (1, 2)"

            assertEquals(resultCondition, expectedCondition)
        }
    }

    @Test
    fun testFloatArrayEscape()
    {
        runOnQueryBuilder {
            val floatArrayToEscape=floatArrayOf(1.1f, 2.2f)
            val resultCondition="where ${FS.Restaurant_name} in ${floatArrayToEscape.sqlEscaped}".removeUnnecessarySpaces()
            val expectedCondition="where ${FS.Restaurant_name} in (1.1, 2.2)"

            assertEquals(resultCondition, expectedCondition)
        }
    }

    @Test
    fun testDoubleArrayEscape()
    {
        runOnQueryBuilder {
            val doubleArrayToEscape=doubleArrayOf(1.1, 2.2)
            val resultCondition="where ${FS.Restaurant_name} in ${doubleArrayToEscape.sqlEscaped}".removeUnnecessarySpaces()
            val expectedCondition="where ${FS.Restaurant_name} in (1.1, 2.2)"

            assertEquals(resultCondition, expectedCondition)
        }
    }

    @Test
    fun testNumberArrayEscape()
    {
        runOnQueryBuilder {
            val numberArrayToEscape=arrayOf<Number>(1, 2L, 3.3f, 4.4)
            val resultCondition="where ${FS.Restaurant_name} in ${numberArrayToEscape.sqlEscaped}".removeUnnecessarySpaces()
            val expectedCondition="where ${FS.Restaurant_name} in (1, 2, 3.3, 4.4)"

            assertEquals(resultCondition, expectedCondition)
        }
    }

    @Test
    fun testNumberListEscape()
    {
        runOnQueryBuilder {
            val numberListToEscape=listOf<Number>(1, 2L, 3.3f, 4.4)
            val resultCondition="where ${FS.Restaurant_name} in ${numberListToEscape.sqlEscaped}".removeUnnecessarySpaces()
            val expectedCondition="where ${FS.Restaurant_name} in (1, 2, 3.3, 4.4)"

            assertEquals(resultCondition, expectedCondition)
        }
    }

    @Test
    fun testRestaurantArrayEscape()
    {
        runOnQueryBuilder {
            try
            {
                val restaurantArrayToEscape=arrayOf(Restaurant())
                restaurantArrayToEscape.sqlEscaped
                fail()
            }
            catch (ex : Exception)
            {
                assertEquals(ex.message, "Cannot escape ${Restaurant::class.java.name}")
            }
        }
    }

    @Test
    fun testRestaurantListEscape()
    {
        runOnQueryBuilder {
            try
            {
                val restaurantListToEscape=listOf(Restaurant())
                restaurantListToEscape.sqlEscaped
                fail()
            }
            catch (ex : Exception)
            {
                assertEquals(ex.message, "Cannot escape ${Restaurant::class.java.name}")
            }
        }
    }

    @Test
    fun testEmptyArrayEscape()
    {
        runOnQueryBuilder {
            try
            {
                val emptyArrayToEscape=arrayOf<Restaurant>()
                emptyArrayToEscape.sqlEscaped
                fail()
            }
            catch (ex : Exception)
            {
                assertEquals(ex.message, "Cannot escape empty collection")
            }
        }
    }

    @Test
    fun testEmptyListEscape()
    {
        runOnQueryBuilder {
            try
            {
                val emptyListToEscape=listOf<Restaurant>()
                emptyListToEscape.sqlEscaped
                fail()
            }
            catch (ex : Exception)
            {
                assertEquals(ex.message, "Cannot escape empty collection")
            }
        }
    }

    @Test
    fun testTrueBooleanEscape()
    {
        runOnQueryBuilder {
            val resultCondition="where someFlag = ${true.sqlEscaped}"
            val expectedCondition="where someFlag = 1"

            assertEquals(resultCondition, expectedCondition)
        }
    }

    @Test
    fun testFalseBooleanEscape()
    {
        runOnQueryBuilder {
            val resultCondition="where someFlag = ${false.sqlEscaped}"
            val expectedCondition="where someFlag = 0"

            assertEquals(resultCondition, expectedCondition)
        }
    }
}
